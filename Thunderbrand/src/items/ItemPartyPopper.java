package items;

import java.awt.Color;

import textures.Textures;

public class ItemPartyPopper extends Item {
	private static final long serialVersionUID = 1L;
	
	private final Color color;
	private int usesLeft;
	public Color getColor() { return color; }
	public int getUsesLeft() { return usesLeft; }
	public void decrementUses() { usesLeft--; }
	
	public ItemPartyPopper(String colorName) {
		super(colorName + " Party Popper", getTextureForPartyPopperColor(colorName));
		usesLeft = 3;
		
		if (colorName.equals("Blue")) color = new Color(63, 72, 204);
		else if (colorName.equals("Green")) color = new Color(27, 146, 63);
		else if (colorName.equals("Red")) color = new Color(237, 28, 36);
		else if (colorName.equals("Yellow")) color = new Color(255, 242, 0);
		else /*if (colorName.equals("White"))*/ color = new Color(255, 255, 255);
		
	}
	
	private static int getTextureForPartyPopperColor(String color) {
		if (color.equals("Blue")) return Textures.ITEM_PARTY_POPPER_BLUE;
		else if (color.equals("Green")) return Textures.ITEM_PARTY_POPPER_GREEN;
		else if (color.equals("Red")) return Textures.ITEM_PARTY_POPPER_RED;
		else if (color.equals("Yellow")) return Textures.ITEM_PARTY_POPPER_YELLOW;
		else if (color.equals("White")) return Textures.ITEM_PARTY_POPPER_WHITE;
		return Textures.ITEM_UNKNOWN;
	}
}
