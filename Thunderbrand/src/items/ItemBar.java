package items;

public class ItemBar extends Item {
	private static final long serialVersionUID = 1L;
	
	private final String barType;
	public String getBarType() { return barType; }
	
	public ItemBar(String barType, int textureId) {
		super(barType + " Bar", textureId);
		this.barType = barType;
	}
	
}
