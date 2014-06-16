package entities;

import geometry.Coordinate;
import items.Item;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import board.Board;
import board.tiles.CollisionDetectionTile;
import busy.Busy;
import busy.GotHitByAttackBouncedBackBusy;

public class EntityMovementHelper {
	
	private static final transient double BOUNCE_BACK_VERTICAL_MOMENTUM = 0.11;
	private static final transient double BOUNCE_BACK_HORIZONTAL_MOMENTUM = 0.11;
	private static final transient double BOUNCE_BACK_TAPER_OFF_FACTOR = 0.95;
	private static final transient double BOUNCE_BACK_TAPER_OFF_LIMIT = 0.01;
	
	private double verticalMomentum = 0;
	private double horizontalMomentum = 0;
	
	private boolean leftMovementRequested;
	private boolean rightMovementRequested;
	private boolean jumpMovementRequested;
	private Item itemToUse;
	private Coordinate coordinateClicked;
	private boolean onTheGround;
	private boolean currentlyJumping;
	private boolean holdingJumpButton;
	private boolean currentlyBouncingBackFromAttack;
	
	private final Entity parentEntity;
	private final Map<String, Board> boardMap;
	
	public Item getItemToUse() { return itemToUse; }
	public Coordinate getCoordinateClicked() { return coordinateClicked; }
	public Map<String, Board> getBoardMap() { return boardMap; }
	public boolean isCurrentlyBouncingBackFromAttack() { return currentlyBouncingBackFromAttack; }
	
	public EntityMovementHelper(Entity entity, Map<String, Board> boards) {
		resetMovementRequests();
		onTheGround = false;
		currentlyJumping = false;
		holdingJumpButton = false;
		currentlyBouncingBackFromAttack = false;
		parentEntity = entity;
		boardMap = boards;
	}
	
	public void resetMovementRequests() {
		leftMovementRequested = false;
		rightMovementRequested = false;
		jumpMovementRequested = false;
		itemToUse = null;
		coordinateClicked = null;
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
	
	public void requestUseItem(Coordinate coordinate, Item item) {
		coordinateClicked = coordinate;
		itemToUse = item;
	}
	
	/**
	 * Makes this Entity bounce backwards after getting hit by an Attack.
	 * @param bounceLeft {@code true} to make the Entity bounce leftward, {@code false} to bounce rightward
	 */
	public void bounceBackFromAttack(boolean bounceLeft) {
		parentEntity.setFacingRight(bounceLeft);
		verticalMomentum = onTheGround ? BOUNCE_BACK_VERTICAL_MOMENTUM : 0;
		horizontalMomentum = (bounceLeft ? -1 : 1) * BOUNCE_BACK_HORIZONTAL_MOMENTUM * (onTheGround ? 1 : 1.2);
		parentEntity.setBusy(new GotHitByAttackBouncedBackBusy(parentEntity.getStunnedTexture()));
		currentlyBouncingBackFromAttack = true;
	}
	
	public void moveEntity() {
		moveEntityPre();
		moveEntityPost();
	}
	
	public void moveEntityPre() {
		Busy parentEntityBusy = parentEntity.getBusy();
		if (parentEntityBusy != null) {
			if (parentEntityBusy.getCompletelyImmobilized() || (parentEntityBusy.getCannotWalk() && onTheGround)) {
				resetMovementRequests();
			}
		}
	}
	
	public void moveEntityPost() {
		Coordinate startingPosition;
		Coordinate endingPosition;
		
		// Planned movement rules:
		// 
		// - Can only jump when on the ground
		// - If you leave the ground by jumping, you are in the jump state until you hit the GROUND again
		// - If in the jump state and did not press the jump button this frame and are moving upwards, vertical momentum is now 0
		//     (unless in the middle of a bouncing back from being hit by an attack)
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
		if (!jumpMovementRequested && currentlyJumping && verticalMomentum > 0 && !currentlyBouncingBackFromAttack) {
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
						raisedEndingPosition = collisionDetectionTile.entityFeetCollide(parentEntity, startingPosition, endingPosition, endingPosition.getY() <= startingPosition.getY(), onTheGround);
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
						if (horizontalMomentum <= BOUNCE_BACK_TAPER_OFF_LIMIT && horizontalMomentum >= -BOUNCE_BACK_TAPER_OFF_LIMIT) {
							currentlyBouncingBackFromAttack = false;
						}
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
				if (currentlyBouncingBackFromAttack) {
					horizontalMomentum *= BOUNCE_BACK_TAPER_OFF_FACTOR;
				} else {
					horizontalMomentum = 0;
				}
			} else {
				if (horizontalMomentum <= parentEntity.getHorizontalAirTaperOffLimit() && horizontalMomentum >= -parentEntity.getHorizontalAirTaperOffLimit()) {
					horizontalMomentum = 0;
				} else if (!currentlyBouncingBackFromAttack) {
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
						raisedEndingPosition = collisionDetectionTile.entityFeetCollide(parentEntity, startingPosition, endingPosition, !onTheGround, onTheGround);
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
	}
	
}
