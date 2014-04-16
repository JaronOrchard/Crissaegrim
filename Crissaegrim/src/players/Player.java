package players;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;
import busy.Busy;
import textures.Textures;
import crissaegrim.Crissaegrim;
import entities.Entity;

public class Player extends Entity {
	private static double PLAYER_FEET_HEIGHT = 0.425;
	private static double PLAYER_BODY_HEIGHT = 2.4;
	private static double PLAYER_BODY_WIDTH = 0.6;
	private static double PLAYER_SWORD_ALTITUDE = 1.74;
	private static double PLAYER_SWORD_HEIGHT = 0.15;
	private static double PLAYER_SWORD_LENGTH = 1.0;
	
	public static double getPlayerFeetHeight() { return PLAYER_FEET_HEIGHT; }
	public static double getPlayerBodyHeight() { return PLAYER_BODY_HEIGHT; }
	public static double getPlayerBodyWidth() { return PLAYER_BODY_WIDTH; }
	public static double getPlayerSwordAltitude() { return PLAYER_SWORD_ALTITUDE; }
	public static double getPlayerSwordHeight() { return PLAYER_SWORD_HEIGHT; }
	public static double getPlayerSwordLength() { return PLAYER_SWORD_LENGTH; }
	
	private double horizontalMovementSpeed = 0.08;
	private double jumpMomentum = 0.205;
	private Coordinate position;
	private boolean facingRight = true;
	private Busy busyStatus = null;
	private String icon = null; // Appears when something is actionable, like entering a door
	
	public double getHorizontalMovementSpeed() { return horizontalMovementSpeed; }
	public double getJumpMomentum() { return jumpMomentum; }
	public Coordinate getPosition() { return position; }
	public boolean getFacingRight() { return facingRight; }
	public boolean isBusy() { return busyStatus != null; }
	public Busy getBusyStatus() { return busyStatus; }
	
	public void setFacingRight(boolean fr) { facingRight = fr; }
	public void setBusy(Busy busy) { busyStatus = busy; }
	public void setIcon(String newIcon) { icon = newIcon; }
	
	private int playerId;
	public int getId() { return playerId; }
	
	private String name;
	public String getName() { return name; }
	public void setName(String n) { name = n; }
	
	private Inventory inventory = new Inventory();
	public Inventory getInventory() { return inventory; }
	
	public Player() {
		super();
		playerId = -1;
		name = "UNASSIGNED";
		position = new Coordinate(0, 0);
		
		gravityAcceleration = -0.0045;
		tileCollisionPadding = gravityAcceleration / -2;
		gravityTerminalVelocity = -0.400;
		horizontalAirPenaltyFactor = 0.3;
		horizontalAirTaperOffFactor = 0.88;
		horizontalAirTaperOffLimit = 0.01;
	}
	
	// === Entity functions:
	
