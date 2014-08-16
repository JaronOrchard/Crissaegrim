package items;

public class Items {
	
	// Empty slot:
	public static Item nothing() { return new ItemNothing(); }
	
	// Solais:
	public static Item solais(int amount) { return new ItemSolais(amount); }
	
	// Ores:
	public static Item rhichiteOre() { return new ItemOre("Rhichite"); }
	public static Item valeniteOre() { return new ItemOre("Valenite"); }
	public static Item sandelugeOre() { return new ItemOre("Sandeluge"); }
	
	// Bars:
	public static Item rhichiteBar() { return new ItemBar("Rhichite"); }
	public static Item tameikeBar() { return new ItemBar("Tameike"); }
	
	// Pickaxes:
	public static Item rhichitePickaxe() { return new ItemPickaxe("Rhichite"); }
	public static Item tameikePickaxe() { return new ItemPickaxe("Tameike"); }
	
	// Weapons:
	public static Item rhichiteSword() { return new ItemSword("Rhichite"); }
	public static Item tameikeSword() { return new ItemSword("Tameike"); }
	
	// Party poppers:
	public static Item bluePartyPopper() { return new ItemPartyPopper("Blue"); }
	public static Item greenPartyPopper() { return new ItemPartyPopper("Green"); }
	public static Item redPartyPopper() { return new ItemPartyPopper("Red"); }
	public static Item yellowPartyPopper() { return new ItemPartyPopper("Yellow"); }
	public static Item whitePartyPopper() { return new ItemPartyPopper("White"); }
	
}
