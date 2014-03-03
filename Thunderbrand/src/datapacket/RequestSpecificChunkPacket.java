package datapacket;

public class RequestSpecificChunkPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final int chunkXOrigin;
	private final int chunkYOrigin;
	public String getBoardName() { return boardName; }
	public int getChunkXOrigin() { return chunkXOrigin; }
	public int getChunkYOrigin() { return chunkYOrigin; }
	
	public RequestSpecificChunkPacket(String bName, int xOrig, int yOrig) {
		super(DataPacketTypes.REQUEST_SPECIFIC_CHUNK_PACKET);
		boardName = bName;
		chunkXOrigin = xOrig;
		chunkYOrigin = yOrig;
	}
	
}
