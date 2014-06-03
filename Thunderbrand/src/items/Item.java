package items;

import java.io.Serializable;

public abstract class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final int texture;
	
	public String getName() { return name; }
	public int getTexture() { return texture; }
	
	public Item(String itemName, int itemTexture) {
		name = itemName;
		texture = itemTexture;
	}
	
}
