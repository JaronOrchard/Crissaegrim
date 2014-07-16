package players;

import static org.lwjgl.opengl.GL11.*;
import items.Item;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import textblock.TextTexture;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;
import crissaegrim.GameRunner;
import entities.EntityMovementHelper;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;

public class InventoryRunner {
	
	private boolean closeInventory;
	private Item heldItem;
	
	private static transient final int BOX_SIZE_PIXELS = 32;
	private static transient final int INNER_PADDING_PIXELS = 4;
	private static transient final int OUTER_PADDING_PIXELS = 8;
	private Rect inventoryRect;
	
	public InventoryRunner() {
		closeInventory = false;
		heldItem = null;
		recalculateInventoryDimensions();
	}
	
	public void run() {
		GameRunner gameRunner = Crissaegrim.getGameRunner();
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		Inventory inventory = player.getInventory();
		GameInitializer.setWindowTitle("Inventory");
		
		try {
			long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
			while (!Display.isCloseRequested()) {
				startTime = Thunderbrand.getTime();
				
				if (!Crissaegrim.connectionStable) { break; }
				
				player.update(); // (Don't negate gravity)
				gameRunner.drawScene();
				
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput(false);
				} else {
					getKeyboardInput();
				}
				if (closeInventory) { return; } // Close Inventory if requested
				int hoveredItemIndex = getHoveredItemIndex();
				drawMouseHoverStatus(hoveredItemIndex);
				while (Mouse.next()) {
					if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
						if (hoveredItemIndex != -1) {
							Item itemInSpot = inventory.getItem(hoveredItemIndex);
							inventory.setItem(hoveredItemIndex, heldItem);
							heldItem = itemInSpot;
						}
					}
				}
				
				playerMovementHelper.moveEntity();
				
				gameRunner.drawHUD();
				drawInventory(inventory);
				
				// Still transmit data to the server
				Crissaegrim.getValmanwayConnection().sendPlayerStatus();
					
				Display.update();
				endTime = Thunderbrand.getTime();
				elaspedTime = endTime - startTime;
				Thread.sleep(Math.max(0, GameRunner.MILLISECONDS_PER_FRAME - elaspedTime));
			}
			
			Display.destroy();
			Crissaegrim.getValmanwayConnection().closeConnections();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	private void recalculateInventoryDimensions() {
		int inventoryBoxWidth = 423;
		int inventoryBoxHeight = 330;
		int widthBuffer = (Crissaegrim.getWindowWidth() - inventoryBoxWidth) / 2;
		int heightBuffer = (Crissaegrim.getWindowHeight() - inventoryBoxHeight) / 2;
		inventoryRect = new Rect(
				new Coordinate(widthBuffer, heightBuffer),
				new Coordinate(Crissaegrim.getWindowWidth() - widthBuffer, Crissaegrim.getWindowHeight() - heightBuffer));
	}
	
