package players;

import java.util.Date;

import outside_src.RuntimeTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import textblock.TextTexture;
import crissaegrim.Crissaegrim;
import gldrawer.GLDrawer;
import items.Item;
import items.ItemBar;
import items.ItemNothing;
import items.ItemOre;
import items.ItemPartyPopper;
import items.ItemPickaxe;
import items.ItemSolais;
import items.Items;
import items.ItemSword;
import items.StackableItem;

public class Inventory {
	
	private static final int BOX_SIZE_PIXELS = 32;
	private static final int INNER_PADDING_PIXELS = 4;
	private static final int OUTER_PADDING_PIXELS = 8;
	private static final long MILLIS_AT_FULL_EXTENDED = 2000;
	private static final long MILLIS_TO_SLIDE_RIGHT = 300;
	
	private static final int INVENTORY_QUICKEQUIP_SLOTS = 6;
	private static final int INVENTORY_SIZE = 30;
	private TextTexture solaisTextTexture;
	private Gson gson;
	
	private Item[] items;
	private int solais;
	private int solaisCountOnTextTexture; // Used to determine when the Solais count changes
	private int selectedQuickequipItemIndex; // Only goes from 0 to INVENTORY_QUICKEQUIP_SLOTS-1
	private long lastTouchedTime = 0;
	
	public int getInventorySize() { return INVENTORY_SIZE; }
	public Item[] getItems() { return items; }
	public Item getItem(int index) { return items[index]; }
	public void setItem(int index, Item item) { items[index] = item; serializeAndSaveItems(); }
	public void removeItem(int index) { items[index] = Items.nothing(); serializeAndSaveItems(); }
	
	public Inventory() {
		selectedQuickequipItemIndex = 0;
		
		items = new Item[INVENTORY_SIZE];
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			items[i] = Items.nothing();
		}
		items[0] = Items.rhichiteSword();
		items[1] = Items.rhichitePickaxe();
		items[8] = Items.rhichiteOre();
		items[15] = Items.valeniteOre();
		items[19] = Items.sandelugeOre();
		
		RuntimeTypeAdapterFactory<Item> adapter = RuntimeTypeAdapterFactory.of(Item.class)
				.registerSubtype(ItemBar.class)
				.registerSubtype(ItemNothing.class)
				.registerSubtype(ItemOre.class)
				.registerSubtype(ItemPartyPopper.class)
				.registerSubtype(ItemPickaxe.class)
				.registerSubtype(ItemSolais.class)
				.registerSubtype(ItemSword.class);
		gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
		
		deserializeAndLoadSolais();
		solaisCountOnTextTexture = solais;
		updateSolaisTextTexture();
		
