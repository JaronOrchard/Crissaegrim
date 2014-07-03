package busy;

import textures.Textures;

public class SwordSwingBusy extends TimedBusy {

	private static long SWORD_SWING_TOTAL_TIME = 500;
	
	public SwordSwingBusy() {
		super(SWORD_SWING_TOTAL_TIME, ImmobilizingType.CANNOT_WALK, AttackableType.CAN_BE_ATTACKED);
	}

	@Override
	public int getTexture() {
		long millisElapsed = getMillisElapsed();
		if (millisElapsed < 100) return Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_1;
		if (millisElapsed < 300) return Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_2;
		return Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_3;
	}
	
}
