package actions;

public class MineRockAction extends Action {
	
	private int rockId;
	private int playerId;
	private String boardName;
	private double chanceOfSuccess;
	
	public MineRockAction(long millis, int rockId, int playerId, String boardName, double chanceOfSuccess) {
		super(millis);
		this.rockId = rockId;
		this.playerId = playerId;
		this.boardName = boardName;
		this.chanceOfSuccess = chanceOfSuccess;
	}
	
}