		deserializeAndLoadItems();
	}
	
	private void updateSolaisTextTexture() {
		solaisTextTexture = Crissaegrim.getCommonTextures().getTextTexture("Solais: " + Integer.toString(solais));
	}
	
	public Item getCurrentItem() { return items[selectedQuickequipItemIndex]; }
	
	public void removeCurrentItem() {
		items[selectedQuickequipItemIndex] = Items.nothing();
		serializeAndSaveItems();
	}
	
	public void selectPreviousItem() {
		selectedQuickequipItemIndex = (selectedQuickequipItemIndex + INVENTORY_QUICKEQUIP_SLOTS - 1) % INVENTORY_QUICKEQUIP_SLOTS;
		updateLastTouchedTime();
	}
	public void selectNextItem() {
		selectedQuickequipItemIndex = (selectedQuickequipItemIndex + 1) % INVENTORY_QUICKEQUIP_SLOTS;
		updateLastTouchedTime();
	}
	public void selectSpecificItem(int index) {
		if (index < 0) {
			selectedQuickequipItemIndex = 0;
		} else if (index >= INVENTORY_QUICKEQUIP_SLOTS) {
			selectedQuickequipItemIndex = INVENTORY_QUICKEQUIP_SLOTS - 1;
		} else {
			selectedQuickequipItemIndex = index;
		}
		updateLastTouchedTime();
	}
	private void updateLastTouchedTime() { lastTouchedTime = new Date().getTime(); }
	
	public void addSolais(int amount) {
		solais += amount;
		serializeAndSaveSolais();
	}
	
	/**
	 * Adds the {@link Item} to the Inventory.  If the Inventory is full, returns {@code false}.
	 * @param item The {@link Item} to add to the Inventory
	 * @return {@code true} if the Item was added, {@code false} otherwise
	 */
	public boolean addItem(Item item) {
		int itemStackIndex = InventoryUtils.findIndexOfStack(item);
		if (itemStackIndex != -1) {
			((StackableItem)(items[itemStackIndex])).addToStack(((StackableItem)(item)).getNumberInStack());
			return true;
		}
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			if (items[i] instanceof ItemNothing) {
				items[i] = item;
				serializeAndSaveItems();
				return true;
			}
		}
		return false;
	}
	
	private void serializeAndSaveSolais() {
		Crissaegrim.getPreferenceHandler().setInventorySolais(solais);
	}
	
	private void serializeAndSaveItems() {
		Crissaegrim.getPreferenceHandler().setInventoryItemsJson(gson.toJson(items, Item[].class));
	}
	
	private void deserializeAndLoadSolais() {
		solais = Crissaegrim.getPreferenceHandler().getInventorySolais();
	}
	
	private void deserializeAndLoadItems() {
		String jsonItems = Crissaegrim.getPreferenceHandler().getInventoryItemsJson();
		if (jsonItems != null) {
			try {
				items = gson.fromJson(jsonItems, Item[].class);
			} catch (JsonParseException e) {
				Crissaegrim.addSystemMessage("Your inventory could not be loaded and was reset.  Sorry!");
				serializeAndSaveItems();
			}
		}
	}
	
	public void draw() {
		long now = new Date().getTime();
		if (items[selectedQuickequipItemIndex] instanceof ItemNothing && now - lastTouchedTime > MILLIS_AT_FULL_EXTENDED + MILLIS_TO_SLIDE_RIGHT) {
			return; // Inventory is out of view
		}
		if (solaisCountOnTextTexture != solais) {
			solaisCountOnTextTexture = solais;
			updateSolaisTextTexture();
		}
		int topY = Crissaegrim.getWindowHeight() - OUTER_PADDING_PIXELS;
		int selectedItemRightX = Crissaegrim.getWindowWidth() - OUTER_PADDING_PIXELS;
		int rightX;
		if (now - lastTouchedTime < MILLIS_AT_FULL_EXTENDED) {
			rightX = selectedItemRightX;
		} else {
			double amtSlid = ((double)(now - lastTouchedTime - MILLIS_AT_FULL_EXTENDED)) / (double)MILLIS_TO_SLIDE_RIGHT;
			rightX = (int)((1.0 - amtSlid)*(selectedItemRightX) +
					amtSlid*(Crissaegrim.getWindowWidth() + BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2));
			if (items[selectedQuickequipItemIndex] instanceof ItemNothing) {
				selectedItemRightX = (int)((1.0 - amtSlid)*(selectedItemRightX) +
						amtSlid*(Crissaegrim.getWindowWidth() + BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2));
			}
		}
		
		// Draw "Solais: ###":
		int solaisLabelLeft = rightX - solaisTextTexture.getWidth();
		GLDrawer.useTexture(solaisTextTexture.getTextureId());
		GLDrawer.drawQuad(solaisLabelLeft, solaisLabelLeft + solaisTextTexture.getWidth(), topY - 20, topY);
		topY -= 24;
		
		// Draw item box outlines:
		GLDrawer.disableTextures();
		for (int i = 0; i < selectedQuickequipItemIndex; i++) {
			setColorForItem(i);
			GLDrawer.drawOutline(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, rightX,
					topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY);
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		setColorForItem(selectedQuickequipItemIndex);
		GLDrawer.drawOutline(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, selectedItemRightX,
				topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY);
		topY -= BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		for (int i = selectedQuickequipItemIndex + 1; i < INVENTORY_QUICKEQUIP_SLOTS; i++) {
			setColorForItem(i);
			GLDrawer.drawOutline(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, rightX,
					topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY);
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		
		// Draw items in boxes and item label if applicable:
		topY = Crissaegrim.getWindowHeight() - OUTER_PADDING_PIXELS - 24;
		for (int i = 0; i < selectedQuickequipItemIndex; i++) {
			if (!(items[i] instanceof ItemNothing)) {
				GLDrawer.useTexture(items[i].getTexture());
				GLDrawer.drawQuad(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, rightX - INNER_PADDING_PIXELS,
						topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
			}
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		if (!(items[selectedQuickequipItemIndex] instanceof ItemNothing)) {
			GLDrawer.useTexture(items[selectedQuickequipItemIndex].getTexture());
			GLDrawer.drawQuad(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, selectedItemRightX - INNER_PADDING_PIXELS,
					topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
			if (now - lastTouchedTime < MILLIS_AT_FULL_EXTENDED) {
				TextTexture selectedItemLabel = Crissaegrim.getCommonTextures().getTextTexture(items[selectedQuickequipItemIndex].getDisplayName());
				int selectedItemLabelLeft = selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2 - OUTER_PADDING_PIXELS - selectedItemLabel.getWidth();
				GLDrawer.useTexture(selectedItemLabel.getTextureId());
				GLDrawer.drawQuad(selectedItemLabelLeft, selectedItemLabelLeft + selectedItemLabel.getWidth(), topY - 45, topY - 25);
			}
		}
		topY -= BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		for (int i = selectedQuickequipItemIndex + 1; i < INVENTORY_QUICKEQUIP_SLOTS; i++) {
			if (!(items[i] instanceof ItemNothing)) {
				GLDrawer.useTexture(items[i].getTexture());
				GLDrawer.drawQuad(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, rightX - INNER_PADDING_PIXELS,
						topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
			}
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		
	}
	
	private void setColorForItem(int index) {
		if (!(items[index] instanceof ItemNothing)) {
			GLDrawer.clearColor();
		} else {
			GLDrawer.setColor(0.6, 0.6, 0.6);
		}
	}
	
	/**
	 * Determines if the {@link Inventory} is full or not.
	 * @return {@code true} if the Inventory has no more empty spaces, {@code false} otherwise
	 */
	public boolean isFull() {
		for (int i = getInventorySize() - 1; i >= 0; i--) {
			if (items[i] instanceof ItemNothing) { return false; }
		}
		return true;
	}
	
}
