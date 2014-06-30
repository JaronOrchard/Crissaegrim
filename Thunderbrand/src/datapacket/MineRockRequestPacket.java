package datapacket;

public class MineRockRequestPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int rockId;
	private final int playerId;
	private final String boardName;
	private final double chanceOfSuccess;
	
	public int getRockId() { return rockId; }
	public int getPlayerId() { return playerId; }
	public String getBoardName() { return boardName; }
	public double getChanceOfSuccess() { return chanceOfSuccess; }
	
	public MineRockRequestPacket(int rockId, int playerId, String boardName, double chanceOfSuccess) {
		super(DataPacketTypes.MINE_ROCK_REQUEST_PACKET);
		this.rockId = rockId;
		this.playerId = playerId;
		this.boardName = boardName;
		this.chanceOfSuccess = chanceOfSuccess;
	}
	
}
