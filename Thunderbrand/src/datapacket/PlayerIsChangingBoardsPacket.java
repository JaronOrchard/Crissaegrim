package datapacket;

public class PlayerIsChangingBoardsPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final boolean sendBoardData;
	public String getBoardName() { return boardName; }
	public boolean getSendBoardData() { return sendBoardData; }
	
	public PlayerIsChangingBoardsPacket(String bName, boolean sendData) {
		super(DataPacketTypes.PLAYER_IS_CHANGING_BOARDS_PACKET);
		boardName = bName;
		sendBoardData = sendData;
	}
	
}
