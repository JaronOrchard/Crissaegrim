package items;

import textures.Textures;

public class ItemSolais extends Item {
	private static final long serialVersionUID = 1L;
	
	private final int numSolais;
	public int getNumSolais() { return numSolais; }
	
	public ItemSolais(int count) {
		super("Solais", Textures.NONE);
		numSolais = count;
	}
	
}
