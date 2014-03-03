package datapacket;

public class ChunkPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final int chunkXOrigin;
	private final int chunkYOrigin;
	private final byte[] data;
	public String getBoardName() { return boardName; }
	public int getChunkXOrigin() { return chunkXOrigin; }
	public int getChunkYOrigin() { return chunkYOrigin; }
	public byte[] getData() { return data; }
	
	public ChunkPacket(String bName, int xOrig, int yOrig, byte[] bytes) {
		super(DataPacketTypes.CHUNK_PACKET);
		boardName = bName;
		chunkXOrigin = xOrig;
		chunkYOrigin = yOrig;
		data = bytes;
	}
	
}
