package datapacket;

public class DoneSendingChunksPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	public DoneSendingChunksPacket() {
		super(DataPacketTypes.DONE_SENDING_CHUNKS_PACKET);
	}
	
}
