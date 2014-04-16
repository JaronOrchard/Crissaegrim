package players;

import geometry.Coordinate;
import items.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import crissaegrim.Crissaegrim;
import attack.Attack;
import board.tiles.CollisionDetectionTile;
import busy.BusySwordSwing;

public class PlayerMovementHelper {
	
	private double verticalMomentum = 0;
	private double horizontalMomentum = 0;
	
	private boolean leftMovementRequested;
	private boolean rightMovementRequested;
	private boolean jumpMovementRequested;
	private boolean useItemRequested;
	private boolean onTheGround;
	private boolean currentlyJumping;
	private boolean holdingJumpButton;
	private List<Attack> attackList;
	
	public List<Attack> getAttackList() { return attackList; }
	
	public PlayerMovementHelper() {
		resetMovementRequests();
		onTheGround = false;
		currentlyJumping = false;
		holdingJumpButton = false;
		attackList = new ArrayList<Attack>();
	}
	
	public void resetMovementRequests() {
		leftMovementRequested = false;
		rightMovementRequested = false;
		jumpMovementRequested = false;
		useItemRequested = false;
	}
	
	public void requestLeftMovement() {
		leftMovementRequested = true;
	}
	
	public void requestRightMovement() {
		rightMovementRequested = true;
	}
	
	public void requestJumpMovement() {
		jumpMovementRequested = true;
	}
	
	public void requestUseItem() {
		useItemRequested = true;
	}
	
