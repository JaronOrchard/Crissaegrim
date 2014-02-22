package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import datapacket.DataPacket;

public class ValmanwayUserData {
	
	private List<DataPacket> outgoingDataPackets;
	private final int playerId;
	
	public int getPlayerId() { return playerId; }
	
	public ValmanwayUserData(int playerId) {
		this.playerId = playerId;
		outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());
	}
	
	public void addOutgoingDataPacket(DataPacket dp) { outgoingDataPackets.add(dp); }
	public boolean outgoingDataPacketsExist() { return !outgoingDataPackets.isEmpty(); }
	public DataPacket popOutgoingDataPacket() { return outgoingDataPackets.remove(0); }
	
}
