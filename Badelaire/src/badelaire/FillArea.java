package badelaire;

import gldrawer.GLDrawer;

public class FillArea {
	private int mode = 0; // 0 = Idle, 1 = First tile chosen, 2 = Both tiles chosen
	private int point1TileX;
	private int point1TileY;
	private int point2TileX;
	private int point2TileY;
	
	public int getPoint1TileX() { return point1TileX; }
	public int getPoint1TileY() { return point1TileY; }
	public int getPoint2TileX() { return point2TileX; }
	public int getPoint2TileY() { return point2TileY; }
	
	public boolean isIdle() { return mode == 0; }
	public boolean readyToFill() { return mode == 2; }
	
	public FillArea() {
		mode = 0;
		point1TileX = 0;
		point1TileY = 0;
		point2TileX = 0;
		point2TileY = 0;
	}
	
	public void nextState(int tileX, int tileY) {
		mode = (mode + 1) % 3;
		if (mode == 1) {
			point1TileX = tileX;
			point1TileY = tileY;
		} else if (mode == 2) {
			point2TileX = tileX;
			point2TileY = tileY;
		}
	}
	
	public void draw(int tileX, int tileY) {
		if (mode == 0) { return; }
		if (mode == 2) {
			tileX = point2TileX;
			tileY = point2TileY;
		}
		double leftmostTile = point1TileX < tileX ? point1TileX : tileX;
		double rightmostTile = point1TileX < tileX ? tileX+1 : point1TileX+1;
		double topmostTile = point1TileY < tileY ? tileY+1 : point1TileY+1;
		double bottommostTile = point1TileY < tileY ? point1TileY : tileY;
		
		GLDrawer.disableTextures();
		GLDrawer.setLineWidth(3);
		GLDrawer.setColor(0.8, 0, 0);
		GLDrawer.drawOutline(leftmostTile, rightmostTile, bottommostTile, topmostTile);
		GLDrawer.setLineWidth(1);
	}
	
}
