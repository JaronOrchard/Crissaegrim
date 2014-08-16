package items;

import textures.Textures;

public class ItemPickaxe extends Item {
	private static final long serialVersionUID = 1L;
	
	private String pickaxeType;
	private double chanceOfSuccessMultiplier;
	
	public String getPickaxeType() { return pickaxeType; }
	public double getChanceOfSuccessMultiplier() { return chanceOfSuccessMultiplier; }
	
	public ItemPickaxe(String oreName) {
		super(oreName + " Pickaxe", getTextureForPickaxeType(oreName));
		pickaxeType = oreName;
		setChanceOfSuccessMultiplier(oreName);
	}
	
	/**
	 * Sets the bonus multiplier that is added due to the pickaxe.
	 * @param oreName
	 */
	private void setChanceOfSuccessMultiplier(String oreName) {
		if (oreName.equals("Rhichite"))			chanceOfSuccessMultiplier = 1.0;
		else if (oreName.equals("Tameike"))		chanceOfSuccessMultiplier = 1.05;
		else chanceOfSuccessMultiplier = 1.0;
	}
	
	private static int getTextureForPickaxeType(String type) {
		if (type.equals("Rhichite")) return Textures.ITEM_RHICHITE_PICKAXE;
		else if (type.equals("Tameike")) return Textures.ITEM_TAMEIKE_PICKAXE;
		return Textures.ITEM_UNKNOWN;
	}
	
}
