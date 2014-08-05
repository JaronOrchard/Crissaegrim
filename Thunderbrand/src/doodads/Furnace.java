package doodads;

import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;
import textures.Textures;

public class Furnace extends Doodad {
	private static final long serialVersionUID = 1L;
	
	private static double FURNACE_RADIUS = 2;
	private static double FURNACE_HEIGHT = 4;
	
	public Furnace(int id, Coordinate bottomCenter) {
		super(id, DoodadActions.SMELT_ORE, new Rect(
				new Coordinate(bottomCenter.getX() - FURNACE_RADIUS, bottomCenter.getY()),
				new Coordinate(bottomCenter.getX() + FURNACE_RADIUS, bottomCenter.getY() + FURNACE_HEIGHT)));
	}
	
	@Override
	public boolean isActionable() { return true; }
	
	@Override
	public String getActionIcon() { return "F"; }
	
	@Override
	public void draw() {
		GLDrawer.useTexture(Textures.FURNACE);
		GLDrawer.drawQuad(getBounds());
	}
	
	@Override
	public void drawDebugMode() {
		GLDrawer.disableTextures();
		GLDrawer.setColor(0, 1, 1);
		GLDrawer.drawOutline(getBounds());
	}
	
}
