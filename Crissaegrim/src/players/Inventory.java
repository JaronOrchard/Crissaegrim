package players;

import static org.lwjgl.opengl.GL11.*;

import java.util.Date;

import textblock.TextTexture;
import textures.Textures;
import crissaegrim.Crissaegrim;
import items.Item;
import items.ItemPickaxe;
import items.Items;
import items.Weapon;

public class Inventory {
	
	private static final int BOX_SIZE_PIXELS = 32;
	private static final int INNER_PADDING_PIXELS = 4;
	private static final int OUTER_PADDING_PIXELS = 8;
	private final static long MILLIS_AT_FULL_EXTENDED = 2000;
	private final static long MILLIS_TO_SLIDE_RIGHT = 300;
	
	private static final int INVENTORY_QUICKEQUIP_SLOTS = 6;
	private static final int INVENTORY_SIZE = 30;
	private TextTexture solaisTextTexture; // Update this whenever the user's amount of Solais changes
	
	private Item[] items;
	private int solais;
	private int solaisCountOnTextTexture; // Used to determine when the Solais count changes
	private int selectedQuickequipItemIndex; // Only goes from 0 to INVENTORY_QUICKEQUIP_SLOTS-1
	private long lastTouchedTime = 0;
	
	public int getInventorySize() { return INVENTORY_SIZE; }
	public Item[] getItems() { return items; }
	public Item getItem(int index) { return items[index]; }
	
	public Inventory() {
		items = new Item[INVENTORY_SIZE];
		solais = 0;
		solaisCountOnTextTexture = 0;
		updateSolaisTextTexture();
		selectedQuickequipItemIndex = 0;
		
		items[0] = new Weapon("Rhichite Sword", Textures.ITEM_RHICHITE_SWORD);
		items[1] = new ItemPickaxe("Rhichite", Textures.ITEM_RHICHITE_PICKAXE);
		items[8] = Items.rhichiteOre();
		items[15] = Items.valeniteOre();
		items[19] = Items.sandelugeOre();
	}
	
	private void updateSolaisTextTexture() {
		solaisTextTexture = Crissaegrim.getCommonTextures().getTextTexture("Solais: " + Integer.toString(solais));
	}
	
	public Item getCurrentItem() { return items[selectedQuickequipItemIndex]; }
	
	public void removeCurrentItem() { items[selectedQuickequipItemIndex] = null; }
	
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
	
	public void addSolais(int amount) { solais += amount; }
	
