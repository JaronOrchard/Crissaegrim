package items;

import static org.lwjgl.opengl.GL11.*;

import java.util.Date;
import java.util.List;

import board.Board;
import board.tiles.CollisionDetectionTile;
import thunderbrand.Constants;
import thunderbrand.Thunderbrand;
import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;
import gldrawer.GLDrawer;

public class LocalDroppedItem {
	
	private static final double GRAVITY_ACCELERATION = -0.0045;
	private static final double GRAVITY_TERMINAL_VELOCITY = -0.4;
	
	private final int id;
	private final Item item;
	private Coordinate position;
	private Board board;
	private double horizontalVelocity;
	private double verticalVelocity;
	private Long hitGroundTime;
	private Rect bounds;
	
	public int getId() { return id; }
	public String getBoardName() { return board.getName(); }
	public Rect getBounds() { return bounds; }
	
	public LocalDroppedItem(Item item, Coordinate pos, Board currentBoard, boolean facingRight) {
		id = Thunderbrand.getNextDroppedItemId();
		this.item = item;
		position = new Coordinate(pos);
		board = currentBoard;
		horizontalVelocity = Thunderbrand.getRandomNumbers().getDoubleInRange(0.05, 0.11) * (facingRight ? 1 : -1);
		verticalVelocity = Thunderbrand.getRandomNumbers().getDoubleInRange(0.08, 0.12);
		hitGroundTime = null;
		recalculateBoundsRect();
	}
	
	/**
	 * @return {@code true} if the LocalDroppedItem has reached end-of-life, {@code false} otherwise
	 */
	public boolean update() {
		if (hitGroundTime == null) { // Item has not yet hit the ground
			Coordinate originalPosition = new Coordinate(position);
			int xLeft = (horizontalVelocity > 0 ? 0 : 1);
			int xRight = (horizontalVelocity > 0 ? 1 : 0);
			int yDown = (verticalVelocity > 0 ? 0 : 1);
			int yUp = (verticalVelocity > 0 ? 1 : 0);
			List<CollisionDetectionTile> nearbyTiles = board.getCollisionDetectionTilesNearPosition(position, xLeft, xRight, yDown, yUp);
			
			// Move horizontally:
			if (horizontalVelocity != 0) {
				Line horizontalMovementLine = new Line(position.getX(), position.getY(), position.getX() + horizontalVelocity, position.getY());
				boolean collisionDetected = false;
				for (CollisionDetectionTile cdt : nearbyTiles) {
					if (cdt.lineIntersectsTile(horizontalMovementLine)) {
						collisionDetected = true;
						break;
					}
				}
				if (collisionDetected) {
					horizontalVelocity = 0; // ->| *whack* v| *thud* .|
				} else {
					position.incrementX(horizontalVelocity);
				}
			}
			
			// Move vertically:
			Line verticalMovementLine = new Line(position.getX(), position.getY(), position.getX(), position.getY() + verticalVelocity);
			boolean collisionDetected = false;
			for (CollisionDetectionTile cdt : nearbyTiles) {
				if (cdt.lineIntersectsTile(verticalMovementLine)) {
					collisionDetected = true;
					break;
				}
			}
			if (!collisionDetected) {
				position.incrementY(verticalVelocity);
				verticalVelocity = Math.max(verticalVelocity + GRAVITY_ACCELERATION, GRAVITY_TERMINAL_VELOCITY);
			} else if (verticalVelocity > 0) { // Hit the ceiling going up; start going down
				verticalVelocity = 0;
			} else { // Hit the floor; stop moving entirely
				hitGroundTime = new Date().getTime();
			}
			
			if (!position.matchesCoordinate(originalPosition)) {
				recalculateBoundsRect();
			}
			return false;
		} else {
			return new Date().getTime() > hitGroundTime + Constants.LOCAL_DROPPED_ITEM_LIFESPAN_MILLIS;
		}
	}
	
	private void recalculateBoundsRect() {
		bounds = new Rect(
				new Coordinate(position.getX() - Constants.DROPPED_ITEM_RADIUS, position.getY()),
				new Coordinate(position.getX() + Constants.DROPPED_ITEM_RADIUS, position.getY() + Constants.DROPPED_ITEM_RADIUS*2));
	}
	
	public void draw() {
		GLDrawer.useTexture(item.getTexture());
		double oscillation = 0;
		if (hitGroundTime != null) {
			long millisSinceHitGround = new Date().getTime() - hitGroundTime;
			double radians = Math.PI * ((double)(millisSinceHitGround) / 1000.0);
			oscillation = Math.sin(radians) * 0.12;
		}
		glPushMatrix();
			glTranslated(0, oscillation, 0);
			GLDrawer.drawQuad(bounds);
		glPopMatrix();
	}
	
	public void drawDebugMode() {
		GLDrawer.disableTextures();
		GLDrawer.setColor(1, 0, 1);
		GLDrawer.drawOutline(bounds);
	}
	
}
