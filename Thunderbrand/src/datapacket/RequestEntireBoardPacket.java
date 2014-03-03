package datapacket;

public class RequestEntireBoardPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	public String getBoardName() { return boardName; }
	
	public RequestEntireBoardPacket(String bName) {
		super(DataPacketTypes.REQUEST_ENTIRE_BOARD_PACKET);
		boardName = bName;
	}
	
}
