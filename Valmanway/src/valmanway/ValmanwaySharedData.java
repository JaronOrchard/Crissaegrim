package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import npc.NPC;
import datapacket.DataPacket;
import datapacket.SendChatMessagePacket;
import entities.EntityStatus;
import actions.Action;
import board.Board;
import textblock.TextBlock;

public class ValmanwaySharedData {
	
	private static final int DATAPACKETS_ARRAY_SIZE = 1000;
	
	private Map<String, Board> boardMap;
	private Map<String, List<NPC>> npcs;
	
	private PriorityQueue<Action> actionQueue;							// PriorityQueue of Actions
	private Map<String, Map<Integer, EntityStatus>> npcStatusMap;   	// EntityStatuses of NPCs
	private Map<String, Map<Integer, EntityStatus>> playerStatusMap;	// EntityStatuses of Players
	
	public PriorityQueue<Action> getActionQueue() { return actionQueue; }
	public Map<String, Map<Integer, EntityStatus>> getNpcStatusMap() { return npcStatusMap; }
	public Map<String, Map<Integer, EntityStatus>> getPlayerStatusMap() { return playerStatusMap; }
	public void addActionToQueue(Action action) { synchronized(actionQueue) { actionQueue.add(action); } }
	public void updateNpcStatusMap(int npcId, String boardName, EntityStatus entityStatus) { npcStatusMap.get(boardName).put(npcId, entityStatus); }
	public void updatePlayerStatusMap(int playerId, String boardName, EntityStatus entityStatus) { playerStatusMap.get(boardName).put(playerId, entityStatus); }
	public void removePlayerStatusFromBoard(int playerId, String boardName) { playerStatusMap.get(boardName).remove(playerId); }
	
	private Map<Integer, String> playerNameMap;
	private int nextPlayerId;
	private DataPacket[] dataPackets = new DataPacket[DATAPACKETS_ARRAY_SIZE];
	private int mostRecentDataPacketIndex;
	
	public ValmanwaySharedData() {
		boardMap = new HashMap<String, Board>();
		npcs = Collections.synchronizedMap(new HashMap<String, List<NPC>>());
		npcStatusMap = Collections.synchronizedMap(new HashMap<String, Map<Integer, EntityStatus>>());
		playerStatusMap = Collections.synchronizedMap(new HashMap<String, Map<Integer, EntityStatus>>());
		for (String boardName : Valmanway.getBoardNames()) {
			npcs.put(boardName, Collections.synchronizedList(new ArrayList<NPC>()));
			npcStatusMap.put(boardName, Collections.synchronizedMap(new HashMap<Integer, EntityStatus>()));
			playerStatusMap.put(boardName, Collections.synchronizedMap(new HashMap<Integer, EntityStatus>()));
		}
		playerNameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		nextPlayerId = 1;
		for (int i = 0; i < DATAPACKETS_ARRAY_SIZE; i++) {
			dataPackets[i] = null;
		}
		actionQueue = new PriorityQueue<Action>(10, new ActionComparator());
		mostRecentDataPacketIndex = -1;
	}
	
	public class ActionComparator implements Comparator<Action> {
		@Override
		public int compare(Action action1, Action action2) {
			return (action1.getActionTime() < action2.getActionTime() ? -1 : 1);
		}
	}
	
	public Map<String, Board> getBoardMap() { return boardMap; }
	public Map<String, List<NPC>> getNPCs() { return npcs; }
	
	public int getNextPlayerId() { return nextPlayerId++; }
	public DataPacket getDataPacket(int index) { return dataPackets[index % DATAPACKETS_ARRAY_SIZE]; }
	public int getMostRecentDataPacketIndex() { return mostRecentDataPacketIndex; }
	
	public String getPlayerName(int playerId) { return playerNameMap.get(playerId); }
	public void setPlayerName(int playerId, String playerName) { playerNameMap.put(playerId, playerName); }
	public boolean isPlayerNameInUse(String playerName) {
		String lowercaseName = playerName.toLowerCase();
		for (String name : playerNameMap.values()) {
			if (name.toLowerCase().equals(lowercaseName)) {
				return true;
			}
		}
		return false;
	}
	public List<String> getPlayerNamesSorted() {
		List<String> names = new ArrayList<String>(playerNameMap.values());
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}
	
	public void addChatMessage(TextBlock tb) {
		DataPacket packet = new SendChatMessagePacket(tb);
		addDataPacket(packet);
		Valmanway.logMessage(tb);
	}
	
	public synchronized void addDataPacket(DataPacket packet) {
		// In this order to avoid synchronization issues:
		dataPackets[(mostRecentDataPacketIndex + 1) % DATAPACKETS_ARRAY_SIZE] = packet;
		mostRecentDataPacketIndex++;
	}
	
	public void dropPlayer(int playerId, String boardName) {
		if (!boardName.isEmpty()) { removePlayerStatusFromBoard(playerId, boardName); }
		playerNameMap.remove(playerId);
	}
	
}
