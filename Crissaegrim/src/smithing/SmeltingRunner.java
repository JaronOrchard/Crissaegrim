package smithing;

import static org.lwjgl.opengl.GL11.*;
import items.Item;
import items.LocalDroppedItem;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import players.Inventory;
import players.Player;
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

public class SmeltingRunner {
	
	private boolean closeSmeltingRunner;
	
	private static transient final int BOX_SIZE_PIXELS = 64;
	private static transient final int INNER_PADDING_PIXELS = 4;
	private static transient final int OUTER_PADDING_PIXELS = 8;
	private Rect smeltingDialogRect;
	
	public SmeltingRunner() {
		closeSmeltingRunner = false;
		recalculateDialogDimensions();
	}
	
	public void run() {
		GameRunner gameRunner = Crissaegrim.getGameRunner();
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		Inventory inventory = player.getInventory();
		GameInitializer.setWindowTitle("Smelting ores");
		
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
				
//				int hoveredItemIndex = getHoveredItemIndex();
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput(false);
				} else {
					getKeyboardInput();
				}
				if (closeSmeltingRunner) { // Close dialog if requested
					return;
				}
//				drawMouseHoverStatus(hoveredItemIndex);
//				while (Mouse.next()) {
//					if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
//						if (hoveredItemIndex != -1) {
//							Item itemInSpot = inventory.getItem(hoveredItemIndex);
//							inventory.setItem(hoveredItemIndex, heldItem);
//							heldItem = itemInSpot;
//						}
//					}
//				}
				
				playerMovementHelper.moveEntity();
				
				gameRunner.drawHUD();
				drawSmeltingDialog(inventory);
				
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
	
	private void recalculateDialogDimensions() {
		int dialogBoxWidth = 365;
		int dialogBoxHeight = 204;
		int widthBuffer = (Crissaegrim.getWindowWidth() - dialogBoxWidth) / 2;
		int heightBuffer = (Crissaegrim.getWindowHeight() - dialogBoxHeight) / 2;
		smeltingDialogRect = new Rect(
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
					recalculateDialogDimensions();
				} else if (pressedKey == Keyboard.KEY_ESCAPE) {	// Escape: Close smelting dialog
					closeSmeltingRunner = true;
				}
			}
		}
	}
	
	private void drawSmeltingDialog(Inventory inventory) {
		GameInitializer.initializeNewFrameForWindow();
		GLDrawer.disableTextures();
		
		GLDrawer.setColor(0.314, 0.314, 0.314);
		GLDrawer.drawQuad(smeltingDialogRect); // Draw smelting dialog background
		// Draw blue gradient title bar rectangle:
		glBegin(GL_QUADS);
		GLDrawer.setColor(0, 0.188, 0.502);
			glVertex2d(smeltingDialogRect.getLeft(), smeltingDialogRect.getTop());
			glVertex2d(smeltingDialogRect.getRight(), smeltingDialogRect.getTop());
			GLDrawer.setColor(0, 0.251, 0.667);
			glVertex2d(smeltingDialogRect.getRight(), smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(smeltingDialogRect.getLeft(), smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS - 25);
		glEnd();
		GLDrawer.setColor(0.75, 0.75, 0.75);
		GLDrawer.setLineWidth(3);
		GLDrawer.drawOutline(smeltingDialogRect); // Draw smelting dialog outline
		GLDrawer.setLineWidth(1);
		
		TextTexture headerLabel = Crissaegrim.getCommonTextures().getTextTexture("Smelting ores");
		GLDrawer.useTexture(headerLabel.getTextureId());
		GLDrawer.drawQuad(smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS, smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS + headerLabel.getWidth(),
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS - 20, smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS);
		
		// Draw clickable boxes:
		int posX, posY;
		posX = (int)(smeltingDialogRect.getLeft()) + OUTER_PADDING_PIXELS;
		posY = (int)(smeltingDialogRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
		for (int i = 0; i < 2; i++) {
			GLDrawer.disableTextures();
			GLDrawer.setColor(0.122, 0.141, 0.161);
			GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
			GLDrawer.setColor(1, 1, 1);
			GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
			if (i == 0) {
				GLDrawer.useTexture(Textures.ITEM_RHICHITE_BAR);
			} else if (i == 1) {
				GLDrawer.useTexture(Textures.ITEM_VAL_SAN_BAR);
			}
			GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
					posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
			posY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		
		// Draw text:
		TextTexture requiresLabel = Crissaegrim.getCommonTextures().getTextTexture("- Requires:");
		TextTexture plusLabel = Crissaegrim.getCommonTextures().getTextTexture("+");
		GLDrawer.useTexture(requiresLabel.getTextureId());
		GLDrawer.drawQuad(
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS,
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + requiresLabel.getWidth(),
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*2 - 71,
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*2 - 51);
		GLDrawer.drawQuad(
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS,
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + requiresLabel.getWidth(),
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 71,
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 51);
		GLDrawer.useTexture(plusLabel.getTextureId());
		GLDrawer.drawQuad(
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*4 + INNER_PADDING_PIXELS*4 + BOX_SIZE_PIXELS*2 + requiresLabel.getWidth(),
				smeltingDialogRect.getLeft() + OUTER_PADDING_PIXELS*4 + INNER_PADDING_PIXELS*4 + BOX_SIZE_PIXELS*2 + requiresLabel.getWidth() + plusLabel.getWidth(),
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 71,
				smeltingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 51);
		
		// Draw required ores:
		posX = (int)(smeltingDialogRect.getLeft()) + 186;
		posY = (int)(smeltingDialogRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.122, 0.141, 0.161);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.setColor(1, 1, 1);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_RHICHITE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		posX = (int)(smeltingDialogRect.getLeft()) + 186;
		posY = (int)(smeltingDialogRect.getTop()) - OUTER_PADDING_PIXELS*3 - 25 - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2;
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.122, 0.141, 0.161);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.setColor(1, 1, 1);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_VALENITE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		posX = (int)(smeltingDialogRect.getLeft()) + 184 + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + plusLabel.getWidth();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.122, 0.141, 0.161);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.setColor(1, 1, 1);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_SANDELUGE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		
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
