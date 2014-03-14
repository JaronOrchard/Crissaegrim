package valmanway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import player.PlayerStatus;
import thunderbrand.TextBlock;

class ValmanwaySharedData {
	
	private static final int CHAT_MESSAGES_ARRAY_SIZE = 200;
	
	private Map<Integer, PlayerStatus> playerStatusMap;
	private Map<Integer, String> playerNameMap;
	private int nextPlayerId;
	private TextBlock[] chatMessages = new TextBlock[CHAT_MESSAGES_ARRAY_SIZE];
	private int mostRecentChatMessageIndex;
	
	public ValmanwaySharedData() {
		playerStatusMap = Collections.synchronizedMap(new HashMap<Integer, PlayerStatus>());
		playerNameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		nextPlayerId = 1;
		for (int i = 0; i < CHAT_MESSAGES_ARRAY_SIZE; i++) {
			chatMessages[i] = null;
		}
		mostRecentChatMessageIndex = -1;
	}
	
	public int getNextPlayerId() { return nextPlayerId++; }
	public TextBlock getChatMessage(int index) { return chatMessages[index % CHAT_MESSAGES_ARRAY_SIZE]; }
	public int getMostRecentChatMessageIndex() { return mostRecentChatMessageIndex; }
	
	public void updatePlayerStatusMap(int playerId, PlayerStatus playerStatus) {
		playerStatusMap.put(playerId, playerStatus);
	}
	public int getPlayerStatusMapCount() { return playerStatusMap.size(); }
	public Map<Integer, PlayerStatus> getPlayerStatuses() { return new HashMap<Integer, PlayerStatus>(playerStatusMap); }
	
	public String getPlayerName(int playerId) { return playerNameMap.get(playerId); }
	public void setPlayerName(int playerId, String playerName) { playerNameMap.put(playerId, playerName); }
	public boolean isPlayerNameInUse(String playerName) { return playerNameMap.containsValue(playerName); }
	
	public synchronized void addChatMessage(TextBlock tb) {
		// In this order to avoid synchronization issues:
		chatMessages[(mostRecentChatMessageIndex + 1) % CHAT_MESSAGES_ARRAY_SIZE] = tb;
		mostRecentChatMessageIndex++;
		Valmanway.logMessage(tb);
	}
	
	public void dropPlayer(int playerId) {
		playerStatusMap.remove(playerId);
		playerNameMap.remove(playerId);
	}
	
}
