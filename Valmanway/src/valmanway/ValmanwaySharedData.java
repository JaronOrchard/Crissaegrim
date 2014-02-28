package valmanway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import player.PlayerStatus;

class ValmanwaySharedData {
	
	private Map<Integer, PlayerStatus> playerStatusMap;
	private int nextPlayerId;
	
	public ValmanwaySharedData() {
		playerStatusMap = Collections.synchronizedMap(new HashMap<Integer, PlayerStatus>());
		nextPlayerId = 1;
	}
	
	public int getNextPlayerId() { return nextPlayerId++; }
	
	public void updatePlayerStatusMap(int playerId, PlayerStatus playerStatus) {
		playerStatusMap.put(playerId, playerStatus);
	}
	
	public int getPlayerStatusMapCount() { return playerStatusMap.size(); }
	public Map<Integer, PlayerStatus> getPlayerStatuses() { return new HashMap<Integer, PlayerStatus>(playerStatusMap); }
	
	public void dropPlayer(int playerId) {
		playerStatusMap.remove(playerId);
	}
	
}
