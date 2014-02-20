package busy;

import textures.Textures;

public class BusySwordSwing extends Busy {

	private static long SWORD_SWING_TOTAL_TIME = 300;
	
	public BusySwordSwing() {
		super(SWORD_SWING_TOTAL_TIME);
	}

	@Override
	public int getTexture() {
		double amountElapsed = getAmountElapsed();
		if (amountElapsed < 0.33 || amountElapsed > 0.67) {
			return Textures.STICK_PLAYER_ATTACK_1;
		}
		return Textures.STICK_PLAYER_ATTACK_2;
	}
	
}
