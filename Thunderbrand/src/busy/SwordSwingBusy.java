package busy;

import textures.Textures;

public class SwordSwingBusy extends TimedBusy {

	private static long SWORD_SWING_TOTAL_TIME = 500;
	private int swordSwingTexture1;
	private int swordSwingTexture2;
	private int swordSwingTexture3;
	
	public SwordSwingBusy(String swordType) {
		super(SWORD_SWING_TOTAL_TIME, ImmobilizingType.CANNOT_WALK, AttackableType.CAN_BE_ATTACKED);
		setUpSwordSwingTextures(swordType);
	}
	
	private void setUpSwordSwingTextures(String swordType) {
		if (swordType.equals("Rhichite")) {
			swordSwingTexture1 = Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_1;
			swordSwingTexture2 = Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_2;
			swordSwingTexture3 = Textures.STICK_PLAYER_RHICHITE_SWORD_SWING_3;
		} else if (swordType.equals("Tameike")) {
			swordSwingTexture1 = Textures.STICK_PLAYER_TAMEIKE_SWORD_SWING_1;
			swordSwingTexture2 = Textures.STICK_PLAYER_TAMEIKE_SWORD_SWING_2;
			swordSwingTexture3 = Textures.STICK_PLAYER_TAMEIKE_SWORD_SWING_3;
		} else {
			setUpSwordSwingTextures("Rhichite");
		}
	}
	
	@Override
	public int getTexture() {
		long millisElapsed = getMillisElapsed();
		if (millisElapsed < 100) return swordSwingTexture1;
		if (millisElapsed < 300) return swordSwingTexture2;
		return swordSwingTexture3;
	}
	
}
