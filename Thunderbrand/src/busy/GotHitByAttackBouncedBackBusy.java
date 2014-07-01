package busy;

import textures.Textures;

public class GotHitByAttackBouncedBackBusy extends Busy {
	
	private final int stunnedTexture;
	
	public GotHitByAttackBouncedBackBusy(int stunnedTexture) {
		super(ImmobilizingType.COMPLETELY_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
		this.stunnedTexture = stunnedTexture;
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 200 < 100) return Textures.NONE;
		return stunnedTexture;
	}
	
}
