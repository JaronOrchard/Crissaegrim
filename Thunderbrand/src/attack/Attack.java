package attack;

import geometry.Rect;

public class Attack {
	
	private int attackerId;
	private Rect bounds;
	private boolean oneFrameLifetime;
	
	public int getAttackerId() { return attackerId; }
	public Rect getBounds() { return bounds; }
	public boolean getOneFrameLifetime() { return oneFrameLifetime; }
	
	public Attack(int attackerId, Rect bounds, boolean oneFrameLifetime) {
		this.attackerId = attackerId;
		this.bounds = bounds;
		this.oneFrameLifetime = oneFrameLifetime;
	}
	
}
