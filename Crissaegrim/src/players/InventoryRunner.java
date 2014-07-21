package players;

import static org.lwjgl.opengl.GL11.*;
import items.Item;
import items.LocalDroppedItem;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import textblock.TextTexture;
import textures.Textures;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;
import crissaegrim.GameRunner;
import entities.EntityMovementHelper;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;
import gldrawer.GLDrawer;

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
				
				if (!Crissaegrim.connectionStable) { // Lost connection to server
					Crissaegrim.getValmanwayConnection().closeConnections();
					Crissaegrim.getGameRunner().displayMessageForever(Textures.LOST_CONNECTION_MESSAGE, 423, 64, "Connection lost - Please restart");
					return;
				}
				
				player.update(); // (Don't negate gravity)
				gameRunner.drawScene();
				
				int hoveredItemIndex = getHoveredItemIndex();
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput(false);
				} else {
					getKeyboardInput(hoveredItemIndex);
				}
				if (closeInventory) { // Close Inventory if requested
					if (heldItem != null) {
						dropItem(heldItem);
						heldItem = null;
					}
					return;
				}
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
	private void getKeyboardInput(int hoveredItemIndex) {
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
				} else if (pressedKey == Keyboard.KEY_Q) {		// Q key: Drop heldItem or hovered item
					Inventory inventory = Crissaegrim.getPlayer().getInventory();
					if (heldItem != null) {
						dropItem(heldItem);
						heldItem = null;
					} else if (hoveredItemIndex != -1 && inventory.getItem(hoveredItemIndex) != null) {
						dropItem(inventory.getItem(hoveredItemIndex));
						inventory.setItem(hoveredItemIndex, null);
					}
				} else if (pressedKey == Keyboard.KEY_ESCAPE ||
						pressedKey == Keyboard.KEY_E) {			// E / Escape: Close inventory
					closeInventory = true;
				}
			}
		}
	}
	
	private void dropItem(Item item) {
		Player player = Crissaegrim.getPlayer();
		Crissaegrim.getGameRunner().addLocalDroppedItem(new LocalDroppedItem(
						item,
						new Coordinate(player.getPosition().getX(), player.getPosition().getY() + (player.getEntireHeight() / 2)),
						Crissaegrim.getCurrentBoard(),
						player.getFacingRight()));
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
		GLDrawer.useTexture(mouseHoverStatus.getTextureId());
		GLDrawer.drawQuad(5, 5 + mouseHoverStatus.getWidth(), top - 20, top);
	}
	
	private void drawInventory(Inventory inventory) {
		GameInitializer.initializeNewFrameForWindow();
		GLDrawer.disableTextures();
		
		GLDrawer.setColor(0.314, 0.314, 0.314);
		GLDrawer.drawQuad(inventoryRect); // Draw inventory background
		GLDrawer.setColor(0.75, 0.75, 0.75);
		GLDrawer.setLineWidth(3);
		GLDrawer.drawOutline(inventoryRect); // Draw inventory outline
		GLDrawer.setLineWidth(1);
		glBegin(GL_LINES); // Draw lines
			glVertex2d(inventoryRect.getLeft() + 6, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(inventoryRect.getRight() - 6, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			
			glVertex2d(inventoryRect.getLeft() + 248, inventoryRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(inventoryRect.getLeft() + 248, inventoryRect.getBottom() + 6);
		glEnd();
		
		TextTexture headerLabel = Crissaegrim.getCommonTextures().getTextTexture("Inventory (Click items to move them, E to close)");
		GLDrawer.useTexture(headerLabel.getTextureId());
		GLDrawer.drawQuad(inventoryRect.getLeft() + OUTER_PADDING_PIXELS, inventoryRect.getLeft() + OUTER_PADDING_PIXELS + headerLabel.getWidth(),
				inventoryRect.getTop() - OUTER_PADDING_PIXELS - 20, inventoryRect.getTop() - OUTER_PADDING_PIXELS);
		
		// Draw "Armor slots coming in future release" message
		TextTexture armorslotsLabel1 = Crissaegrim.getCommonTextures().getTextTexture("(Armor slots coming");
		TextTexture armorslotsLabel2 = Crissaegrim.getCommonTextures().getTextTexture("in future release!)");
		GLDrawer.useTexture(armorslotsLabel1.getTextureId());
		GLDrawer.setColor(0.8, 0.8, 0.8);
		GLDrawer.drawQuad(inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS,
				inventoryRect.getLeft() + 248 + OUTER_PADDING_PIXELS + armorslotsLabel1.getWidth(),
				inventoryRect.getTop() - 180, inventoryRect.getTop() - 160);
		GLDrawer.useTexture(armorslotsLabel2.getTextureId());
		GLDrawer.setColor(0.8, 0.8, 0.8);
		int nudge = (armorslotsLabel1.getWidth() - armorslotsLabel2.getWidth()) / 2;
		GLDrawer.drawQuad(inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS,
				inventoryRect.getLeft() + 248 + nudge + OUTER_PADDING_PIXELS + armorslotsLabel2.getWidth(),
				inventoryRect.getTop() - 203, inventoryRect.getTop() - 183);
		
		// Draw background and outline of all item boxes:
		int posX, posY;
		posX = (int)(inventoryRect.getLeft()) + OUTER_PADDING_PIXELS;
		for (int i = 0; i < 5; i++) {
			posY = (int)(inventoryRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
			for (int j = 0; j < 6; j++) {
				Item item = inventory.getItem(i*6 + j);
				
				setColorForItemBoxBackground(item);
				GLDrawer.disableTextures();
				GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				
				setGlColorForItemBoxOutline(i == 0, item);
				GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2,
						posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				
				if (item != null) {
					GLDrawer.useTexture(item.getTexture());
					GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
							posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
				}
				posY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
			}
			posX += BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		
		// Draw held item
		if (heldItem != null) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			GLDrawer.useTexture(heldItem.getTexture());
			GLDrawer.drawQuad(mouseX - 16, mouseX + 16, mouseY - 16, mouseY + 16);
		}
	}
	
	private void setColorForItemBoxBackground(Item item) {
		if (item != null) {
			GLDrawer.setColor(0.122, 0.141, 0.161);
		} else {
			GLDrawer.setColor(0.22, 0.22, 0.22);
		}
	}
	
	private void setGlColorForItemBoxOutline(boolean quickequipColumn, Item item) {
		if (quickequipColumn) {
			if (item != null) {
				GLDrawer.setColor(0.733, 0.953, 1);
			} else {
				GLDrawer.setColor(0.518, 0.659, 0.682);
			}
		} else {
			if (item != null) {
				GLDrawer.setColor(1, 1, 1);
			} else {
				GLDrawer.setColor(0.6, 0.6, 0.6);
			}
		}
	}
	
}
