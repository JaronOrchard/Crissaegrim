package doodads;

import geometry.Rect;

public abstract class Doodad {
	
	protected Rect bounds;
	
	public Rect getBounds() { return bounds; }
	
	public Doodad(Rect bounds) {
		this.bounds = bounds;
	}
	
	public abstract void draw();
	public abstract void drawDebugMode();
	
}
