package datapacket;

public class ClientIsOutdatedPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	public ClientIsOutdatedPacket() {
		super(DataPacketTypes.CLIENT_IS_OUTDATED_PACKET);
	}
	
}
