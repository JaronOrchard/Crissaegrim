package npc;

import items.Item;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import board.Board;
import entities.Entity;
import geometry.Coordinate;

public abstract class NPC extends Entity {
	
	private boolean alive;
	private SpawnCondition spawnCondition;
	
	public boolean isAlive() { return alive; }
	
	public abstract int getAttackPower();
	
	public NPC(int npc_id, int maxHealth, SpawnCondition spawnCond, String boardName, Map<String, Board> boardMap) {
		super(maxHealth, boardMap);
		id = npc_id;
		currentBoardName = boardName;
		spawnCondition = spawnCond;
		respawn(spawnCondition.getNewRespawnPoint());
		
		alive = true;
	}
	
	public synchronized void killNPC() {
		alive = false;
		position.setAll(0, 0);
		spawnCondition.setTimeOfDeath();
	}
	
	@Override
	public synchronized final void update() {
		if (!alive) {
			if (!spawnCondition.getRevivedYet()) { return; }
			getHealthBar().healDamage(getHealthBar().getMaxHealth());
			respawn(spawnCondition.getNewRespawnPoint());
			alive = true;
		}
		updateNPC();
	}
	
	protected abstract void updateNPC();
	protected abstract void respawn(Coordinate respawnPoint);
	public abstract List<Item> dropItems();
	public abstract Color getMainColor();
	
}
