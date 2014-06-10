package npc;

import items.Item;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Line;
import geometry.RectUtils;
import textures.Textures;
import valmanway.Valmanway;

public class NPCChargingSpike extends NPC {
	
	private static final int MAX_HEALTH = 1; // Cannot be hurt
	private static int ATTACK_POWER = 4;
	protected enum ChargingStatus { IDLE, CHARGING, RETREATING }
	
	private static final double CHARGING_SPEED = 0.12;
	
	private Coordinate chargeStartPoint;
	private Coordinate chargeEndPoint;
	private Line chargeCheckLine;
	private ChargingStatus chargingStatus;
	
	public NPCChargingSpike(SpawnCondition spawnCondition, Coordinate chargeTo, boolean chargeRight, String boardName) {
		super(MAX_HEALTH, spawnCondition, boardName);
		chargeEndPoint = new Coordinate(chargeTo);
		facingRight = chargeRight; // True if rests at left and charges right, false if reversed
		
		textureHalfWidth = 1;
		textureHeight = 2;
		
		entityFeetHeight = 0;
		entityBodyHeight = 1.78125;
		entityBodyWidth = 2;
		
		// TODO: Make a required function that sets all the movement parameters up
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_CHARGING_SPIKE;
	}
	
	@Override public int getStunnedTexture() { return Textures.NPC_CHARGING_SPIKE; }
	
	@Override public boolean isAttackable() { return false; }
	
	@Override public int getAttackPower() { return ATTACK_POWER; }
	
	@Override protected String getName() { return "NPC ChargingSpike"; }
	
	@Override public Color getMainColor() { return new Color(140, 109, 38); }
	
	@Override
	public List<Item> dropItems() {
		return new ArrayList<Item>(); // ChargingSpike cannot be killed
	}
	
	@Override
	public void respawn() {
		// (ChargingSpike only spawns once)
		Coordinate respawnPoint = getSpawnCondition().getNewRespawnPoint();
		position = new Coordinate(respawnPoint);
		chargeStartPoint = new Coordinate(position);
		chargeCheckLine = new Line(
				new Coordinate(chargeStartPoint.getX(), chargeStartPoint.getY() + 0.1),
				new Coordinate(chargeEndPoint.getX() + (facingRight ? 1 : -1), chargeEndPoint.getY() + 0.1));
		chargingStatus = ChargingStatus.IDLE;
	}
	
	@Override
	protected void updateNPC() {
		// ChargingSpike can't be hurt, so right now no Busy applies to it.
		
		if (chargingStatus == ChargingStatus.IDLE) {
			// Check if any Players lie on ChargeLine.  If so, start charging!
			for (Entry<Integer, EntityStatus> entityStatus : Valmanway.getSharedData().getEntityStatuses().entrySet()) {
				if (entityStatus.getKey() < 1000000 && entityStatus.getValue().getBoardName().equals(getCurrentBoardName())) {
					Coordinate esPosition = new Coordinate(entityStatus.getValue().getXPos(), entityStatus.getValue().getYPos());
					if (RectUtils.lineIntersectsRect(chargeCheckLine, RectUtils.getPlayerBoundingRect(esPosition))) {
						chargingStatus = ChargingStatus.CHARGING;
						break;
					}
				}
			}
			
		} else if (chargingStatus == ChargingStatus.CHARGING) {
			// Keep charging until reaching chargeEndPoint
			if (facingRight) {
				position.incrementX(CHARGING_SPEED);
				if (position.getX() >= chargeEndPoint.getX()) {
					position.setX(chargeEndPoint.getX());
					chargingStatus = ChargingStatus.RETREATING;
				}
			} else {
				position.incrementX(-CHARGING_SPEED);
				if (position.getX() <= chargeEndPoint.getX()) {
					position.setX(chargeEndPoint.getX());
					chargingStatus = ChargingStatus.RETREATING;
				}
			}
		} else /*if (chargingStatus == ChargingStatus.RETREATING)*/ {
			// Keep retreating at half speed until reaching chargeStartPoint
			if (facingRight) {
				position.incrementX(-CHARGING_SPEED / 2);
				if (position.getX() <= chargeStartPoint.getX()) {
					position.setX(chargeStartPoint.getX());
					chargingStatus = ChargingStatus.IDLE;
				}
			} else {
				position.incrementX(CHARGING_SPEED / 2);
				if (position.getX() >= chargeStartPoint.getX()) {
					position.setX(chargeStartPoint.getX());
					chargingStatus = ChargingStatus.IDLE;
				}
			}
		}
		
		// Colliding with a player hurts him/her:
		for (Entry<Integer, EntityStatus> entityStatus : Valmanway.getSharedData().getEntityStatuses().entrySet()) {
			if (entityStatus.getKey() < 1000000 && entityStatus.getValue().getBoardName().equals(getCurrentBoardName())) {
				Coordinate esPosition = new Coordinate(entityStatus.getValue().getXPos(), entityStatus.getValue().getYPos());
				if (RectUtils.rectsOverlap(RectUtils.getPlayerBoundingRect(esPosition), this.getEntityEntireRect())) {
					attackPlayer(entityStatus.getKey());
				}
			}
		}
	}
	
}
