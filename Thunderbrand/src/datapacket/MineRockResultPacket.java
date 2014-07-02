package datapacket;

public class MineRockResultPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final boolean succeeded;
	private final int doodadId;
	private final int busyId;
	private final String boardName;
	
	public boolean getSucceeded() { return succeeded; }
	public int getDoodadId() { return doodadId; }
	public int getBusyId() { return busyId; }
	public String getBoardName() { return boardName; }
	
	public MineRockResultPacket(boolean succeeded, int doodadId, int busyId, String boardName) {
		super(DataPacketTypes.MINE_ROCK_RESULT_PACKET);
		this.succeeded = succeeded;
		this.doodadId = doodadId;
		this.busyId = busyId;
		this.boardName = boardName;
	}
	
}
