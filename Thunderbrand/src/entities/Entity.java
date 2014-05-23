package entities;

import java.util.ArrayList;
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
	
	// Default values to Player's:
	protected double entityFeetHeight = 0.425;
	protected double entityBodyHeight = 2.4;
	protected double entityBodyWidth = 0.6;
	
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
	
	public double getFeetHeight() { return entityFeetHeight; }
	public double getBodyHeight() { return entityBodyHeight; }
	public double getEntireHeight() { return entityBodyHeight + entityFeetHeight; }
	public double getBodyWidth() { return entityBodyWidth; }
	public double getBodyRadius() { return entityBodyWidth / 2; }
	
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
	
	public Rect getEntityBoundingRect(Coordinate position) {
		return new Rect(
				new Coordinate(position.getX() - (getBodyWidth() / 2), position.getY() + getFeetHeight()),
				new Coordinate(position.getX() + (getBodyWidth() / 2), position.getY() + getFeetHeight() + getBodyHeight()));
	}
	
	public List<Line> getEntityFeetLines(Coordinate position, boolean includeHorizontalFeetLine) {
		List<Line> feetLines = new ArrayList<Line>();
		feetLines.add(new Line(
				new Coordinate(position.getX(), position.getY()),
				new Coordinate(position.getX(), position.getY() + getFeetHeight())));
		if (includeHorizontalFeetLine) {
			feetLines.add(new Line(
					new Coordinate(position.getX() - getBodyRadius(), position.getY()),
					new Coordinate(position.getX() + getBodyRadius(), position.getY())));
			feetLines.add(new Line(
					new Coordinate(position.getX() - getBodyRadius(), position.getY()),
					new Coordinate(position.getX() - getBodyRadius(), position.getY() + getFeetHeight())));
			feetLines.add(new Line(
					new Coordinate(position.getX() + getBodyRadius(), position.getY()),
					new Coordinate(position.getX() + getBodyRadius(), position.getY() + getFeetHeight())));
		}
		return feetLines;
	}
	
	private final EntityMovementHelper entityMovementHelper;
	public EntityMovementHelper getMovementHelper() { return entityMovementHelper; }
	
	public Entity(Map<String, Board> boardMap) {
		id = -1;
		entityMovementHelper = new EntityMovementHelper(this, boardMap);
	}
	
	public abstract void update();
	public abstract int getCurrentTexture();
	
	// --- TEMPORARY CODE/METHOD to test Attacks; bounding rect will be built into the weapon later
	private static double PLAYER_SWORD_ALTITUDE = 1.74;
	private static double PLAYER_SWORD_HEIGHT = 0.15;
	private static double PLAYER_SWORD_LENGTH = 1.0;
	
	public static double getPlayerSwordAltitude() { return PLAYER_SWORD_ALTITUDE; }
	public static double getPlayerSwordHeight() { return PLAYER_SWORD_HEIGHT; }
	public static double getPlayerSwordLength() { return PLAYER_SWORD_LENGTH; }
	
	public Rect getSwordSwingRect() {
		if (facingRight) {
			return new Rect(
					new Coordinate(position.getX() + getBodyWidth() / 2, position.getY() + getPlayerSwordAltitude()),
					new Coordinate(position.getX() + getBodyWidth() / 2 + getPlayerSwordLength(), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight()));
		} else {
			return new Rect(
					new Coordinate(position.getX() - getBodyWidth() / 2 - getPlayerSwordLength(), position.getY() + getPlayerSwordAltitude()),
					new Coordinate(position.getX() - getBodyWidth() / 2, position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight()));
		}
	}
	
}
