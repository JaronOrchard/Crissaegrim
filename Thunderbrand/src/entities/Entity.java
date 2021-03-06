package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vitals.HealthBar;
import board.Board;
import busy.Busy;
import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;
import gldrawer.GLDrawer;

/**
 * An Entity is a living object, like a Player or Monster.
 */
public abstract class Entity {
	
	protected int id;
	public int getId() { return id; }
	
	private final HealthBar healthBar;
	public HealthBar getHealthBar() { return healthBar; }
	
	// Default values to Player's:
	protected double entityFeetHeight = 0.425;	// NOTE: If these values change,
	protected double entityBodyHeight = 2.4;	//       be sure to change
	protected double entityBodyWidth = 0.6;		//       RectUtils.getPlayerBoundingRect(), too.
	protected double textureHalfWidth = 1.5;
	protected double textureHeight = 3;
	
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
	protected Busy busy = null;
	
	public double getFeetHeight() { return entityFeetHeight; }
	public double getBodyHeight() { return entityBodyHeight; }
	public double getEntireHeight() { return entityBodyHeight + entityFeetHeight; }
	public double getBodyWidth() { return entityBodyWidth; }
	public double getBodyRadius() { return entityBodyWidth / 2; }
	public double getTextureHalfWidth() { return textureHalfWidth; }
	public double getTextureHeight() { return textureHeight; }
	
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
	public boolean isBusy() { return busy != null; }
	public Busy getBusy() { return busy; }
	
	public void setFacingRight(boolean fr) { facingRight = fr; }
	public void setCurrentBoardName(String boardName) { currentBoardName = boardName; }
	public void setBusy(Busy busyStatus) { busy = busyStatus; }
	
	public Board getCurrentBoard() { return entityMovementHelper.getBoardMap().get(currentBoardName); }
	
	public Rect getEntityBodyRect() { return getEntityBodyRect(position); }
	public Rect getEntityBodyRect(Coordinate pos) {
		return new Rect(
				new Coordinate(pos.getX() - (getBodyWidth() / 2), pos.getY() + getFeetHeight()),
				new Coordinate(pos.getX() + (getBodyWidth() / 2), pos.getY() + getFeetHeight() + getBodyHeight()));
	}
	
	public Rect getEntityEntireRect() { return getEntityEntireRect(position); }
	public Rect getEntityEntireRect(Coordinate pos) {
		return new Rect(
				new Coordinate(pos.getX() - (getBodyWidth() / 2), pos.getY()),
				new Coordinate(pos.getX() + (getBodyWidth() / 2), pos.getY() + getFeetHeight() + getBodyHeight()));
	}
	
	public List<Line> getEntityFeetLines(Coordinate pos, boolean includeHorizontalFeetLine) {
		List<Line> feetLines = new ArrayList<Line>();
		feetLines.add(new Line(
				new Coordinate(pos.getX(), pos.getY()),
				new Coordinate(pos.getX(), pos.getY() + getFeetHeight())));
		if (includeHorizontalFeetLine) {
			feetLines.add(new Line(
					new Coordinate(pos.getX() - getBodyRadius(), pos.getY()),
					new Coordinate(pos.getX() + getBodyRadius(), pos.getY())));
			feetLines.add(new Line(
					new Coordinate(pos.getX() - getBodyRadius(), pos.getY()),
					new Coordinate(pos.getX() - getBodyRadius(), pos.getY() + getFeetHeight())));
			feetLines.add(new Line(
					new Coordinate(pos.getX() + getBodyRadius(), pos.getY()),
					new Coordinate(pos.getX() + getBodyRadius(), pos.getY() + getFeetHeight())));
		}
		return feetLines;
	}
	
	private final EntityMovementHelper entityMovementHelper;
	public EntityMovementHelper getMovementHelper() { return entityMovementHelper; }
	
	public Entity(int maxHealth, Map<String, Board> boardMap) {
		id = -1;
		entityMovementHelper = new EntityMovementHelper(this, boardMap);
		healthBar = new HealthBar(maxHealth);
	}
	
	public static void drawMiniHealthBar(double xPos, double yPosPlusTextureHeight, double amtHealth) {
		if (amtHealth < 1.0) {
			double healthBarMiddle = ((1.0 - amtHealth) * (xPos - 0.5)) + (amtHealth * (xPos + 0.5));
			double healthBarBottom = yPosPlusTextureHeight + 0.15;
			double healthBarTop = yPosPlusTextureHeight + 0.35;
			GLDrawer.disableTextures();
			GLDrawer.setColor(0.855, 0.188, 0.204);
			GLDrawer.drawQuad(xPos - 0.5, xPos + 0.5, healthBarBottom, healthBarTop); // Draw red backing
			GLDrawer.setColor(0.161, 0.714, 0.314);
			GLDrawer.drawQuad(xPos - 0.5, healthBarMiddle, healthBarBottom, healthBarTop); // Draw green remaining
		}
	}
	
	public abstract void update();
	public abstract int getCurrentTexture();
	public abstract int getStunnedTexture();
	
}
