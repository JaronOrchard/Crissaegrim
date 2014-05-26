package entities;

import geometry.Coordinate;
import items.Item;
import items.Weapon;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import attack.Attack;
import board.Board;
import board.tiles.CollisionDetectionTile;
import busy.Busy;
import busy.SwordSwingBusy;

public class EntityMovementHelper {
	
	private double verticalMomentum = 0;
	private double horizontalMomentum = 0;
	
	private boolean leftMovementRequested;
	private boolean rightMovementRequested;
	private boolean jumpMovementRequested;
	private Item itemToUse;
	private boolean onTheGround;
	private boolean currentlyJumping;
	private boolean holdingJumpButton;
	
	private final Entity parentEntity;
	private final Map<String, Board> boardMap;
	public Map<String, Board> getBoardMap() { return boardMap; }
	
	public EntityMovementHelper(Entity entity, Map<String, Board> boards) {
		resetMovementRequests();
		onTheGround = false;
		currentlyJumping = false;
		holdingJumpButton = false;
		parentEntity = entity;
		boardMap = boards;
	}
	
	public void resetMovementRequests() {
		leftMovementRequested = false;
		rightMovementRequested = false;
		jumpMovementRequested = false;
		itemToUse = null;
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
	
	public void requestUseItem(Item item) {
		itemToUse = item;
	}
	
	public Attack moveEntity() {
		Attack returnedAttack = null;
		Coordinate startingPosition;
		Coordinate endingPosition;
		
		Busy parentEntityBusy = parentEntity.getBusy();
		if (parentEntityBusy != null) {
			if (parentEntityBusy.getCompletelyImmobilized() || (parentEntityBusy.getCannotWalk() && onTheGround)) {
				resetMovementRequests();
			}
		}
		
		if (itemToUse != null && !parentEntity.isBusy() && itemToUse instanceof Weapon) {
			// TODO: This should be split up depending upon the weapon and attack type
			// TODO: Bounding rect of sword swing should not be entire entity
			parentEntity.setBusy(new SwordSwingBusy());
			returnedAttack = new Attack(parentEntity.getId(), parentEntity.getSwordSwingRect(), 1);
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
		List<CollisionDetectionTile> nearbyTiles = parentEntity.getCurrentBoard().getCollisionDetectionTilesNearEntity(parentEntity.getPosition());
		Collections.swap(nearbyTiles, 0, 7); // A little hack to ensure that the Tile
		Collections.swap(nearbyTiles, 1, 6); //    the player is in always takes
		Collections.swap(nearbyTiles, 2, 8); //    precedence for raisedEndingPosition
		
		// 2) Perform up/down collision detection and movement
		startingPosition = new Coordinate(parentEntity.getPosition());
		endingPosition = new Coordinate(parentEntity.getPosition());
		if (jumpMovementRequested && onTheGround && !holdingJumpButton) {
			verticalMomentum = parentEntity.getJumpMomentum();
			currentlyJumping = true;
		}
		verticalMomentum = Math.max(verticalMomentum + parentEntity.getGravityAcceleration(), parentEntity.getGravityTerminalVelocity());
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
					if (collisionDetectionTile.entityBodyCollides(parentEntity, startingPosition, endingPosition)) {
						headBodyCollisionDetected = true;
					}
				}
			}
			// If there was no body/head collision, see if the player needs to be moved up vertically due to a slope
			if (!headBodyCollisionDetected) {
				Coordinate raisedEndingPosition = null;
				for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) { // (COULD BE REDUCED TO ONLY THE TILE THE PLAYER'S FEET ARE IN?  MAYBE?)
					if (raisedEndingPosition == null) {
						raisedEndingPosition = collisionDetectionTile.entityFeetCollide(parentEntity, startingPosition, endingPosition, endingPosition.getY() <= startingPosition.getY());
					}
				}
				// If there was no feet collision, ending position is good to go.
				// Otherwise, we must check to make sure the bumped-up ending position doesn't collide with something new.
				if (raisedEndingPosition == null) {
					parentEntity.getPosition().setAll(endingPosition);
				} else {
					boolean adjustedHeadBodyCollisionDetected = false;
					for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
						if (!adjustedHeadBodyCollisionDetected) {
							if (collisionDetectionTile.entityBodyCollides(parentEntity, startingPosition, raisedEndingPosition)) {
								adjustedHeadBodyCollisionDetected = true;
							}
						}
					}
					if (!adjustedHeadBodyCollisionDetected) {
						parentEntity.getPosition().setAll(raisedEndingPosition);
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
		startingPosition = new Coordinate(parentEntity.getPosition());
		endingPosition = new Coordinate(parentEntity.getPosition());
		if (leftMovementRequested != rightMovementRequested) {
			parentEntity.setFacingRight(rightMovementRequested);
			if (onTheGround || currentlyJumping) {
				if (leftMovementRequested) { horizontalMomentum = -parentEntity.getHorizontalMovementSpeed(); }
				if (rightMovementRequested) { horizontalMomentum = parentEntity.getHorizontalMovementSpeed(); }
			} else {
				if (leftMovementRequested) { horizontalMomentum = -parentEntity.getHorizontalMovementSpeed() * parentEntity.getHorizontalAirPenaltyFactor(); }
				if (rightMovementRequested) { horizontalMomentum = parentEntity.getHorizontalMovementSpeed() * parentEntity.getHorizontalAirPenaltyFactor(); }
			}
		} else {
			if (onTheGround) {
				horizontalMomentum = 0;
			} else {
				if (horizontalMomentum <= parentEntity.getHorizontalAirTaperOffLimit() && horizontalMomentum >= -parentEntity.getHorizontalAirTaperOffLimit()) {
					horizontalMomentum = 0;
				} else {
					horizontalMomentum *= parentEntity.getHorizontalAirTaperOffFactor();
				}
			}
		}
		endingPosition.incrementX(horizontalMomentum);
		
		if (startingPosition.getX() != endingPosition.getX()) {
			// If the player's head or body collides with any tile, do not allow this movement:
			boolean headBodyCollisionDetected = false;
			for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
				if (!headBodyCollisionDetected) {
					if (collisionDetectionTile.entityBodyCollides(parentEntity, startingPosition, endingPosition)) {
						headBodyCollisionDetected = true;
					}
				}
			}
			// If there was no body/head collision, see if the player needs to be moved up vertically due to a slope
			if (!headBodyCollisionDetected) {
				Coordinate raisedEndingPosition = null;
				for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) { // (COULD BE REDUCED TO ONLY THE TILE THE PLAYER'S FEET ARE IN?)
					if (raisedEndingPosition == null) {
						raisedEndingPosition = collisionDetectionTile.entityFeetCollide(parentEntity, startingPosition, endingPosition, !onTheGround);
					}
				}
				// If there was no feet collision, ending position is good to go.
				// Otherwise, we must check to make sure the bumped-up ending position doesn't collide with something new.
				if (raisedEndingPosition == null) {
					parentEntity.getPosition().setAll(endingPosition);
				} else if (onTheGround) {
					boolean adjustedHeadBodyCollisionDetected = false;
					for (CollisionDetectionTile collisionDetectionTile : nearbyTiles) {
						if (!adjustedHeadBodyCollisionDetected) {
							if (collisionDetectionTile.entityBodyCollides(parentEntity, startingPosition, raisedEndingPosition)) {
								adjustedHeadBodyCollisionDetected = true;
							}
						}
					}
					if (!adjustedHeadBodyCollisionDetected) {
						parentEntity.getPosition().setAll(raisedEndingPosition);
					}
				}
			}
		}
		
		// 4) Reset movement requests for the next frame
		resetMovementRequests();
		
		// 5) Return generated Attack (or null if none)
		return returnedAttack;
	}
	
}
