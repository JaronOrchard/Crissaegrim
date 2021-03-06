package players;

import crissaegrim.Crissaegrim;
import items.Item;
import items.ItemOre;
import items.ItemPickaxe;
import items.StackableItem;

public final class InventoryUtils {
	
	/**
	 * Determines if the player's {@link Inventory} contains an {@link ItemPickaxe} or not.
	 * @return {@code true} if the player has an ItemPickaxe, {@code false} otherwise
	 */
	public static boolean containsPickaxe() {
		Inventory inventory = Crissaegrim.getPlayer().getInventory();
		Item[] items = inventory.getItems();
		for (int i = 0; i < inventory.getInventorySize(); i++) {
			if (items[i] instanceof ItemPickaxe) { return true; }
		}
		return false;
	}
	
	/**
	 * Determines if the player's {@link Inventory} contains certain {@link ItemOre}s or not.
	 * @param oreName The name of the ItemOre to check for
	 * @return {@code true} if the player has at least one of this certain ItemOre, {@code false} otherwise
	 */
	public static boolean containsOre(String oreName) {
		Inventory inventory = Crissaegrim.getPlayer().getInventory();
		Item[] items = inventory.getItems();
		for (int i = 0; i < inventory.getInventorySize(); i++) {
			if (items[i] instanceof ItemOre && ((ItemOre)(items[i])).getOreType().equals(oreName)) { return true; }
		}
		return false;
	}
	
	/**
	 * Given an {@link Item}, if the item is of type {@link StackableItem} and a stack of that item
	 * already exists in the player's inventory, returns the index of that stack.  Otherwise, returns -1.
	 * @param item The {@link Item} to search for
	 * @return The index of the current stack of that item in the player's inventory if it exists, {@code -1} otherwise
	 */
	public static int findIndexOfStack(Item item) {
		if (item instanceof StackableItem) {
			Inventory inventory = Crissaegrim.getPlayer().getInventory();
			Item[] items = inventory.getItems();
			for (int i = 0; i < inventory.getInventorySize(); i++) {
				if (items[i].getName().equals(item.getName())) {
					return i;
				}
			}
		}
		return -1;
	}
	
}
