package actions;

public class MineRockAction extends Action {
	
	private final int doodadId;
	private final int playerId;
	private final int busyId;
	private final String boardName;
	private final double chanceOfSuccess;
	
	public int getDoodadId() { return doodadId; }
	public int getPlayerId() { return playerId; }
	public int getBusyId() { return busyId; }
	public String getBoardName() { return boardName; }
	public double getChanceOfSuccess() { return chanceOfSuccess; }
	
	public MineRockAction(long millis, int doodadId, int playerId, int busyId, String boardName, double chanceOfSuccess) {
		super(millis);
		this.doodadId = doodadId;
		this.playerId = playerId;
		this.busyId = busyId;
		this.boardName = boardName;
		this.chanceOfSuccess = chanceOfSuccess;
	}
	
}
