package doodads;

import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;

import textures.Textures;

public class Door extends Doodad {
	private static final long serialVersionUID = 1L;
	
	private static double DOOR_RADIUS = 0.5;
	private static double DOOR_HEIGHT = 3;
	
	private final String destinationBoardName;
	private final Coordinate destinationCoordinate;
	
	public String getDestinationBoardName() { return destinationBoardName; }
	public Coordinate getDestinationCoordinate() { return destinationCoordinate; }
	
	public Door(int id, Coordinate bottomCenter, String destBoardName, Coordinate destCoord) {
		super(id, DoodadActions.GO_THROUGH_DOORWAY, new Rect(
				new Coordinate(bottomCenter.getX() - DOOR_RADIUS, bottomCenter.getY()),
				new Coordinate(bottomCenter.getX() + DOOR_RADIUS, bottomCenter.getY() + DOOR_HEIGHT)));
		destinationBoardName = destBoardName;
		destinationCoordinate = destCoord;
	}
	
	@Override
	public boolean isActionable() { return true; }
	
	@Override
	public void draw() {
		Rect bounds = getBounds();
		GLDrawer.useTexture(Textures.DOOR_CLOSED);
		GLDrawer.drawQuad(bounds.getLeft() - 1, bounds.getRight() + 1, bounds.getBottom(), bounds.getTop() + 1);
	}

	@Override
	public void drawDebugMode() {
		Rect bounds = getBounds();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0, 1, 1);
		GLDrawer.drawOutline(bounds);
	}
	
}
