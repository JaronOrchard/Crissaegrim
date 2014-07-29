package npc;

import items.Item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;
import textures.Textures;
import valmanway.Valmanway;

public class NPCSpikePit extends NPC {
	
	private static final int MAX_HEALTH = 1; // Cannot be hurt
	private static int ATTACK_POWER = 9999;
	
	private Rect bounds;
	private int textureId;
	private final double width;
	
	public NPCSpikePit(SpawnCondition spawnCondition, String boardName, double width) {
		super(MAX_HEALTH, spawnCondition, boardName);
		
		this.width = width;
		textureId = Textures.NPC_SPIKE_PIT_15; // Change upon variable widths
		
		textureHalfWidth = width / 2;
		textureHeight = 1.5;
		
		entityFeetHeight = 0;
		entityBodyHeight = 1.5;
		entityBodyWidth = width;
		
		// TODO: Make a required function that sets all the movement parameters up
	}
	
	@Override public int getCurrentTexture() { return textureId; }
	
	@Override public int getStunnedTexture() { return textureId; }
	
	@Override public boolean isAttackable() { return false; }
	
	@Override public int getAttackPower() { return ATTACK_POWER; }
	
	@Override public void hitByAttack(boolean hitFromRightSide) { /* Cannot be attacked */ }
	
	@Override public boolean getAttacksBounceBack() { return false; }
	
	@Override public String getName() { return "NPC SpikePit"; }
	
	@Override public Color getMainColor() { return new Color(167, 167, 167); }
	
	@Override
	public List<Item> dropItems() {
		return new ArrayList<Item>(); // SpikePit cannot be killed
	}
	
	@Override
	public void respawn() {
		// (SpikePit only spawns once)
		position = getSpawnCondition().getNewRespawnPoint();
		Coordinate bottomLeft = new Coordinate(position);
		Coordinate topRight = new Coordinate(position);
		bottomLeft.incrementX(-width / 2);
		topRight.incrementX(width / 2);
		topRight.incrementY(1.5);
		bounds = new Rect(bottomLeft, topRight);
	}
	
	@Override
	protected void updateNPC() {
		// SpikePit can't be hurt, so right now no Busy applies to it.
		
		// Colliding with a player hurts him/her:
		for (Entry<Integer, EntityStatus> entityStatus : Valmanway.getSharedData().getPlayerStatusMap().get(getCurrentBoardName()).entrySet()) {
			Coordinate esPosition = new Coordinate(entityStatus.getValue().getXPos(), entityStatus.getValue().getYPos());
			Rect playerBoundingRect = RectUtils.getPlayerBoundingRect(esPosition);
			if (RectUtils.rectsOverlap(playerBoundingRect, bounds)) {
				attackPlayer(entityStatus.getKey(), false /* hitFromRightSide is irrelevant */);
			}
		}
	}
	
}
