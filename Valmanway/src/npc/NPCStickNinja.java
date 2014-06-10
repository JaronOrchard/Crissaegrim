package npc;

import items.Item;
import items.ItemSolais;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import geometry.Coordinate;
import textures.Textures;
import thunderbrand.Thunderbrand;
import entities.EntityMovementHelper;

public class NPCStickNinja extends NPC {
	
	public static long getMillisToRespawn() { return 5000; }
	
	private static final int MAX_HEALTH = 10;
	private static int ATTACK_POWER = 1;
	private final int npc_type;
	
	public NPCStickNinja(SpawnCondition spawnCondition, String boardName, int type) {
		super(MAX_HEALTH, spawnCondition, boardName);
		npc_type = type;
		
		// TODO: Make a required function that sets all the movement parameters up
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_STICK_NINJA;
	}
	
	@Override public int getStunnedTexture() { return Textures.NPC_STICK_NINJA_STUNNED; }
	
	@Override public boolean isAttackable() { return true; }
	
	@Override public int getAttackPower() { return ATTACK_POWER; }
	
	@Override protected String getName() { return "NPC StickNinja"; }
	
	@Override public Color getMainColor() { return new Color(131, 182, 174); }
	
	@Override
	public List<Item> dropItems() {
		List<Item> items = new ArrayList<Item>();
		
		// Solais return: 100% = 1
		items.add(new ItemSolais(1));
		
		return items;
	}
	
	@Override
	public void respawn() {
		position = new Coordinate(getSpawnCondition().getNewRespawnPoint());
	}
	
	long tempTime = Thunderbrand.getTime();
	Coordinate prevPosition = new Coordinate(0,0);
	boolean goLeft = true;
	boolean jump = true;
	boolean jumpedAlready = false;
	@Override
	protected void updateNPC() {
		if (isBusy() && busy.hasExpired()) {
			busy = null;
		}
		
		long currentTime = Thunderbrand.getTime();
		EntityMovementHelper emh = getMovementHelper();
		if (npc_type == 1) {
			if (goLeft) {
				emh.requestLeftMovement();
			} else {
				emh.requestRightMovement();
			}
			if (jump) {
				emh.requestJumpMovement();
			}
			jump = true;
			emh.moveEntity();
			if (currentTime - tempTime > 500) {
				Coordinate currentPosition = getPosition();
				if (prevPosition.matchesCoordinate(currentPosition)) {
					goLeft = !goLeft;
					jump = false;
				} else {
					prevPosition.setAll(currentPosition);
				}
				tempTime = currentTime;
			}
		} else if (npc_type == 2) {
			if (goLeft) {
				emh.requestLeftMovement();
			} else {
				emh.requestRightMovement();
			}
			if (jump) {
				emh.requestJumpMovement();
			}
			jump = true;
			emh.moveEntity();
			if (currentTime - tempTime > 1500) {
				Coordinate currentPosition = getPosition();
				if (prevPosition.matchesCoordinate(currentPosition)) {
					if (jumpedAlready) {
						goLeft = !goLeft;
						jumpedAlready = false;
					} else {
						jump = false;
						jumpedAlready = true;
					}
				} else {
					prevPosition.setAll(currentPosition);
					jumpedAlready = false;
				}
				tempTime = currentTime;
			}
		}
	}
	
}