	/**
	 * Adds the {@link Item} to the Inventory.  If the Inventory is full, returns {@code false}.
	 * @param item The {@link Item} to add to the Inventory
	 * @return {@code true} if the Item was added, {@code false} otherwise
	 */
	public boolean addItem(Item item) {
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public void draw() {
		long now = new Date().getTime();
		if (items[selectedQuickequipItemIndex] == null && now - lastTouchedTime > MILLIS_AT_FULL_EXTENDED + MILLIS_TO_SLIDE_RIGHT) {
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
			if (items[selectedQuickequipItemIndex] == null) {
				selectedItemRightX = (int)((1.0 - amtSlid)*(selectedItemRightX) +
						amtSlid*(Crissaegrim.getWindowWidth() + BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2));
			}
		}
		
		// Draw "Solais: ###":
		glColor3d(1, 1, 1);
		int solaisLabelLeft = rightX - solaisTextTexture.getWidth();
		glBindTexture(GL_TEXTURE_2D, solaisTextTexture.getTextureId());
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(solaisLabelLeft, topY - 20);
				glTexCoord2d(1, 1);
				glVertex2d(solaisLabelLeft + solaisTextTexture.getWidth(), topY - 20);
				glTexCoord2d(1, 0);
				glVertex2d(solaisLabelLeft + solaisTextTexture.getWidth(), topY);
				glTexCoord2d(0, 0);
				glVertex2d(solaisLabelLeft, topY);
			glEnd();
		glPopMatrix();
		topY -= 24;
		
		// Draw item box outlines:
		glDisable(GL_TEXTURE_2D);
		for (int i = 0; i < selectedQuickequipItemIndex; i++) {
			setGlColorForItem(i);
			glBegin(GL_LINE_LOOP);
				glVertex2d(rightX, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
				glVertex2d(rightX, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
			glEnd();
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		setGlColorForItem(selectedQuickequipItemIndex);
		glBegin(GL_LINE_LOOP);
			glVertex2d(selectedItemRightX, topY);
			glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY);
			glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
			glVertex2d(selectedItemRightX, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
		glEnd();
		topY -= BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		for (int i = selectedQuickequipItemIndex + 1; i < INVENTORY_QUICKEQUIP_SLOTS; i++) {
			setGlColorForItem(i);
			glBegin(GL_LINE_LOOP);
				glVertex2d(rightX, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
				glVertex2d(rightX, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
			glEnd();
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		glEnable(GL_TEXTURE_2D);
		
		glColor3d(1, 1, 1);
		// Draw items in boxes and item label if applicable:
		topY = Crissaegrim.getWindowHeight() - OUTER_PADDING_PIXELS - 24;
		for (int i = 0; i < selectedQuickequipItemIndex; i++) {
			if (items[i] != null) {
				glBindTexture(GL_TEXTURE_2D, items[i].getTexture());
				glPushMatrix();
					glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
					glBegin(GL_QUADS);
						glTexCoord2d(0, 1);
						glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
						glTexCoord2d(1, 1);
						glVertex2d(rightX - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
						glTexCoord2d(1, 0);
						glVertex2d(rightX - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
						glTexCoord2d(0, 0);
						glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
					glEnd();
				glPopMatrix();
			}
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		if (items[selectedQuickequipItemIndex] != null) {
			glBindTexture(GL_TEXTURE_2D, items[selectedQuickequipItemIndex].getTexture());
			glPushMatrix();
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS);
					glTexCoord2d(1, 1);
					glVertex2d(selectedItemRightX - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS);
					glTexCoord2d(1, 0);
					glVertex2d(selectedItemRightX - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
					glTexCoord2d(0, 0);
					glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
				glEnd();
			glPopMatrix();
			if (now - lastTouchedTime < MILLIS_AT_FULL_EXTENDED) {
				TextTexture selectedItemLabel = Crissaegrim.getCommonTextures().getTextTexture(items[selectedQuickequipItemIndex].getName());
				int selectedItemLabelLeft = selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2 - OUTER_PADDING_PIXELS - selectedItemLabel.getWidth();
				glBindTexture(GL_TEXTURE_2D, selectedItemLabel.getTextureId());
				glPushMatrix();
					glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
					glBegin(GL_QUADS);
						glTexCoord2d(0, 1);
						glVertex2d(selectedItemLabelLeft, topY - 45);
						glTexCoord2d(1, 1);
						glVertex2d(selectedItemLabelLeft + selectedItemLabel.getWidth(), topY - 45);
						glTexCoord2d(1, 0);
						glVertex2d(selectedItemLabelLeft + selectedItemLabel.getWidth(), topY - 25);
						glTexCoord2d(0, 0);
						glVertex2d(selectedItemLabelLeft, topY - 25);
					glEnd();
				glPopMatrix();
			}
		}
		topY -= BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		for (int i = selectedQuickequipItemIndex + 1; i < INVENTORY_QUICKEQUIP_SLOTS; i++) {
			if (items[i] != null) {
				glBindTexture(GL_TEXTURE_2D, items[i].getTexture());
				glPushMatrix();
					glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
					glBegin(GL_QUADS);
						glTexCoord2d(0, 1);
						glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
						glTexCoord2d(1, 1);
						glVertex2d(rightX - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
						glTexCoord2d(1, 0);
						glVertex2d(rightX - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
						glTexCoord2d(0, 0);
						glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
					glEnd();
				glPopMatrix();
			}
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		
	}
	
	private void setGlColorForItem(int index) {
		if (items[index] != null) {
			glColor3d(1, 1, 1);
		} else {
			glColor3d(0.6, 0.6, 0.6);
		}
	}
	
}
