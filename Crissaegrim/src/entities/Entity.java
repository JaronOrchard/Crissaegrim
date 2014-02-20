package entities;

import crissaegrim.Rect;

public abstract class Entity {
	
	protected Rect bounds;
	
	public Rect getBounds() { return bounds; }
	
	public Entity(Rect bounds) {
		this.bounds = bounds;
	}
	
	public abstract void draw();
	public abstract void drawDebugMode();
	
}
