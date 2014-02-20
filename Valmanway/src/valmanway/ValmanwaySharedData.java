package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import datapacket.DataPacket;

class ValmanwaySharedData {
	
	private List<DataPacket> outgoingDataPackets;
	
	private Map<Integer, PlayerStatus> playerStatusMap;
	private int nextPlayerId;
	
	public ValmanwaySharedData() {
		outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());
		playerStatusMap = Collections.synchronizedMap(new HashMap<Integer, PlayerStatus>());
		nextPlayerId = 1;
	}
	
	public void addOutgoingDataPacket(DataPacket dp) { outgoingDataPackets.add(dp); }
	public boolean outgoingDataPacketsExist() { return !outgoingDataPackets.isEmpty(); }
	public DataPacket popOutgoingDataPacket() { return outgoingDataPackets.remove(0); }
	
	public int getNextPlayerId() { return nextPlayerId++; }
	
	public void updatePlayerStatusMap(int playerId, PlayerStatus playerStatus) {
		synchronized (playerStatusMap) {
			playerStatusMap.put(playerId, playerStatus);
		}
	}
	
	public Set<Entry<Integer, PlayerStatus>> getPlayerStatuses() {
		return playerStatusMap.entrySet();
	}
	
}
