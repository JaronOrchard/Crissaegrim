package players;

import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;
import items.Item;
import items.ItemSolais;
import items.ItemSword;
import items.StackableItem;

import java.util.Map;

import board.Board;
import busy.GotHitByAttackBouncedBackBusy;
import busy.InvincibleBusy;
import busy.PlayerDiedBusy;
import busy.StationaryBusy;
import busy.TimedBusy;
import textures.Textures;
import crissaegrim.Crissaegrim;
import entities.Entity;

public class Player extends Entity {
	
	private static final int MAX_HEALTH = 22;
	
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
		
		double left = position.getX() - 1.5;
		double right = position.getX() + 1.5;
		GLDrawer.useTexture(getCurrentTexture());
		GLDrawer.drawQuad((facingRight ? left : right), (facingRight ? right : left), position.getY(), position.getY() + 3);
		
		if (icon != null) {
			if (icon.equals("F")) {
				GLDrawer.useTexture(Textures.ICON_F);
			} else if (icon.equals("LEFT_CLICK")) {
				GLDrawer.useTexture(Textures.ICON_LEFT_CLICK);
			}
			GLDrawer.drawQuad(position.getX() - 0.5, position.getX() + 0.5, position.getY() + 3, position.getY() + 4);
		}
	}
	
	public void drawDebugMode() {
		GLDrawer.disableTextures();
		GLDrawer.setColor(0, 0, 1);
		GLDrawer.drawOutline(position.getX() - (getBodyWidth() / 2), position.getX() + (getBodyWidth() / 2),
				position.getY(), position.getY() + getFeetHeight());
		GLDrawer.setColor(1, 0, 0);
		GLDrawer.drawLine(position.getX(), position.getY(), position.getX(), position.getY() + getFeetHeight());
		GLDrawer.drawOutline(position.getX() - (getBodyWidth() / 2), position.getX() + (getBodyWidth() / 2),
				position.getY() + getFeetHeight(), position.getY() + getFeetHeight() + getBodyHeight());
		GLDrawer.setColor(0, 1, 0);
		GLDrawer.drawLine(
				position.getX() + ((facingRight ? 1 : -1) * (getBodyWidth() / 2)),
				position.getY() + PLAYER_SWORD_ALTITUDE,
				position.getX() + ((facingRight ? 1 : -1) * ((getBodyWidth() / 2) + 1)),
				position.getY() + PLAYER_SWORD_ALTITUDE);
	}
	
	public void receiveItem(Item item) {
		if (item instanceof ItemSolais) {
			int numSolais = ((ItemSolais)(item)).getNumSolais();
			getInventory().addSolais(numSolais);
			Crissaegrim.addSystemMessage("You received " + numSolais + " Solais.");
		} else {
			if (getInventory().addItem(item)) {
				if (item instanceof StackableItem) {
					Crissaegrim.addSystemMessage("You received " + ((StackableItem)(item)).getNumberInStack() + " " + item.getName() + "s.");
				} else {
					Crissaegrim.addSystemMessage("You received a " + item.getName() + ".");
				}
			} else {
				Crissaegrim.addSystemMessage("You dropped a " + item.getDisplayName() + " because your inventory was full!");
			}
		}
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
		if (isBusy()) {
			if (busy instanceof TimedBusy && ((TimedBusy)(busy)).hasExpired()) {
				if (busy instanceof PlayerDiedBusy) {
					getHealthBar().healDamage(getHealthBar().getMaxHealth());
					Crissaegrim.getGameRunner().setNewDestinationToSpawn();
					Crissaegrim.getGameRunner().requestTravelToDestinationBoard();
					busy = new InvincibleBusy(2000);
				} else {
					busy = null;
				}
			} else if (busy instanceof StationaryBusy && ((StationaryBusy)(busy)).hasMoved(getPosition())) {
				busy = null;
			} else if (busy instanceof GotHitByAttackBouncedBackBusy && !getMovementHelper().isCurrentlyBouncingBackFromAttack()) {
				busy = null;
			}
		}
		icon = null;
	}
	
	private static double PLAYER_SWORD_ALTITUDE = 1.74;
	public Rect getSwordSwingRect(ItemSword sword) {
		return sword.getSwordSwingRect(
				new Coordinate(position.getX() + (getBodyWidth() / 2 * (facingRight ? 1 : -1)), position.getY() + PLAYER_SWORD_ALTITUDE),
				facingRight);
	}
	
}
