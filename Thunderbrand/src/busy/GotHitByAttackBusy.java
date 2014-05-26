package busy;

import textures.Textures;

public class GotHitByAttackBusy extends Busy {

	private static long STUNNED_TOTAL_TIME = 1000;
	
	public GotHitByAttackBusy() {
		super(STUNNED_TOTAL_TIME, ImmobilizingType.COMPLETELY_IMMOBILIZED);
	}

	@Override
	public int getTexture() {
		int amountElapsedInt = (int)(getAmountElapsed() * 100);
		if (amountElapsedInt % 20 < 10) return Textures.NONE;
		return Textures.STICK_PLAYER_STUNNED;
	}
	
}
