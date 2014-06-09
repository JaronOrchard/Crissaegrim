package npc;

import items.Item;

import java.awt.Color;
import java.util.List;
import valmanway.Valmanway;
import entities.Entity;

public abstract class NPC extends Entity {
	
	private boolean alive;
	private SpawnCondition spawnCondition;
	
	public boolean isAlive() { return alive; }
	public SpawnCondition getSpawnCondition() { return spawnCondition; }
	
	public abstract int getAttackPower();
	
	public NPC(int npc_id, int maxHealth, SpawnCondition spawnCond, String boardName) {
		super(maxHealth, Valmanway.getSharedData().getBoardMap());
		id = npc_id;
		currentBoardName = boardName;
		spawnCondition = spawnCond;
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
			respawn();
			alive = true;
		}
		updateNPC();
	}
	
	protected abstract void updateNPC();
	public abstract void respawn();
	public abstract List<Item> dropItems();
	public abstract Color getMainColor();
	
}
