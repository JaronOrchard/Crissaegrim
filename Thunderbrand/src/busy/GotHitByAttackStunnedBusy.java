package busy;

import textures.Textures;

public class GotHitByAttackStunnedBusy extends TimedBusy {
	
	private static long STUNNED_TOTAL_TIME = 1000;
	private final int stunnedTexture;
	
	public GotHitByAttackStunnedBusy(int stunnedTexture) {
		super(STUNNED_TOTAL_TIME, ImmobilizingType.COMPLETELY_IMMOBILIZED, AttackableType.IMMUNE_TO_ATTACKS);
		this.stunnedTexture = stunnedTexture;
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 200 < 100) return Textures.NONE;
		return stunnedTexture;
	}
	
}
