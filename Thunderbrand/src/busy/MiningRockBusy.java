package busy;

import geometry.Coordinate;
import textures.Textures;

public class MiningRockBusy extends StationaryBusy {
	
	public MiningRockBusy(Coordinate position) {
		super(position, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.CAN_BE_ATTACKED);
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 800 < 400) return Textures.STICK_PLAYER_RHICHITE_MINING_2;
		return Textures.STICK_PLAYER_RHICHITE_MINING_1;
	}
	
}
