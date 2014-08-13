package items;

import textures.Textures;

public class ItemBar extends Item {
	private static final long serialVersionUID = 1L;
	
	private final String barType;
	public String getBarType() { return barType; }
	
	public ItemBar(String barType) {
		super(barType + " Bar", getTextureForBarType(barType));
		this.barType = barType;
	}
	
	private static int getTextureForBarType(String type) {
		if (type.equals("Rhichite")) return Textures.ITEM_RHICHITE_BAR;
		else if (type.equals("Val_San")) return Textures.ITEM_VAL_SAN_BAR;
		return Textures.ITEM_UNKNOWN;
	}
	
}
