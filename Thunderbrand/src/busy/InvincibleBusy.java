package busy;

import textures.Textures;

public class InvincibleBusy extends Busy {
	
	public InvincibleBusy(long invincibilityMillis) {
		super(invincibilityMillis, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
	}
	
	@Override
	public int getTexture() {
		int amountElapsedInt = (int)(getAmountElapsed() * 100);
		if (amountElapsedInt % 20 < 10) return Textures.NONE;
		return Textures.STICK_PLAYER;
	}
	
}
