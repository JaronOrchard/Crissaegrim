package players;

import static org.lwjgl.opengl.GL11.*;

import java.util.Date;

import textures.Textures;
import crissaegrim.Crissaegrim;
import items.Item;
import items.Weapon;

public class Inventory {
	
	private static final int BOX_SIZE_PIXELS = 32;
	private static final int INNER_PADDING_PIXELS = 4;
	private static final int OUTER_PADDING_PIXELS = 8;
	private final static long MILLIS_AT_FULL_EXTENDED = 2000;
	private final static long MILLIS_TO_SLIDE_RIGHT = 300;
	
	private static final int INVENTORY_SIZE = 8;
	private Item[] items;
	private int selectedItemIndex;
	private long lastTouchedTime = 0;
	
	public Inventory() {
		items = new Item[INVENTORY_SIZE];
		selectedItemIndex = 3;
		
		// --- Remove when Inventory is done:
				items[2] = new Weapon("Air Staff", Textures.CHUNK_NOT_FOUND);
				items[3] = new Weapon("Water Flask", Textures.KIKORI_D_FULL_L);
				items[4] = new Weapon("Space Sword", Textures.KIKORI_GRASS_SPRIG_2);
				items[6] = new Weapon("Fists", Textures.TOWER_OF_PRELUDES_1R_L);
	}
	
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
	
	public void draw() {
		long now = new Date().getTime();
		if (now - lastTouchedTime > MILLIS_AT_FULL_EXTENDED + MILLIS_TO_SLIDE_RIGHT) { return; } // Inventory is out of view
		int topY = Crissaegrim.getWindowHeight() - OUTER_PADDING_PIXELS;
		int rightX;
		if (now - lastTouchedTime < MILLIS_AT_FULL_EXTENDED) {
			rightX = Crissaegrim.getWindowWidth() - OUTER_PADDING_PIXELS;
		} else {
			double amtSlid = ((double)(now - lastTouchedTime - MILLIS_AT_FULL_EXTENDED)) / (double)MILLIS_TO_SLIDE_RIGHT;
			rightX = (int)((1.0 - amtSlid)*(Crissaegrim.getWindowWidth() - OUTER_PADDING_PIXELS) +
					amtSlid*(Crissaegrim.getWindowWidth() + BOX_SIZE_PIXELS*2 + INNER_PADDING_PIXELS*2));
		}
		
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
			glVertex2d(rightX, topY);
			glVertex2d(rightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY);
			glVertex2d(rightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
			glVertex2d(rightX, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS*2);
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
		
		// Draw items in boxes:
		topY = Crissaegrim.getWindowHeight() - OUTER_PADDING_PIXELS;
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
					glVertex2d(rightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS);
					glTexCoord2d(1, 1);
					glVertex2d(rightX - INNER_PADDING_PIXELS, topY - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS);
					glTexCoord2d(1, 0);
					glVertex2d(rightX - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
					glTexCoord2d(0, 0);
					glVertex2d(rightX - BOX_SIZE_PIXELS*2 - INNER_PADDING_PIXELS, topY - INNER_PADDING_PIXELS);
				glEnd();
			glPopMatrix();
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
