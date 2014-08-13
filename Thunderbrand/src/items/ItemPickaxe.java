package items;

import textures.Textures;

public class ItemPickaxe extends Item {
	private static final long serialVersionUID = 1L;
	
	public ItemPickaxe(String oreName) {
		super(oreName + " Pickaxe", getTextureForPickaxeType(oreName));
	}
	
	private static int getTextureForPickaxeType(String type) {
		if (type.equals("Rhichite")) return Textures.ITEM_RHICHITE_PICKAXE;
		return Textures.ITEM_UNKNOWN;
	}
	
}
