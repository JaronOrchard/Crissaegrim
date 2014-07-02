package actions;

public class ReplenishRockAction extends Action {
	
	private final int doodadId;
	private final String boardName;
	
	public int getDoodadId() { return doodadId; }
	public String getBoardName() { return boardName; }
	
	public ReplenishRockAction(long millis, int doodadId, String boardName) {
		super(millis);
		this.doodadId = doodadId;
		this.boardName = boardName;
	}
	
}
