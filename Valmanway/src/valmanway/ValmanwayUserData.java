package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import datapacket.DataPacket;

public class ValmanwayUserData {
	
	private List<DataPacket> outgoingDataPackets;
	public volatile boolean connectionStable = true;
	private final int playerId;
	private String playerName;
	
	public int getPlayerId() { return playerId; }
	public String getPlayerName() { return playerName; }
	public void setPlayerName(String n) { playerName = n; }
	
	public ValmanwayUserData(int playerId) {
		this.playerId = playerId;
		playerName = "Player " + playerId;
		outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());
	}
	
	public void addOutgoingDataPacket(DataPacket dp) { outgoingDataPackets.add(dp); }
	public boolean outgoingDataPacketsExist() { return !outgoingDataPackets.isEmpty(); }
	public DataPacket popOutgoingDataPacket() { return outgoingDataPackets.remove(0); }
	
}
