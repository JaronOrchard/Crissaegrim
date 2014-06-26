package busy;

import textures.Textures;

public class GotHitByAttackStunnedBusy extends Busy {
	
	private static long STUNNED_TOTAL_TIME = 1000;
	private final int stunnedTexture;
	
	public GotHitByAttackStunnedBusy(int stunnedTexture) {
		super(STUNNED_TOTAL_TIME, ImmobilizingType.COMPLETELY_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
		this.stunnedTexture = stunnedTexture;
	}
	
	@Override
	public int getTexture() {
		int amountElapsedInt = (int)(getAmountElapsed() * 100);
		if (amountElapsedInt % 20 < 10) return Textures.NONE;
		return stunnedTexture;
	}
	
}