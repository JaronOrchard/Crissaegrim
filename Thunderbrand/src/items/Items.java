package items;

public class Items {
	
	// Empty slot:
	public static Item nothing() { return new ItemNothing(); }
	
	public static Item solais(int amount) { return new ItemSolais(amount); }
	
	// Ores:
	public static Item rhichiteOre() { return new ItemOre("Rhichite"); }
	public static Item valeniteOre() { return new ItemOre("Valenite"); }
	public static Item sandelugeOre() { return new ItemOre("Sandeluge"); }
	
	// Bars:
	public static Item rhichiteBar() { return new ItemBar("Rhichite"); }
	public static Item valSanBar() { return new ItemBar("Val_San"); }
	
	// Pickaxes:
	public static Item rhichitePickaxe() { return new ItemPickaxe("Rhichite"); }
	
	// Weapons:
	public static Item rhichiteSword() { return new ItemSword("Rhichite"); }
	public static Item valSanSword() { return new ItemSword("Val_San"); }
	
	// Party poppers:
	public static Item bluePartyPopper() { return new ItemPartyPopper("Blue"); }
	public static Item greenPartyPopper() { return new ItemPartyPopper("Green"); }
	public static Item redPartyPopper() { return new ItemPartyPopper("Red"); }
	public static Item yellowPartyPopper() { return new ItemPartyPopper("Yellow"); }
	public static Item whitePartyPopper() { return new ItemPartyPopper("White"); }
	
}
