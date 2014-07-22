package players;

import items.Item;
import items.ItemPickaxe;

public final class InventoryUtils {
	
	/**
	 * Determines if the given {@link Inventory} contains an {@link ItemPickaxe} or not.
	 * @param inventory The Inventory to check
	 * @return {@code true} if the Inventory contains an ItemPickaxe, {@code false} otherwise
	 */
	public static boolean containsPickaxe(Inventory inventory) {
		Item[] items = inventory.getItems();
		for (int i = 0; i < inventory.getInventorySize(); i++) {
			if (items[i] instanceof ItemPickaxe) { return true; }
		}
		return false;
	}
	
}
