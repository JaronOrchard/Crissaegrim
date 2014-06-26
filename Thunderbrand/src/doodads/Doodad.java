package doodads;

import java.io.Serializable;

import geometry.Rect;

public abstract class Doodad implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Rect bounds;
	
	public Rect getBounds() { return bounds; }
	
	public Doodad(Rect bounds) {
		this.bounds = bounds;
	}
	
	public abstract void draw();
	public abstract void drawDebugMode();
	
}
