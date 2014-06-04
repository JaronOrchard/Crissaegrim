package items;

import java.awt.Color;

public class ItemPartyPopper extends Item {
	private static final long serialVersionUID = 1L;
	
	private final Color color;
	private int usesLeft;
	public Color getColor() { return color; }
	public int getUsesLeft() { return usesLeft; }
	public void decrementUses() { usesLeft--; }
	
	public ItemPartyPopper(String colorName, int textureId) {
		super(colorName + " Party Popper", textureId);
		usesLeft = 3;
		
		if (colorName.equals("Blue")) color = new Color(63, 72, 204);
		else if (colorName.equals("Green")) color = new Color(27, 146, 63);
		else if (colorName.equals("Red")) color = new Color(237, 28, 36);
		else if (colorName.equals("White")) color = new Color(255, 255, 255);
		else /*if (colorName.equals("Yellow"))*/ color = new Color(255, 242, 0);
	}
	
}
