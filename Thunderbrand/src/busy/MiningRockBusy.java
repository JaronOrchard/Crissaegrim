package busy;

import geometry.Coordinate;
import textures.Textures;

public class MiningRockBusy extends StationaryBusy {
	
	private int miningRockTexture1;
	private int miningRockTexture2;
	
	public MiningRockBusy(Coordinate position, String pickaxeType) {
		super(position, ImmobilizingType.NOT_IMMOBILIZED, AttackableType.CAN_BE_ATTACKED);
		setUpMiningRockTextures(pickaxeType);
	}
	
	private void setUpMiningRockTextures(String pickaxeType) {
		if (pickaxeType.equals("Rhichite")) {
			miningRockTexture1 = Textures.STICK_PLAYER_RHICHITE_MINING_1;
			miningRockTexture2 = Textures.STICK_PLAYER_RHICHITE_MINING_2;
		} else if (pickaxeType.equals("Tameike")) {
			miningRockTexture1 = Textures.STICK_PLAYER_TAMEIKE_MINING_1;
			miningRockTexture2 = Textures.STICK_PLAYER_TAMEIKE_MINING_2;
		} else {
			setUpMiningRockTextures("Rhichite");
		}
	}
	
	@Override
	public int getTexture() {
		if (getMillisElapsed() % 800 < 400) return miningRockTexture2;
		return miningRockTexture1;
	}
	
}
