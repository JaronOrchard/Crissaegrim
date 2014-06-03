package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datapacket.DataPacket;
import datapacket.SendChatMessagePacket;
import entities.Entity;
import entities.EntityStatus;
import board.Board;
import textblock.TextBlock;

public class ValmanwaySharedData {
	
	private static final int DATAPACKETS_ARRAY_SIZE = 1000;
	
	private Map<String, Board> boardMap;
	private List<Entity> entities; // NPCs only
	
	private Map<Integer, EntityStatus> entityStatusMap; // Players + NPCs
	private Map<Integer, String> playerNameMap;
	private int nextPlayerId;
	private DataPacket[] dataPackets = new DataPacket[DATAPACKETS_ARRAY_SIZE];
	private int mostRecentDataPacketIndex;
	
	public ValmanwaySharedData() {
		boardMap = new HashMap<String, Board>();
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		entityStatusMap = Collections.synchronizedMap(new HashMap<Integer, EntityStatus>());
		playerNameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		nextPlayerId = 1;
		for (int i = 0; i < DATAPACKETS_ARRAY_SIZE; i++) {
			dataPackets[i] = null;
		}
		mostRecentDataPacketIndex = -1;
	}
	
	public Map<String, Board> getBoardMap() { return boardMap; }
	public List<Entity> getEntities() { return entities; }
	
	public int getNextPlayerId() { return nextPlayerId++; }
	public DataPacket getDataPacket(int index) { return dataPackets[index % DATAPACKETS_ARRAY_SIZE]; }
	public int getMostRecentDataPacketIndex() { return mostRecentDataPacketIndex; }
	
	public void updateEntityStatusMap(int entityId, EntityStatus entityStatus) {
		entityStatusMap.put(entityId, entityStatus);
	}
	public int getEntityStatusMapCount() { return entityStatusMap.size(); }
	public Map<Integer, EntityStatus> getEntityStatuses() { return new HashMap<Integer, EntityStatus>(entityStatusMap); }
	
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
	
	public void dropPlayer(int playerId) {
		entityStatusMap.remove(playerId);
		playerNameMap.remove(playerId);
	}
	
}