	/**
	 * Detects keyboard input and reacts accordingly.
	 */
	private void getKeyboardInput() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { // Key was pressed (not released)
				int pressedKey = Keyboard.getEventKey();
				if (pressedKey == Keyboard.KEY_T ||
						pressedKey == Keyboard.KEY_RETURN) {	// T or Enter: Enter chat mode
					Crissaegrim.getChatBox().enableTypingMode();
					return; // Don't process any more keys!
				} else if (pressedKey == 41) {					// Backtick (`) key
					Crissaegrim.toggleDebugMode();
				} else if (pressedKey == Keyboard.KEY_TAB) {	// Tab key: Toggle zoom
					Crissaegrim.toggleZoom();
				} else if (pressedKey == Keyboard.KEY_M) {		// M key: Toggle window size
					Crissaegrim.toggleWindowSize();
					recalculateInventoryDimensions();
				} else if (pressedKey == Keyboard.KEY_ESCAPE ||
						pressedKey == Keyboard.KEY_E) {			// E / Escape: Close inventory
					closeInventory = true;
				}
			}
		}
	}
	
	/**
	 * @return The index of the inventory item the mouse is currently hovering over, or -1 if no item is being hovered over
	 */
	private int getHoveredItemIndex() {
		Coordinate mouseCoords = new Coordinate(Mouse.getX(), Mouse.getY());
		int posX, posY;
		posX = (int)(inventoryRect.getLeft()) + OUTER_PADDING_PIXELS;
		for (int i = 0; i < 5; i++) {
			posY = (int)(inventoryRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
			for (int j = 0; j < 6; j++) {
				Rect itemBoxRect = new Rect(
						new Coordinate(posX, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2),
						new Coordinate(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY));
				if (RectUtils.coordinateIsInRect(mouseCoords, itemBoxRect)) {
					return (i*6 + j);
				}
				posY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
			}
			posX += BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		return -1;
	}
	
	private void drawMouseHoverStatus(int hoveredItemIndex) {
		Inventory inventory = Crissaegrim.getPlayer().getInventory();
		if (hoveredItemIndex == -1 || (inventory.getItem(hoveredItemIndex) == null && heldItem == null)) {
			return; // Nothing to display
		}
		String mouseHoverString;
		if (heldItem == null) {
			mouseHoverString = "Grab " + inventory.getItem(hoveredItemIndex).getName();
		} else if (inventory.getItem(hoveredItemIndex) == null) {
			mouseHoverString = "Place " + heldItem.getName() + " in empty slot";
		} else {
			mouseHoverString = "Swap " + inventory.getItem(hoveredItemIndex).getName() + " with " + heldItem.getName();
		}
		TextTexture mouseHoverStatus = Crissaegrim.getCommonTextures().getTextTexture(mouseHoverString);
		int top = Crissaegrim.getWindowHeight() - 5;
		glBindTexture(GL_TEXTURE_2D, mouseHoverStatus.getTextureId());
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(5, top - 20);
				glTexCoord2d(1, 1);
				glVertex2d(5 + mouseHoverStatus.getWidth(), top - 20);
				glTexCoord2d(1, 0);
				glVertex2d(5 + mouseHoverStatus.getWidth(), top);
				glTexCoord2d(0, 0);
				glVertex2d(5, top);
			glEnd();
		glPopMatrix();
	}
	
	private void drawInventory(Inventory inventory) {
		GameInitializer.initializeNewFrameForWindow();
		glDisable(GL_TEXTURE_2D);
		
		glColor3d(0.314, 0.314, 0.314);
		glBegin(GL_QUADS); // Draw inventory background
			glVertex2d(inventoryRect.getLeft(), inventoryRect.getTop());
			glVertex2d(inventoryRect.getRight(), inventoryRect.getTop());
			glVertex2d(inventoryRect.getRight(), inventoryRect.getBottom());
			glVertex2d(inventoryRect.getLeft(), inventoryRect.getBottom());
		glEnd();
		glColor3d(0.75, 0.75, 0.75);
		glLineWidth(3);
		glBegin(GL_LINE_LOOP); // Draw inventory outline
			glVertex2d(inventoryRect.getLeft(), inventoryRect.getTop());
			glVertex2d(inventoryRect.getRight(), inventoryRect.getTop());
			glVertex2d(inventoryRect.getRight(), inventoryRect.getBottom());
			glVertex2d(inventoryRect.getLeft(), inventoryRect.getBottom());
		glEnd();
		glLineWidth(1);
		glBegin(GL_LINES); // Draw lines
			glVertex2d(inventoryRect.getLeft() + 6, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(inventoryRect.getRight() - 6, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			
			glVertex2d(inventoryRect.getLeft() + 248, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(inventoryRect.getLeft() + 248, inventoryRect.getBottom() + 6);
		glEnd();
		
		glEnable(GL_TEXTURE_2D);
		glColor3d(1, 1, 1);
		TextTexture headerLabel = Crissaegrim.getCommonTextures().getTextTexture("Inventory (Click items to move them, E to close)");
		glBindTexture(GL_TEXTURE_2D, headerLabel.getTextureId());
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(inventoryRect.getLeft() + OUTER_PADDING_PIXELS, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 20);
				glTexCoord2d(1, 1);
				glVertex2d(inventoryRect.getLeft() + OUTER_PADDING_PIXELS + headerLabel.getWidth(), inventoryRect.getTop() - OUTER_PADDING_PIXELS - 20);
				glTexCoord2d(1, 0);
				glVertex2d(inventoryRect.getLeft() + OUTER_PADDING_PIXELS + headerLabel.getWidth(), inventoryRect.getTop() - OUTER_PADDING_PIXELS);
				glTexCoord2d(0, 0);
				glVertex2d(inventoryRect.getLeft() + OUTER_PADDING_PIXELS, inventoryRect.getTop() - OUTER_PADDING_PIXELS);
			glEnd();
		glPopMatrix();
		
		// Draw "Armor slots coming in future release" message
		glColor3d(0.8, 0.8, 0.8);
		TextTexture armorslotsLabel1 = Crissaegrim.getCommonTextures().getTextTexture("(Armor slots coming");
		TextTexture armorslotsLabel2 = Crissaegrim.getCommonTextures().getTextTexture("in future release!)");
		glBindTexture(GL_TEXTURE_2D, armorslotsLabel1.getTextureId());
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS, inventoryRect.getTop() - 180);
				glTexCoord2d(1, 1);
				glVertex2d(inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS + armorslotsLabel1.getWidth(), inventoryRect.getTop() - 180);
				glTexCoord2d(1, 0);
				glVertex2d(inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS + armorslotsLabel1.getWidth(), inventoryRect.getTop() - 160);
				glTexCoord2d(0, 0);
				glVertex2d(inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS, inventoryRect.getTop() - 160);
			glEnd();
		glPopMatrix();
		glBindTexture(GL_TEXTURE_2D, armorslotsLabel2.getTextureId());
		int nudge = (armorslotsLabel1.getWidth() - armorslotsLabel2.getWidth()) / 2;
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS, inventoryRect.getTop() - 203);
				glTexCoord2d(1, 1);
				glVertex2d(inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS + armorslotsLabel2.getWidth(), inventoryRect.getTop() - 203);
				glTexCoord2d(1, 0);
				glVertex2d(inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS + armorslotsLabel2.getWidth(), inventoryRect.getTop() - 183);
				glTexCoord2d(0, 0);
				glVertex2d(inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS, inventoryRect.getTop() - 183);
			glEnd();
		glPopMatrix();
		
		// Draw background and outline of all item boxes:
		int posX, posY;
		posX = (int)(inventoryRect.getLeft()) + OUTER_PADDING_PIXELS;
		for (int i = 0; i < 5; i++) {
			posY = (int)(inventoryRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
			for (int j = 0; j < 6; j++) {
				Item item = inventory.getItem(i*6 + j);
				
				setGlColorForItemBoxBackground(item);
				glDisable(GL_TEXTURE_2D);
				glBegin(GL_QUADS);
					glVertex2d(posX, posY);
					glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY);
					glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
					glVertex2d(posX, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
				glEnd();
				
				setGlColorForItemBoxOutline(i == 0, item);
				glBegin(GL_LINE_LOOP);
					glVertex2d(posX, posY);
					glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY);
					glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
					glVertex2d(posX, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2);
				glEnd();
				
				if (item != null) {
					glColor3d(1, 1, 1);
					glEnable(GL_TEXTURE_2D);
					glBindTexture(GL_TEXTURE_2D, item.getTexture());
					glPushMatrix();
						glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
						glBegin(GL_QUADS);
							glTexCoord2d(0, 1);
							glVertex2d(posX + INNER_PADDING_PIXELS, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
							glTexCoord2d(1, 1);
							glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS);
							glTexCoord2d(1, 0);
							glVertex2d(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
							glTexCoord2d(0, 0);
							glVertex2d(posX + INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
						glEnd();
					glPopMatrix();
				}
				
				posY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
			}
			posX += BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		glEnable(GL_TEXTURE_2D);
		glColor3d(1, 1, 1);
		
		// Draw held item
		if (heldItem != null) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			glBindTexture(GL_TEXTURE_2D, heldItem.getTexture());
			glPushMatrix();
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(mouseX - 16, mouseY - 16);
					glTexCoord2d(1, 1);
					glVertex2d(mouseX + 16, mouseY - 16);
					glTexCoord2d(1, 0);
					glVertex2d(mouseX + 16, mouseY + 16);
					glTexCoord2d(0, 0);
					glVertex2d(mouseX - 16, mouseY + 16);
				glEnd();
			glPopMatrix();
		}
	}
	
	private void setGlColorForItemBoxBackground(Item item) {
		if (item != null) {
			glColor3d(0.122, 0.141, 0.161);
		} else {
			glColor3d(0.22, 0.22, 0.22);
		}
	}
	
	private void setGlColorForItemBoxOutline(boolean quickequipColumn, Item item) {
		if (quickequipColumn) {
			if (item != null) {
				glColor3d(0.733, 0.953, 1);
			} else {
				glColor3d(0.518, 0.659, 0.682);
			}
		} else {
			if (item != null) {
				glColor3d(1, 1, 1);
			} else {
				glColor3d(0.6, 0.6, 0.6);
			}
		}
	}
	
}
