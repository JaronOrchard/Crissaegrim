package datapacket;

public class ReceivePlayerIdPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int receivedPlayerId;
	public int getReceivedPlayerId() { return receivedPlayerId; }
	
	public ReceivePlayerIdPacket(int id) {
		super(RECEIVE_PLAYER_ID_PACKET);
		receivedPlayerId = id;
	}
	
}
