package attack;

import java.io.Serializable;

import geometry.Rect;

public class Attack implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int attackerId;
	private Rect bounds;
	private int framesLeftToLive;
	
	public int getAttackerId() { return attackerId; }
	public Rect getBounds() { return bounds; }
	public int getFramesLeftToLive() { return framesLeftToLive; }
	
	public Attack(int attackerId, Rect bounds, int framesLeftToLive) {
		this.attackerId = attackerId;
		this.bounds = bounds;
		this.framesLeftToLive = framesLeftToLive;
	}
	
}
