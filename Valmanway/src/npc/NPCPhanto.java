package npc;

import items.Item;
import items.ItemPartyPopper;
import items.ItemSolais;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import entities.EntityStatus;
import geometry.Coordinate;
import geometry.RectUtils;
import textures.Textures;
import thunderbrand.Thunderbrand;
import valmanway.Valmanway;

public class NPCPhanto extends NPC {
	
	public static long getMillisToRespawn() { return 5000; }
	
	private static final int MAX_HEALTH = 3;
	private static int ATTACK_POWER = 4;
	
	double angle = 0;
	double angleIncrement = 2*Math.PI / 360.0 / 2.0; // 0.5 degrees/frame
	double radius = 7;
	Coordinate currentCenter;
	
	public NPCPhanto(SpawnCondition spawnCondition, String boardName) {
		super(MAX_HEALTH, spawnCondition, boardName);
		textureHalfWidth = 1;
		textureHeight = 2;
		
		entityFeetHeight = 0;
		entityBodyHeight = 2;
		entityBodyWidth = 2;
		
		// TODO: Make a required function that sets all the movement parameters up
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_PHANTO;
	}
	
	@Override public int getStunnedTexture() { return Textures.NPC_PHANTO; }
	
	@Override public boolean isAttackable() { return true; }
	
	@Override public int getAttackPower() { return ATTACK_POWER; }
	
	@Override protected String getName() { return "NPC Phanto"; }
	
	@Override public Color getMainColor() { return new Color(182, 57, 0); }
	
	@Override
	public List<Item> dropItems() {
		List<Item> items = new ArrayList<Item>();
		
		// Solais return: 50% = 3, 30% = 4, 15% = 6, 5% = 10
		double randSolais = Thunderbrand.getRandomNumbers().getDouble();
		if (randSolais < 0.5) items.add(new ItemSolais(3));
		else if (randSolais < 0.8) items.add(new ItemSolais(4));
		else if (randSolais < 0.95) items.add(new ItemSolais(6));
		else items.add(new ItemSolais(10));
		
		// Item return (party poppers): 30% = Blue, 20% = Green, 12.5% = Red, 7.5% = Yellow, 5% = White
		double randItem = Thunderbrand.getRandomNumbers().getDouble();
		if (randItem < 0.3) items.add(new ItemPartyPopper("Blue", Textures.ITEM_PARTY_POPPER_BLUE));
		else if (randItem < 0.5) items.add(new ItemPartyPopper("Green", Textures.ITEM_PARTY_POPPER_GREEN));
		else if (randItem < 0.625) items.add(new ItemPartyPopper("Red", Textures.ITEM_PARTY_POPPER_RED));
		else if (randItem < 0.7) items.add(new ItemPartyPopper("Yellow", Textures.ITEM_PARTY_POPPER_YELLOW));
		else if (randItem < 0.75) items.add(new ItemPartyPopper("White", Textures.ITEM_PARTY_POPPER_WHITE));
		
		return items;
	}
	
	@Override
	public void respawn() {
		Coordinate respawnPoint = getSpawnCondition().getNewRespawnPoint();
		position = new Coordinate(respawnPoint);
		currentCenter = new Coordinate(respawnPoint);
	}
	
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
