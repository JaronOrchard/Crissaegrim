package items;

import board.Board;
import thunderbrand.Constants;
import thunderbrand.Thunderbrand;
import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;

public class LocalDroppedItem {
	
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
		horizontalVelocity = Thunderbrand.getRandomNumbers().getDoubleInRange(0.7, 0.10) * (facingRight ? 1 : -1);
		verticalVelocity = Thunderbrand.getRandomNumbers().getDoubleInRange(0.09, 0.11);
		hitGroundTime = null;
		recalculateBoundsRect();
	}
	
	public void update() {
		Coordinate currentPosition = new Coordinate(position);
		
		
		// If the item moves, recalculate bounds
	}
	
	private void recalculateBoundsRect() {
		bounds = new Rect(
				new Coordinate(position.getX() - Constants.DROPPED_ITEM_RADIUS, position.getY()),
				new Coordinate(position.getX() + Constants.DROPPED_ITEM_RADIUS, position.getY() + Constants.DROPPED_ITEM_RADIUS*2));
	}
	
	public void draw() {
		GLDrawer.useTexture(item.getTexture());
		GLDrawer.drawQuad(bounds);
	}
	
	public void drawDebugMode() {
		GLDrawer.disableTextures();
		GLDrawer.setColor(1, 0, 1);
		GLDrawer.drawOutline(bounds);
	}
	
}