	public void movePlayer() {
		Player player = Crissaegrim.getPlayer();
		Coordinate startingPosition;
		Coordinate endingPosition;
		
		attackList.clear();		
		if (useItemRequested && !player.isBusy() && player.getInventory().getCurrentItem() instanceof Weapon) {
			player.setBusy(new BusySwordSwing());
		}
		if (player.isBusy() && player.getBusyStatus() instanceof BusySwordSwing) {
			attackList.add(new Attack(player.getId(), player.getSwordSwingRect(), true));
		}
		
		// Planned movement rules:
		// 
		// - Can only jump when on the ground
		// - If you leave the ground by jumping, you are in the jump state until you hit the GROUND again
		// - If in the jump state and did not press the jump button this frame and are moving upwards, vertical momentum is now 0
		// - Hitting your head or body during vertical movement resets vertical momentum to 0
		//
		// - If left/right requested and on the ground or in the jump state, move left/right with horizontal movement speed
		// - If left/right requested and not on the ground and not in the jump state, move left/right with severely penalized horizontal movement speed
		// - If left/right not requested and not on the ground, horizontal momentum quickly tapers off towards 0
		
		// 1) Get the 18 tiles adjacent to the player for collision detection
		List<CollisionDetectionTile> nearbyTiles = Crissaegrim.getBoard().getCollisionDetectionTilesNearPlayer(player.getPosition());
		Collections.swap(nearbyTiles, 0, 7); // A little hack to ensure that the Tile
		Collections.swap(nearbyTiles, 1, 6); //    the player is in always takes
		Collections.swap(nearbyTiles, 2, 8); //    precedence for raisedEndingPosition
		
		// 2) Perform up/down collision detection and movement
		startingPosition = new Coordinate(player.getPosition());
		endingPosition = new Coordinate(player.getPosition());
		if (jumpMovementRequested && onTheGround && !holdingJumpButton) {
			verticalMomentum = player.getJumpMomentum();
			currentlyJumping = true;
		}
		verticalMomentum = Math.max(verticalMomentum + player.getGravityAcceleration(), player.getGravityTerminalVelocity());
		if (!jumpMovementRequested && currentlyJumping && verticalMomentum > 0) {
			verticalMomentum = 0;
		}
		holdingJumpButton = jumpMovementRequested;
		endingPosition.incrementY(verticalMomentum);
		
		if (startingPosition.getY() != endingPosition.getY()) {
			onTheGround = false;
			// If the player's head or body collides with any tile, do not allow this movement:
			boolean headBodyCollisionDetected = false;
			for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
				if (!headBodyCollisionDetected) {
					if (collisionDetectionTile.playerBodyCollides(player, startingPosition, endingPosition)) {
						headBodyCollisionDetected = true;
					}
				}
			}
			// If there was no body/head collision, see if the player needs to be moved up vertically due to a slope
			if (!headBodyCollisionDetected) {
				Coordinate raisedEndingPosition = null;
				for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) { // (COULD BE REDUCED TO ONLY THE TILE THE PLAYER'S FEET ARE IN?  MAYBE?)
					if (raisedEndingPosition == null) {
						raisedEndingPosition = collisionDetectionTile.playerFeetCollide(player, startingPosition, endingPosition, endingPosition.getY() <= startingPosition.getY());
					}
				}
				// If there was no feet collision, ending position is good to go.
				// Otherwise, we must check to make sure the bumped-up ending position doesn't collide with something new.
				if (raisedEndingPosition == null) {
					player.getPosition().setAll(endingPosition.getX(), endingPosition.getY());
				} else {
					boolean adjustedHeadBodyCollisionDetected = false;
					for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
						if (!adjustedHeadBodyCollisionDetected) {
							if (collisionDetectionTile.playerBodyCollides(player, startingPosition, raisedEndingPosition)) {
								adjustedHeadBodyCollisionDetected = true;
							}
						}
					}
					if (!adjustedHeadBodyCollisionDetected) {
						player.getPosition().setAll(raisedEndingPosition.getX(), raisedEndingPosition.getY());
						verticalMomentum = 0;
						onTheGround = true;
						currentlyJumping = false;
					}
				}
			} else {
				verticalMomentum = 0;
			}
		}
		
		// 3) Perform left/right collision detection and movement
		startingPosition = new Coordinate(player.getPosition());
		endingPosition = new Coordinate(player.getPosition());
		if (leftMovementRequested != rightMovementRequested) {
			player.setFacingRight(rightMovementRequested);
			if (onTheGround || currentlyJumping) {
				if (leftMovementRequested) { horizontalMomentum = -player.getHorizontalMovementSpeed(); }
				if (rightMovementRequested) { horizontalMomentum = player.getHorizontalMovementSpeed(); }
			} else {
				if (leftMovementRequested) { horizontalMomentum = -player.getHorizontalMovementSpeed() * player.getHorizontalAirPenaltyFactor(); }
				if (rightMovementRequested) { horizontalMomentum = player.getHorizontalMovementSpeed() * player.getHorizontalAirPenaltyFactor(); }
			}
		} else {
			if (onTheGround) {
				horizontalMomentum = 0;
			} else {
				if (horizontalMomentum <= player.getHorizontalAirTaperOffLimit() && horizontalMomentum >= -player.getHorizontalAirTaperOffLimit()) {
					horizontalMomentum = 0;
				} else {
					horizontalMomentum *= player.getHorizontalAirTaperOffFactor();
				}
			}
		}
		endingPosition.incrementX(horizontalMomentum);
		
		if (startingPosition.getX() != endingPosition.getX()) {
			// If the player's head or body collides with any tile, do not allow this movement:
			boolean headBodyCollisionDetected = false;
			for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
				if (!headBodyCollisionDetected) {
					if (collisionDetectionTile.playerBodyCollides(player, startingPosition, endingPosition)) {
						headBodyCollisionDetected = true;
					}
				}
			}
			// If there was no body/head collision, see if the player needs to be moved up vertically due to a slope
			if (!headBodyCollisionDetected) {
				Coordinate raisedEndingPosition = null;
				for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) { // (COULD BE REDUCED TO ONLY THE TILE THE PLAYER'S FEET ARE IN?)
					if (raisedEndingPosition == null) {
						raisedEndingPosition = collisionDetectionTile.playerFeetCollide(player, startingPosition, endingPosition, !onTheGround);
					}
				}
				// If there was no feet collision, ending position is good to go.
				// Otherwise, we must check to make sure the bumped-up ending position doesn't collide with something new.
				if (raisedEndingPosition == null) {
					player.getPosition().setAll(endingPosition.getX(), endingPosition.getY());
				} else if (onTheGround) {
					boolean adjustedHeadBodyCollisionDetected = false;
					for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
						if (!adjustedHeadBodyCollisionDetected) {
							if (collisionDetectionTile.playerBodyCollides(player, startingPosition, raisedEndingPosition)) {
								adjustedHeadBodyCollisionDetected = true;
							}
						}
					}
					if (!adjustedHeadBodyCollisionDetected) {
						player.getPosition().setAll(raisedEndingPosition.getX(), raisedEndingPosition.getY());
					}
				}
			}
		}
		
		// 4) Reset movement requests for the next frame
		resetMovementRequests();
	}
	
}
