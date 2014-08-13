package items;

import textures.Textures;

public class Items {
	
	// Empty slot:
	public static Item nothing() { return new ItemNothing(); }
	
	// Ores:
	public static Item rhichiteOre() { return new ItemOre("Rhichite", Textures.ITEM_RHICHITE_ORE); }
	public static Item sandelugeOre() { return new ItemOre("Sandeluge", Textures.ITEM_SANDELUGE_ORE); }
	public static Item valeniteOre() { return new ItemOre("Valenite", Textures.ITEM_VALENITE_ORE); }
	
	// Bars:
	public static Item rhichiteBar() { return new ItemBar("Rhichite", Textures.ITEM_RHICHITE_BAR); }
	public static Item valSanBar() { return new ItemBar("ValSan", Textures.ITEM_VAL_SAN_BAR); }
	
	// Pickaxes:
	public static Item rhichitePickaxe() { return new ItemPickaxe("Rhichite", Textures.ITEM_RHICHITE_PICKAXE); }
	
	// Weapons:
	public static Item rhichiteSword() { return new ItemSword("Rhichite"); }
	public static Item valSanSword() { return new ItemSword("Val_San"); }
	
}
