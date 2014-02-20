package datapacket;

public class RequestPlayerIdPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	public RequestPlayerIdPacket() {
		super(REQUEST_PLAYER_ID_PACKET);
	}
	
}
