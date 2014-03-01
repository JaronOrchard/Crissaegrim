package datapacket;

public class ReceivePlayerNamePacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String receivedPlayerName;
	public String getReceivedPlayerName() { return receivedPlayerName; }
	
	public ReceivePlayerNamePacket(String name) {
		super(DataPacketTypes.RECEIVE_PLAYER_NAME_PACKET);
		receivedPlayerName = name;
	}
	
}
