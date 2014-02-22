package valmanway;

class ValmanwaySharedData {
	
//	private Map<Integer, PlayerStatus> playerStatusMap;
	private int nextPlayerId;
	
	public ValmanwaySharedData() {
//		playerStatusMap = Collections.synchronizedMap(new HashMap<Integer, PlayerStatus>());
		nextPlayerId = 1;
	}
	
	public int getNextPlayerId() { return nextPlayerId++; }
	
//	public void updatePlayerStatusMap(int playerId, PlayerStatus playerStatus) {
//		synchronized (playerStatusMap) {
//			playerStatusMap.put(playerId, playerStatus);
//		}
//	}
	
//	public Set<Entry<Integer, PlayerStatus>> getPlayerStatuses() {
//		return playerStatusMap.entrySet();
//	}
	
}
