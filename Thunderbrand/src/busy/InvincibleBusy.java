package busy;

import textures.Textures;

public class InvincibleBusy extends TimedBusy {
	
	public InvincibleBusy(long invincibilityMillis) {
		super(invincibilityMillis, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 200 < 100) return Textures.NONE;
		return Textures.STICK_PLAYER;
	}
	
}
