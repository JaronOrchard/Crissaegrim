package players;

import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import board.Board;
import textures.Textures;
import crissaegrim.Crissaegrim;
import entities.Entity;

public class Player extends Entity {
	
	private static final int MAX_HEALTH = 100;
	
	private String icon = null; // Appears when something is actionable, like entering a door
	public void setIcon(String newIcon) { icon = newIcon; }
	
	private String name;
	public String getName() { return name; }
	public void setName(String n) { name = n; }
	
	private Inventory inventory = new Inventory();
	public Inventory getInventory() { return inventory; }
	
	public Player(Map<String, Board> boardMap) {
		super(MAX_HEALTH, boardMap);
		name = "UNASSIGNED";
		
		gravityAcceleration = -0.0045;
		tileCollisionPadding = gravityAcceleration / -2;
		gravityTerminalVelocity = -0.400;
		horizontalAirPenaltyFactor = 0.3;
		horizontalAirTaperOffFactor = 0.88;
		horizontalAirTaperOffLimit = 0.01;
	}
	
	public void assignPlayerId(int playerId) {
		id = playerId;
		name = "Player " + id;
		Crissaegrim.addSystemMessageIfDebug("Assigned player ID: " + id);
	}
	
	public void draw() {
		
		drawMiniHealthBar(position.getX(), position.getY() + textureHeight, getHealthBar().getAmtHealth());
		
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
			glVertex2d(position.getX() - (getBodyWidth() / 2), position.getY() + getFeetHeight());
			glVertex2d(position.getX() - (getBodyWidth() / 2), position.getY());
			glVertex2d(position.getX() + (getBodyWidth() / 2), position.getY());
			glVertex2d(position.getX() + (getBodyWidth() / 2), position.getY() + getFeetHeight());
		glEnd();
		glColor3d(1, 0, 0);
		glBegin(GL_LINES);	
			glVertex2d(position.getX(), position.getY());
			glVertex2d(position.getX(), position.getY() + getFeetHeight());
		glEnd();
		glBegin(GL_LINE_LOOP);
			glVertex2d(position.getX() - (getBodyWidth() / 2), position.getY() + getFeetHeight());
			glVertex2d(position.getX() + (getBodyWidth() / 2), position.getY() + getFeetHeight());
			glVertex2d(position.getX() + (getBodyWidth() / 2), position.getY() + getFeetHeight() + getBodyHeight());
			glVertex2d(position.getX() - (getBodyWidth() / 2), position.getY() + getFeetHeight() + getBodyHeight());
		glEnd();
		glColor3d(0, 1, 0);
		glBegin(GL_LINE_STRIP);	
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * (getBodyWidth() / 2)), position.getY() + getPlayerSwordAltitude());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getBodyWidth() / 2) + getPlayerSwordLength())), position.getY() + getPlayerSwordAltitude());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getBodyWidth() / 2) + getPlayerSwordLength())), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight());
			glVertex2d(position.getX() + ((facingRight ? 1 : -1) * ((getBodyWidth() / 2))), position.getY() + getPlayerSwordAltitude() + getPlayerSwordHeight());
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) {
			return busy.getTexture();
		} else if (Crissaegrim.getChatBox().isTypingMode()) {
			return Textures.STICK_PLAYER_TYPING;
		}
		return Textures.STICK_PLAYER;
	}
	
	@Override
	public int getStunnedTexture() { return Textures.STICK_PLAYER_STUNNED; }
	
	@Override
	public void update() {
		if (isBusy() && busy.hasExpired()) {
			busy = null;
		}
		icon = null;
	}
	
}
