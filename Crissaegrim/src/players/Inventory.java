package players;

import static org.lwjgl.opengl.GL11.*;

import java.util.Date;

import textblock.TextTexture;
import textures.Textures;
import crissaegrim.Crissaegrim;
import items.Item;
import items.ItemPickaxe;
import items.Weapon;

public class Inventory {
	
	private static final int BOX_SIZE_PIXELS = 32;
	private static final int INNER_PADDING_PIXELS = 4;
	private static final int OUTER_PADDING_PIXELS = 8;
	private final static long MILLIS_AT_FULL_EXTENDED = 2000;
	private final static long MILLIS_TO_SLIDE_RIGHT = 300;
	
	private static final int INVENTORY_SIZE = 8;
	private TextTexture solaisTextTexture; // Update this whenever the user's amount of Solais changes
	
	private Item[] items;
	private int solais;
	private int solaisCountOnTextTexture; // Used to determine when the Solais count changes
	private int selectedItemIndex;
	private long lastTouchedTime = 0;
	
	public int getInventorySize() { return INVENTORY_SIZE; }
	public Item[] getItems() { return items; }
	
	public Inventory() {
		items = new Item[INVENTORY_SIZE];
		solais = 0;
		solaisCountOnTextTexture = 0;
		updateSolaisTextTexture();
		selectedItemIndex = 0;
		
		items[0] = new Weapon("Starter Sword", Textures.ITEM_STARTER_SWORD);
		items[1] = new ItemPickaxe("Rhichite", Textures.ITEM_RHICHITE_PICKAXE);
	}
	
	private void updateSolaisTextTexture() {
		solaisTextTexture = Crissaegrim.getCommonTextures().getTextTexture("Solais: " + Integer.toString(solais));
	}
	
	public Item getCurrentItem() { return items[selectedItemIndex]; }
	
	public void removeCurrentItem() { items[selectedItemIndex] = null; }
	
	public void selectPreviousItem() {
		selectedItemIndex = (selectedItemIndex + INVENTORY_SIZE - 1) % INVENTORY_SIZE;
		updateLastTouchedTime();
	}
	public void selectNextItem() {
		selectedItemIndex = (selectedItemIndex + 1) % INVENTORY_SIZE;
		updateLastTouchedTime();
	}
	public void selectSpecificItem(int index) {
		if (index < 0) {
			selectedItemIndex = 0;
		} else if (index >= INVENTORY_SIZE) {
			selectedItemIndex = INVENTORY_SIZE - 1;
		} else {
			selectedItemIndex = index;
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
		if (items[selectedItemIndex] == null && now - lastTouchedTime > MILLIS_AT_FULL_EXTENDED + MILLIS_TO_SLIDE_RIGHT) {
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
			if (items[selectedItemIndex] == null) {
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
		for (int i = 0; i < selectedItemIndex; i++) {
			setGlColorForItem(i);
			glBegin(GL_LINE_LOOP);
				glVertex2d(rightX, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY);
				glVertex2d(rightX - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
				glVertex2d(rightX, topY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
			glEnd();
			topY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		setGlColorForItem(selectedItemIndex);
		glBegin(GL_LINE_LOOP);
			glVertex2d(selectedItemRightX, topY);
			glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY);
			glVertex2d(selectedItemRightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
			glVertex2d(selectedItemRightX, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
		glEnd();
		topY -= BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		for (int i = selectedItemIndex + 1; i < INVENTORY_SIZE; i++) {
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
		for (int i = 0; i < selectedItemIndex; i++) {
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
		if (items[selectedItemIndex] != null) {
			glBindTexture(GL_TEXTURE_2D, items[selectedItemIndex].getTexture());
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
				TextTexture selectedItemLabel = Crissaegrim.getCommonTextures().getTextTexture(items[selectedItemIndex].getName());
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
		for (int i = selectedItemIndex + 1; i < INVENTORY_SIZE; i++) {
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
