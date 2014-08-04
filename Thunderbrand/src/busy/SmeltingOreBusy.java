package busy;

import geometry.Coordinate;
import textures.Textures;

public class SmeltingOreBusy extends StationaryBusy {
	
	public SmeltingOreBusy(Coordinate position) {
		super(position, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.CAN_BE_ATTACKED);
	}
	
	@Override
	public int getTexture() {
		return Textures.STICK_PLAYER_SMELTING_ORE;
	}
	
}
