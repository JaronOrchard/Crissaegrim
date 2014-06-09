package npc;

import items.Item;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import datapacket.GotHitByAttackPacket;
import valmanway.Valmanway;
import entities.Entity;

public abstract class NPC extends Entity {
	
	private static final long PLAYER_NEXT_ATTACKABLE_DELAY = 101;
	private static final long PNAT_MAP_PURGE_INTERVAL = 30000; // 30 seconds
	private long nextPNATMapPurgeTime = 0;
	
	private boolean alive;
	private SpawnCondition spawnCondition;
	private Map<Integer, Long> playerNextAttackableTimes;
	
	public boolean isAlive() { return alive; }
	public SpawnCondition getSpawnCondition() { return spawnCondition; }
	
	public NPC(int npc_id, int maxHealth, SpawnCondition spawnCond, String boardName) {
		super(maxHealth, Valmanway.getSharedData().getBoardMap());
		id = npc_id;
		currentBoardName = boardName;
		spawnCondition = spawnCond;
		playerNextAttackableTimes = new HashMap<Integer, Long>();
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
		long currentTime = new Date().getTime();
		if (currentTime > nextPNATMapPurgeTime) {
			nextPNATMapPurgeTime = currentTime + PNAT_MAP_PURGE_INTERVAL;
			purgePlayerNextAttackableTimesMap();
		}
	}
	
	protected synchronized void attackPlayer(int playerId) {
		if (!playerNextAttackableTimes.containsKey(playerId) || new Date().getTime() >= playerNextAttackableTimes.get(playerId)) {
			playerNextAttackableTimes.put(playerId, new Date().getTime() + PLAYER_NEXT_ATTACKABLE_DELAY);
			Valmanway.sendPacketToPlayer(playerId, new GotHitByAttackPacket(getName(), getAttackPower()));
		}
	}
	
	private synchronized void purgePlayerNextAttackableTimesMap() {
		Iterator<Entry<Integer, Long>> pnatIter = playerNextAttackableTimes.entrySet().iterator();
		while (pnatIter.hasNext()) {
			Entry<Integer, Long> pnat = pnatIter.next();
			if (new Date().getTime() >= pnat.getValue()) {
				pnatIter.remove();
			}
		}
	}
	
	protected abstract void updateNPC();
	public abstract void respawn();
	public abstract List<Item> dropItems();
	public abstract Color getMainColor();
	public abstract int getAttackPower();
	protected abstract String getName();
	
}
