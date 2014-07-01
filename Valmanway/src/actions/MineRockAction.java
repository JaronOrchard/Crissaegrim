package actions;

public class MineRockAction extends Action {
	
	private int doodadId;
	private int playerId;
	private String boardName;
	private double chanceOfSuccess;
	
	public int getDoodadId() { return doodadId; }
	public int getPlayerId() { return playerId; }
	public String getBoardName() { return boardName; }
	public double getChanceOfSuccess() { return chanceOfSuccess; }
	
	public MineRockAction(long millis, int doodadId, int playerId, String boardName, double chanceOfSuccess) {
		super(millis);
		this.doodadId = doodadId;
		this.playerId = playerId;
		this.boardName = boardName;
		this.chanceOfSuccess = chanceOfSuccess;
	}
	
}
