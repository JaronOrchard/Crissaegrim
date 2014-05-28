package datapacket;

public class IncomingChunkCountPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int incomingChunkCount;
	public int getIncomingChunkCount() { return incomingChunkCount; }
	
	public IncomingChunkCountPacket(int numChunks) {
		super(DataPacketTypes.INCOMING_CHUNK_COUNT_PACKET);
		incomingChunkCount = numChunks;
	}
	
}
