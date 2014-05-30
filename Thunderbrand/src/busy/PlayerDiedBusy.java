package busy;

public class PlayerDiedBusy extends Busy {

	private static long STUNNED_TOTAL_TIME = 4000;
	private final int deadTexture;
	
	public PlayerDiedBusy(int deadTexture) {
		super(STUNNED_TOTAL_TIME, ImmobilizingType.COMPLETELY_IMMOBILIZED);
		this.deadTexture = deadTexture;
	}
	
	@Override
	public int getTexture() {
		return deadTexture;
	}
	
}
