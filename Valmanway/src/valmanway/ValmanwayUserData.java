package valmanway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import datapacket.DataPacket;
import datapacket.SendChatMessagePacket;

public class ValmanwayUserData {
	
	private List<DataPacket> outgoingDataPackets;
	public volatile boolean connectionStable = true;
	private final int playerId;
	private String playerName;
	private int lastDisplayedChatMessageIndex;
	
	public int getPlayerId() { return playerId; }
	public String getPlayerName() { return playerName; }
	public void setPlayerName(String n) { playerName = n; }
	public int getLastDisplayedChatMessageIndex() { return lastDisplayedChatMessageIndex; }
	
	public ValmanwayUserData(int playerId, int mostRecentChatMessageIndex) {
		this.playerId = playerId;
		playerName = "Player " + playerId;
		outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());
		lastDisplayedChatMessageIndex = mostRecentChatMessageIndex;
	}
	
	public void addOutgoingDataPacket(DataPacket dp) { outgoingDataPackets.add(dp); }
	public boolean outgoingDataPacketsExist() { return !outgoingDataPackets.isEmpty(); }
	public DataPacket popOutgoingDataPacket() { return outgoingDataPackets.remove(0); }
	
	public void sendNewChatMessages() {
		while (lastDisplayedChatMessageIndex != Valmanway.getSharedData().getMostRecentChatMessageIndex()) {
			lastDisplayedChatMessageIndex++;
			addOutgoingDataPacket(new SendChatMessagePacket(Valmanway.getSharedData().getChatMessage(lastDisplayedChatMessageIndex)));
		}
	}
	
}
