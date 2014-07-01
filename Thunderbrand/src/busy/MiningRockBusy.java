package busy;

import geometry.Coordinate;
import textures.Textures;

public class MiningRockBusy extends StationaryBusy {
	
	public MiningRockBusy(Coordinate position) {
		super(position, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.CAN_BE_ATTACKED);
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 200 < 100) return Textures.STICK_PLAYER_TYPING;
		return Textures.STICK_PLAYER_STUNNED;
	}
	
}
