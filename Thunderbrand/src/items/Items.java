package items;

import textures.Textures;

public class Items {
	
	// Ores:
	public static Item rhichiteOre() { return new ItemOre("Rhichite", Textures.ITEM_RHICHITE_ORE); }
	public static Item sandelugeOre() { return new ItemOre("Sandeluge", Textures.ITEM_SANDELUGE_ORE); }
	public static Item valeniteOre() { return new ItemOre("Valenite", Textures.ITEM_VALENITE_ORE); }
	
	// Bars:
	public static Item rhichiteBar() { return new ItemBar("Rhichite", Textures.ITEM_RHICHITE_BAR); }
	public static Item valSanBar() { return new ItemBar("ValSan", Textures.ITEM_VAL_SAN_BAR); }
	
}
