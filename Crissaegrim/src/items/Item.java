package items;

public abstract class Item {
	
	private final String name;
	private final int texture;
	
	public String getName() { return name; }
	public int getTexture() { return texture; }
	
	public Item(String itemName, int itemTexture) {
		name = itemName;
		texture = itemTexture;
	}
	
}
