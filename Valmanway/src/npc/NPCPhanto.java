package npc;

import java.util.Map;
import java.util.Map.Entry;

import datapacket.GotHitByAttackPacket;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;
import board.Board;
import textures.Textures;
import valmanway.Valmanway;

public class NPCPhanto extends NPC {
	
	public static long getMillisToRespawn() { return 5000; }
	
	private static final int MAX_HEALTH = 3;
	private static int ATTACK_POWER = 5;
	
	double angle = 0;
	double angleIncrement = 2*Math.PI / 360.0; // 1 degree
	double radius = 7;
	Coordinate currentCenter;
	
	public NPCPhanto(int npc_id, SpawnCondition spawnCondition, String boardName, Map<String, Board> boardMap) {
		super(npc_id, MAX_HEALTH, spawnCondition, boardName, boardMap);
		textureHalfWidth = 1;
		textureHeight = 2;
		
		entityFeetHeight = 0;
		entityBodyHeight = 2;
		entityBodyWidth = 2;
		
		// TODO: Make a required function that sets all the movement parameters up
	}
	
	@Override
	protected void respawn(Coordinate respawnPoint) {
		position = new Coordinate(respawnPoint);
		currentCenter = new Coordinate(respawnPoint);
	}
	
	@Override
	public int getAttackPower() { return ATTACK_POWER; }
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_PHANTO;
	}
	
	@Override
	public int getStunnedTexture() { return Textures.NPC_PHANTO; }
	
	@Override
	protected void updateNPC() {
		if (isBusy()) {
			if (busy.hasExpired()) {
				busy = null;
			} else {
				return; // Phanto is busy!
			}
		}
		
		// Move Phanto:
		angle = (angle + angleIncrement) % (2*Math.PI);
		position = new Coordinate(currentCenter.getX() + (Math.cos(angle) * radius), currentCenter.getY() + (Math.sin(angle) * radius));
		
		// Colliding with a player hurts him/her:
		// This is rather hackish for now but it will be redone when/if there are different Entity sizes.
		// Default Entity values:
		double feetHeight = 0.425;
		double bodyHeight = 2.4;
		double bodyWidth = 0.6;
		for (Entry<Integer, EntityStatus> entityStatus : Valmanway.getSharedData().getEntityStatuses().entrySet()) {
			if (entityStatus.getKey() < 1000000 && entityStatus.getValue().getBoardName().equals(getCurrentBoardName())) {
				Coordinate esPosition = new Coordinate(entityStatus.getValue().getXPos(), entityStatus.getValue().getYPos());
				Rect entityBounds = new Rect(
						new Coordinate(esPosition.getX() - (bodyWidth / 2), esPosition.getY()),
						new Coordinate(esPosition.getX() + (bodyWidth / 2), esPosition.getY() + feetHeight + bodyHeight));
				if (RectUtils.rectsOverlap(entityBounds, this.getEntityEntireRect())) {
					Valmanway.sendPacketToPlayer(entityStatus.getKey(), new GotHitByAttackPacket("NPC Phanto", getAttackPower()));
				}
			}
		}
	}
	
}
