package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Entity;
import entities.EntityStatus;
import board.Board;
import thunderbrand.TextBlock;

public class ValmanwaySharedData {
	
	private static final int CHAT_MESSAGES_ARRAY_SIZE = 200;
	
	private Map<String, Board> boardMap;
	private List<Entity> entities; // NPCs only
	
	private Map<Integer, EntityStatus> entityStatusMap; // Players + NPCs
	private Map<Integer, String> playerNameMap;
	private int nextPlayerId;
	private TextBlock[] chatMessages = new TextBlock[CHAT_MESSAGES_ARRAY_SIZE];
	private int mostRecentChatMessageIndex;
	
	public ValmanwaySharedData() {
		boardMap = new HashMap<String, Board>();
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		entityStatusMap = Collections.synchronizedMap(new HashMap<Integer, EntityStatus>());
		playerNameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		nextPlayerId = 1;
		for (int i = 0; i < CHAT_MESSAGES_ARRAY_SIZE; i++) {
			chatMessages[i] = null;
		}
		mostRecentChatMessageIndex = -1;
	}
	
	public Map<String, Board> getBoardMap() { return boardMap; }
	public List<Entity> getEntities() { return entities; }
	
	public int getNextPlayerId() { return nextPlayerId++; }
	public TextBlock getChatMessage(int index) { return chatMessages[index % CHAT_MESSAGES_ARRAY_SIZE]; }
	public int getMostRecentChatMessageIndex() { return mostRecentChatMessageIndex; }
	
	public void updateEntityStatusMap(int entityId, EntityStatus entityStatus) {
		entityStatusMap.put(entityId, entityStatus);
	}
	public int getEntityStatusMapCount() { return entityStatusMap.size(); }
	public Map<Integer, EntityStatus> getEntityStatuses() { return new HashMap<Integer, EntityStatus>(entityStatusMap); }
	
	public String getPlayerName(int playerId) { return playerNameMap.get(playerId); }
	public void setPlayerName(int playerId, String playerName) { playerNameMap.put(playerId, playerName); }
	public boolean isPlayerNameInUse(String playerName) { return playerNameMap.containsValue(playerName); }
	public List<String> getPlayerNamesSorted() {
		List<String> names = new ArrayList<String>(playerNameMap.values());
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}
	
	public synchronized void addChatMessage(TextBlock tb) {
		// In this order to avoid synchronization issues:
		chatMessages[(mostRecentChatMessageIndex + 1) % CHAT_MESSAGES_ARRAY_SIZE] = tb;
		mostRecentChatMessageIndex++;
		Valmanway.logMessage(tb);
	}
	
	public void dropPlayer(int playerId) {
		entityStatusMap.remove(playerId);
		playerNameMap.remove(playerId);
	}
	
}
