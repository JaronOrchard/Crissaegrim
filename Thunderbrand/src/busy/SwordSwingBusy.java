package busy;

import textures.Textures;

public class SwordSwingBusy extends Busy {

	private static long SWORD_SWING_TOTAL_TIME = 500;
	
	public SwordSwingBusy() {
		super(SWORD_SWING_TOTAL_TIME, ImmobilizingType.CANNOT_WALK);
	}

	@Override
	public int getTexture() {
		double amountElapsed = getAmountElapsed();
		if (amountElapsed < 0.20) return Textures.STICK_PLAYER_SWORD_SWING_1;
		if (amountElapsed < 0.60) return Textures.STICK_PLAYER_SWORD_SWING_2;
		return Textures.STICK_PLAYER_SWORD_SWING_3;
	}
	
}
