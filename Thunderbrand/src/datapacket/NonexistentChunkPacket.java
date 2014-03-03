package datapacket;

public class NonexistentChunkPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final int chunkXOrigin;
	private final int chunkYOrigin;
	public String getBoardName() { return boardName; }
	public int getChunkXOrigin() { return chunkXOrigin; }
	public int getChunkYOrigin() { return chunkYOrigin; }
	
	public NonexistentChunkPacket(String bName, int xOrig, int yOrig) {
		super(DataPacketTypes.NONEXISTENT_CHUNK_PACKET);
		boardName = bName;
		chunkXOrigin = xOrig;
		chunkYOrigin = yOrig;
	}
	
}
