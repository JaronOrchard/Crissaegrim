package attack;

import java.io.Serializable;

import geometry.Rect;

public class Attack implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int attackerId;
	private final String boardName;
	private Rect bounds;
	private int attackPower;
	private int framesLeftToLive;
	
	public int getAttackerId() { return attackerId; }
	public String getBoardName() { return boardName; }
	public Rect getBounds() { return bounds; }
	public int getAttackPower() { return attackPower; }
	public int getFramesLeftToLive() { return framesLeftToLive; }
	
	public Attack(int attackerId, String boardName, Rect bounds, int attackPower, int framesLeftToLive) {
		this.attackerId = attackerId;
		this.boardName = boardName;
		this.bounds = bounds;
		this.attackPower = attackPower;
		this.framesLeftToLive = framesLeftToLive;
	}
	
}
