package items;

public class ItemOre extends Item {
	private static final long serialVersionUID = 1L;
	
	private final String oreType;
	public String getOreType() { return oreType; }
	
	public ItemOre(String oreType, int textureId) {
		super(oreType + " Ore", textureId);
		this.oreType = oreType;
	}
	
}
