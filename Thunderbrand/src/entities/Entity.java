package entities;

import java.util.List;
import java.util.Map;

import board.Board;
import busy.Busy;
import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;

/**
 * An Entity is a living object, like a Player or Monster.
 */
public abstract class Entity {
	
	protected int id;
	public int getId() { return id; }
	
	// Default movement values to Player's
	protected double gravityAcceleration = -0.0045;
	protected double tileCollisionPadding = gravityAcceleration / -2;
	protected double gravityTerminalVelocity = -0.400;
	protected double horizontalAirPenaltyFactor = 0.3;
	protected double horizontalAirTaperOffFactor = 0.88;
	protected double horizontalAirTaperOffLimit = 0.01;
	
	private double horizontalMovementSpeed = 0.08;
	private double jumpMomentum = 0.205;
	protected boolean facingRight = true;
	
	protected Coordinate position = new Coordinate(0, 0);
	protected String currentBoardName = null;
	protected Busy busyStatus = null;
	
	public double getGravityAcceleration() { return gravityAcceleration; }
	public double getTileCollisionPadding() { return tileCollisionPadding; }
	public double getGravityTerminalVelocity() { return gravityTerminalVelocity; }
	public double getHorizontalAirPenaltyFactor() { return horizontalAirPenaltyFactor; }
	public double getHorizontalAirTaperOffFactor() { return horizontalAirTaperOffFactor; }
	public double getHorizontalAirTaperOffLimit() { return horizontalAirTaperOffLimit; }
	
	public double getHorizontalMovementSpeed() { return horizontalMovementSpeed; }
	public double getJumpMomentum() { return jumpMomentum; }
	public boolean getFacingRight() { return facingRight; }
	
	public Coordinate getPosition() { return position; }
	public String getCurrentBoardName() { return currentBoardName; }
	public boolean isBusy() { return busyStatus != null; }
	public Busy getBusyStatus() { return busyStatus; }
	
	public void setFacingRight(boolean fr) { facingRight = fr; }
	public void setCurrentBoardName(String boardName) { currentBoardName = boardName; }
	public void setBusy(Busy busy) { busyStatus = busy; }
	
	public Board getCurrentBoard() { return entityMovementHelper.getBoardMap().get(currentBoardName); }
	
	public abstract Rect getEntityBoundingRect(Coordinate position);
	public abstract List<Line> getEntityFeetLines(Coordinate position, boolean includeHorizontalFeetLine);
	
	private final EntityMovementHelper entityMovementHelper;
	public EntityMovementHelper getMovementHelper() { return entityMovementHelper; }
	
	public Entity(Map<String, Board> boardMap) {
		id = -1;
		entityMovementHelper = new EntityMovementHelper(this, boardMap);
	}
	
	public abstract void update();
	public abstract int getCurrentTexture();
	
}
