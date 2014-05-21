package datapacket;

public class RequestPlayerIdPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int clientVersion;
	public int getClientVersion() { return clientVersion; }
	
	public RequestPlayerIdPacket(int version) {
		super(DataPacketTypes.REQUEST_PLAYER_ID_PACKET);
		clientVersion = version;
	}
	
}
