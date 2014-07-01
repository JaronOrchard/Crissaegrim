package busy;

public class PlayerDiedBusy extends TimedBusy {

	private static long STUNNED_TOTAL_TIME = 4000;
	private final int deadTexture;
	
	public PlayerDiedBusy(int deadTexture) {
		super(STUNNED_TOTAL_TIME, ImmobilizingType.COMPLETELY_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
		this.deadTexture = deadTexture;
	}
	
	@Override
	public int getTexture() {
		return deadTexture;
	}
	
}
