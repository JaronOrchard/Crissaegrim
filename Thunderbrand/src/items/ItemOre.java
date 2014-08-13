package items;

import textures.Textures;

public class ItemOre extends Item {
	private static final long serialVersionUID = 1L;
	
	private final String oreType;
	public String getOreType() { return oreType; }
	
	public ItemOre(String oreType) {
		super(oreType + " Ore", getTextureForOreType(oreType));
		this.oreType = oreType;
	}
	
	private static int getTextureForOreType(String type) {
		if (type.equals("Rhichite")) return Textures.ITEM_RHICHITE_ORE;
		else if (type.equals("Valenite")) return Textures.ITEM_VALENITE_ORE;
		else if (type.equals("Sandeluge")) return Textures.ITEM_SANDELUGE_ORE;
		return Textures.ITEM_UNKNOWN;
	}
	
}
