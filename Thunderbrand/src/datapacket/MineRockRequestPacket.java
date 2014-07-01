package datapacket;

public class MineRockRequestPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int doodadId;
	private final int playerId;
	private final String boardName;
	private final double chanceOfSuccess;
	
	public int getDoodadId() { return doodadId; }
	public int getPlayerId() { return playerId; }
	public String getBoardName() { return boardName; }
	public double getChanceOfSuccess() { return chanceOfSuccess; }
	
	public MineRockRequestPacket(int doodadId, int playerId, String boardName, double chanceOfSuccess) {
		super(DataPacketTypes.MINE_ROCK_REQUEST_PACKET);
		this.doodadId = doodadId;
		this.playerId = playerId;
		this.boardName = boardName;
		this.chanceOfSuccess = chanceOfSuccess;
	}
	
}
