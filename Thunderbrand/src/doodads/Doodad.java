package doodads;

import java.io.Serializable;

import geometry.Rect;

public abstract class Doodad implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private final int doodadAction;
	private Rect bounds;
	
	public int getId() { return id; }
	public int getDoodadAction() { return doodadAction; }
	public Rect getBounds() { return bounds; }
	
	public Doodad(int id, int doodadAction, Rect bounds) {
		this.id = id;
		this.doodadAction = doodadAction;
		this.bounds = bounds;
	}
	
	public abstract boolean isActionable();
	public abstract void draw();
	public abstract void drawDebugMode();
	
}