	@Override
	public Rect getEntityBoundingRect(Coordinate position) {
		return new Rect(
				new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight()),
				new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight() + Player.getPlayerBodyHeight()));
	}
	
	@Override
	public List<Line> getEntityFeetLines(Coordinate position, boolean includeHorizontalFeetLine) {
		List<Line> feetLines = new ArrayList<Line>();
		feetLines.add(new Line(
				new Coordinate(position.getX(), position.getY()),
				new Coordinate(position.getX(), position.getY() + Player.getPlayerFeetHeight())));
		if (includeHorizontalFeetLine) {
			feetLines.add(new Line(
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY())));
			feetLines.add(new Line(
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight())));
			feetLines.add(new Line(
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight())));
		}
		return feetLines;
	}
	
	// ===
	
	public void assignPlayerId(int id) {
		playerId = id;
		name = "Player " + id;
		Crissaegrim.addSystemMessageIfDebug("Assigned player ID: " + id);
	}
	
	/**
	 * Update the player's various statuses.
	 */
	public void update() {
		if (isBusy() && busyStatus.hasExpired()) {
			busyStatus = null;
		}
		icon = null;
	}
	
	public Rect getSwordSwingRect() {
		if (facingRight) {
			return new Rect(
					new Coordinate(position.getX() + getPlayerBodyWidth() / 2, position.getY() + getPlayerSwordAltitude()),
					new Coordinate(position.getX() + getPlayerBodyWidth() / 2 + getPlayerSwordLength(), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight()));
		} else {
			return new Rect(
					new Coordinate(position.getX() - getPlayerBodyWidth() / 2 - getPlayerSwordLength(), position.getY() + getPlayerSwordAltitude()),
					new Coordinate(position.getX() - getPlayerBodyWidth() / 2, position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight()));
		}
	}
	
	public void draw() {
		glColor3d(1.0, 1.0, 1.0);
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D, getCurrentTexture());
			glBegin(GL_QUADS);
				glTexCoord2d(facingRight ? 0 : 1, 0);
				glVertex2d(position.getX() - 1.5, position.getY() + 3);
				glTexCoord2d(facingRight ? 1 : 0, 0);
				glVertex2d(position.getX() + 1.5, position.getY() + 3);
				glTexCoord2d(facingRight ? 1 : 0, 1);
				glVertex2d(position.getX() + 1.5, position.getY());
				glTexCoord2d(facingRight ? 0 : 1, 1);
				glVertex2d(position.getX() - 1.5, position.getY());
			glEnd();
			
			if (icon != null) {
				if (icon.equals("X")) glBindTexture(GL_TEXTURE_2D, Textures.ICON_X); 
				glBegin(GL_QUADS);
					glTexCoord2d(0, 0);
					glVertex2d(position.getX() - 0.5, position.getY() + 4);
					glTexCoord2d(1, 0);
					glVertex2d(position.getX() + 0.5, position.getY() + 4);
					glTexCoord2d(1, 1);
					glVertex2d(position.getX() + 0.5, position.getY() + 3);
					glTexCoord2d(0, 1);
					glVertex2d(position.getX() - 0.5, position.getY() + 3);
				glEnd();
			}
		glPopMatrix();
	}
	
	public void drawDebugMode() {
		glDisable(GL_TEXTURE_2D);
		glColor3d(0, 0, 1);
		glBegin(GL_LINE_STRIP);
			glVertex2d(position.getX() - (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight());
			glVertex2d(position.getX() - (getPlayerBodyWidth() / 2), position.getY());
			glVertex2d(position.getX() + (getPlayerBodyWidth() / 2), position.getY());
			glVertex2d(position.getX() + (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight());
		glEnd();
		glColor3d(1, 0, 0);
		glBegin(GL_LINES);	
			glVertex2d(position.getX(), position.getY());
			glVertex2d(position.getX(), position.getY() + getPlayerFeetHeight());
		glEnd();
		glBegin(GL_LINE_LOOP);
			glVertex2d(position.getX() - (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight());
			glVertex2d(position.getX() + (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight());
			glVertex2d(position.getX() + (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight() + getPlayerBodyHeight());
			glVertex2d(position.getX() - (getPlayerBodyWidth() / 2), position.getY() + getPlayerFeetHeight() + getPlayerBodyHeight());
		glEnd();
		glColor3d(0, 1, 0);
		glBegin(GL_LINE_STRIP);	
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * (getPlayerBodyWidth() / 2)), position.getY() + getPlayerSwordAltitude());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getPlayerBodyWidth() / 2) + getPlayerSwordLength())), position.getY() + getPlayerSwordAltitude());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getPlayerBodyWidth() / 2) + getPlayerSwordLength())), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getPlayerBodyWidth() / 2))), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight());
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public int getCurrentTexture() {
		if (isBusy()) {
			return busyStatus.getTexture();
		} else if (Crissaegrim.getChatBox().isTypingMode()) {
			return Textures.STICK_PLAYER_TYPING;
		}
		return Textures.STICK_PLAYER;
	}
	
}
