package smithing;

import static org.lwjgl.opengl.GL11.*;
import items.Item;
import items.ItemOre;
import items.Items;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import busy.SmeltingOreBusy;
import players.Inventory;
import players.InventoryUtils;
import players.Player;
import textblock.TextTexture;
import textures.Textures;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;
import crissaegrim.GameRunner;
import dialogboxes.DialogBox;
import dialogboxes.DialogBoxRunner;
import entities.EntityMovementHelper;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;
import gldrawer.GLDrawer;

public class SmithingRunner {
	
	
	// --- SmithingRunner is not yet ready
	// --- Going to come back to this after Weapons and Throwing Knives and Stackable Items have been added
	
	
	private boolean closeSmithingRunner;
	private boolean showingDialog;
	private int smithingItemType;
	@SuppressWarnings("unused")
	private String smithingOreType;
	private long lastSmithTime;
	
	private boolean hasRhichiteOre;
	private boolean hasValeniteOre;
	private boolean hasSandelugeOre;
	
	private static transient final long SMITH_ITEM_DELAY_MILLIS = 1500;
	private static transient final int BOX_SIZE_PIXELS = 64;
	private static transient final int INNER_PADDING_PIXELS = 4;
	private static transient final int OUTER_PADDING_PIXELS = 8;
	private Rect smithingDialogRect;
	
	@SuppressWarnings("unused")
	private class SmithableItem {
		String name;
		TextTexture label;
		List<Integer> textures; // per ore type
		// Item itemToReceive;
		
		// SET UP SMITHABLE ITEMS
		
	}
	
	public SmithingRunner() {
		closeSmithingRunner = false;
		showingDialog = true;
		smithingItemType = -1;
		smithingOreType = "";
		lastSmithTime = -1;
		hasRhichiteOre = InventoryUtils.containsOre("Rhichite");
		hasValeniteOre = InventoryUtils.containsOre("Valenite");
		hasSandelugeOre = InventoryUtils.containsOre("Sandeluge");
		recalculateDialogDimensions();
	}
	
	public void run() {
		GameRunner gameRunner = Crissaegrim.getGameRunner();
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		GameInitializer.setWindowTitle("Smithing items");
		
		DialogBoxRunner dbr = new DialogBoxRunner();
		DialogBox.Result res = dbr.run(new DialogBox(
				"Which type of bar do you want to smith items from?",
				Arrays.asList("Rhichite", "Tameike", "I don't want to smith anything")));
		if (res == DialogBox.Result.BUTTON_1) { smithingOreType = "Rhichite"; }
		else if (res == DialogBox.Result.BUTTON_2) { smithingOreType = "Tameike"; }
		else { return; }
		
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
					getKeyboardInput();
				}
				if (closeSmithingRunner) { // Close dialog if requested
					return;
				}
				drawMouseHoverStatus(hoveredItemIndex);
				while (Mouse.next()) {
					if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
						if (showingDialog && hoveredItemIndex != -1 && !player.isBusy()) {
							if (hoveredItemIndex == 0 && !hasRhichiteOre) {
								Crissaegrim.addSystemMessage("You need Rhichite Ore to smelt a Rhichite Bar.");
							} else if (hoveredItemIndex == 1 && (!hasValeniteOre || !hasSandelugeOre)) {
								Crissaegrim.addSystemMessage("You need Valenite Ore and Sandeluge Ore to smelt a Tameike Bar.");
							} else {
								showingDialog = false;
								Crissaegrim.addSystemMessage("You begin smelting the ore.");
								smithingItemType = hoveredItemIndex;
								lastSmithTime = new Date().getTime();
								player.setBusy(new SmeltingOreBusy(player.getPosition()));
							}
						}
					}
				}
				
				playerMovementHelper.moveEntity();
				
				gameRunner.drawHUD();
				if (showingDialog) { drawSmithingDialog(); }
				if (smithingItemType != -1 && new Date().getTime() > lastSmithTime + SMITH_ITEM_DELAY_MILLIS) { smeltOre(); }
				
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
	
	/**
	 * Perform the next ore smelting action tick.
	 */
	private void smeltOre() {
		Inventory inventory = Crissaegrim.getPlayer().getInventory();
		boolean canContinue = true;
		if (smithingItemType == 0) { // Rhichite
			int rhichiteIndex = getNextOreIndex("Rhichite");
			if (rhichiteIndex != -1) {
				inventory.removeItem(rhichiteIndex);
				if (Thunderbrand.getRandomNumbers().getBoolean()) {
					inventory.addItem(Items.rhichiteBar());
					Crissaegrim.addSystemMessage("You smelt a Rhichite Bar.");
				} else {
					Crissaegrim.addSystemMessage("The Rhichite Ore crumbled away.");
				}
			}
			if (getNextOreIndex("Rhichite") == -1) {
				canContinue = false;
			}
		} else if (smithingItemType == 1) { // Tameike
			int valeniteIndex = getNextOreIndex("Valenite");
			int sandelugeIndex = getNextOreIndex("Sandeluge");
			if (valeniteIndex != -1 && sandelugeIndex != -1) {
				inventory.removeItem(valeniteIndex);
				inventory.removeItem(sandelugeIndex);
				inventory.addItem(Items.tameikeBar());
				Crissaegrim.addSystemMessage("You smelt a Tameike Bar.");
			}
			if (getNextOreIndex("Valenite") == -1 || getNextOreIndex("Sandeluge") == -1) {
				canContinue = false;
			}
		}
		lastSmithTime = new Date().getTime();
		if (!canContinue) {
			quitSmeltingOre(true);
			closeSmithingRunner = true;
		}
	}
	
	/**
	 * @param oreType The type of ore to find
	 * @return The index of that ore in the player's {@link Inventory}
	 */
	private int getNextOreIndex(String oreType) {
		Inventory inventory = Crissaegrim.getPlayer().getInventory();
		for (int i = 0; i < inventory.getInventorySize(); i++) {
			Item item = inventory.getItem(i);
			if (item instanceof ItemOre && ((ItemOre)(item)).getOreType().equals(oreType)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Quit smelting ores.  Display a message and remove the SmeltingOreBusy if it is still active.
	 * @param quitNormally Determines which message to show
	 */
	private void quitSmeltingOre(boolean quitNormally) {
		if (Crissaegrim.getPlayer().getBusy() instanceof SmeltingOreBusy) {
			Crissaegrim.getPlayer().setBusy(null);
			if (quitNormally) {
				Crissaegrim.addSystemMessage("You finish smelting the ore.");
			} else {
				Crissaegrim.addSystemMessage("You stop smelting the ore.");
			}
		}
	}
	
	/**
	 * Recalculates {@code smeltingDialogRect} due to first-run conditions or a changed window size.
	 */
	private void recalculateDialogDimensions() {
		int dialogBoxWidth = 365;
		int dialogBoxHeight = 204;
		int widthBuffer = (Crissaegrim.getWindowWidth() - dialogBoxWidth) / 2;
		int heightBuffer = (Crissaegrim.getWindowHeight() - dialogBoxHeight) / 2;
		smithingDialogRect = new Rect(
				new Coordinate(widthBuffer, heightBuffer),
				new Coordinate(Crissaegrim.getWindowWidth() - widthBuffer, Crissaegrim.getWindowHeight() - heightBuffer));
	}
	
	/**
	 * Detects keyboard input and reacts accordingly.
	 */
	private void getKeyboardInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_D) ||
				Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (smithingItemType != -1) { quitSmeltingOre(false); }
			closeSmithingRunner = true;
			return;
		}
		
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
				} else if (pressedKey == Keyboard.KEY_ESCAPE) {	// Escape: Close smithing dialog
					closeSmithingRunner = true;
				}
			}
		}
	}
	
	/**
	 * @return The index of the smeltable item the mouse is currently hovering over, or -1 if no item is being hovered over
	 */
	private int getHoveredItemIndex() {
		Coordinate mouseCoords = new Coordinate(Mouse.getX(), Mouse.getY());
		int posX, posY;
		posX = (int)(smithingDialogRect.getLeft()) + OUTER_PADDING_PIXELS;
		posY = (int)(smithingDialogRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
		for (int i = 0; i < 2; i++) {
			Rect smeltableItemBoxRect = new Rect(
					new Coordinate(posX, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2),
					new Coordinate(posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY));
			if (RectUtils.coordinateIsInRect(mouseCoords, smeltableItemBoxRect)) {
				return i;
			}
			posY -= BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2 + OUTER_PADDING_PIXELS;
		}
		return -1;
	}
	
	private void drawMouseHoverStatus(int hoveredItemIndex) {
		String mouseHoverString;
		if (hoveredItemIndex == 0 && hasRhichiteOre) {
			mouseHoverString = "Smelt Rhichite Bar";
		} else if (hoveredItemIndex == 1 && hasValeniteOre && hasSandelugeOre) {
			mouseHoverString = "Smelt Tameike Bar";
		} else {
			return; // Nothing to display
		}
		TextTexture mouseHoverStatus = Crissaegrim.getCommonTextures().getTextTexture(mouseHoverString);
		int top = Crissaegrim.getWindowHeight() - 5;
		GLDrawer.useTexture(mouseHoverStatus.getTextureId());
		GLDrawer.drawQuad(5, 5 + mouseHoverStatus.getWidth(), top - 20, top);
	}
	
	private void drawSmithingDialog() {
		GameInitializer.initializeNewFrameForWindow();
		GLDrawer.disableTextures();
		
		GLDrawer.setColor(0.314, 0.314, 0.314);
		GLDrawer.drawQuad(smithingDialogRect); // Draw smithing dialog background
		// Draw blue gradient title bar rectangle:
		glBegin(GL_QUADS);
		GLDrawer.setColor(0, 0.188, 0.502);
			glVertex2d(smithingDialogRect.getLeft(), smithingDialogRect.getTop());
			glVertex2d(smithingDialogRect.getRight(), smithingDialogRect.getTop());
			GLDrawer.setColor(0, 0.251, 0.667);
			glVertex2d(smithingDialogRect.getRight(), smithingDialogRect.getTop() - OUTER_PADDING_PIXELS - 25);
			glVertex2d(smithingDialogRect.getLeft(), smithingDialogRect.getTop() - OUTER_PADDING_PIXELS - 25);
		glEnd();
		GLDrawer.setColor(0.75, 0.75, 0.75);
		GLDrawer.setLineWidth(3);
		GLDrawer.drawOutline(smithingDialogRect); // Draw smithing dialog outline
		GLDrawer.setLineWidth(1);
		
		TextTexture headerLabel = Crissaegrim.getCommonTextures().getTextTexture("Anvil: Smith items");
		GLDrawer.useTexture(headerLabel.getTextureId());
		GLDrawer.drawQuad(smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS, smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS + headerLabel.getWidth(),
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS - 20, smithingDialogRect.getTop() - OUTER_PADDING_PIXELS);
		
		// Draw clickable boxes:
		int posX, posY;
		posX = (int)(smithingDialogRect.getLeft()) + OUTER_PADDING_PIXELS;
		posY = (int)(smithingDialogRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
		for (int i = 0; i < 2; i++) {
			GLDrawer.disableTextures();
			if (i == 0) {
				setColorForItemBoxBackground(hasRhichiteOre);
				GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				setGlColorForItemBoxOutline(hasRhichiteOre);
				GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				GLDrawer.useTexture(Textures.ITEM_RHICHITE_BAR);
			} else if (i == 1) {
				setColorForItemBoxBackground(hasValeniteOre && hasSandelugeOre);
				GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				setGlColorForItemBoxOutline(hasValeniteOre && hasSandelugeOre);
				GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
				GLDrawer.useTexture(Textures.ITEM_TAMEIKE_BAR);
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
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS,
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + requiresLabel.getWidth(),
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*2 - 71,
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*2 - 51);
		GLDrawer.drawQuad(
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS,
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + requiresLabel.getWidth(),
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 71,
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 51);
		GLDrawer.useTexture(plusLabel.getTextureId());
		GLDrawer.drawQuad(
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*4 + INNER_PADDING_PIXELS*4 + BOX_SIZE_PIXELS*2 + requiresLabel.getWidth(),
				smithingDialogRect.getLeft() + OUTER_PADDING_PIXELS*4 + INNER_PADDING_PIXELS*4 + BOX_SIZE_PIXELS*2 + requiresLabel.getWidth() + plusLabel.getWidth(),
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 71,
				smithingDialogRect.getTop() - OUTER_PADDING_PIXELS*3 - INNER_PADDING_PIXELS*2 - BOX_SIZE_PIXELS - 51);
		
		// Draw required ores:
		posX = (int)(smithingDialogRect.getLeft()) + 186;
		posY = (int)(smithingDialogRect.getTop()) - OUTER_PADDING_PIXELS*2 - 25;
		GLDrawer.disableTextures();
		setColorForItemBoxBackground(hasRhichiteOre);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		setGlColorForItemBoxOutline(hasRhichiteOre);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_RHICHITE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		posX = (int)(smithingDialogRect.getLeft()) + 186;
		posY = (int)(smithingDialogRect.getTop()) - OUTER_PADDING_PIXELS*3 - 25 - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2;
		GLDrawer.disableTextures();
		setColorForItemBoxBackground(hasValeniteOre);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		setGlColorForItemBoxOutline(hasValeniteOre);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_VALENITE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		posX = (int)(smithingDialogRect.getLeft()) + 184 + OUTER_PADDING_PIXELS*2 + INNER_PADDING_PIXELS*2 + BOX_SIZE_PIXELS + plusLabel.getWidth();
		GLDrawer.disableTextures();
		setColorForItemBoxBackground(hasSandelugeOre);
		GLDrawer.drawQuad(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		setGlColorForItemBoxOutline(hasSandelugeOre);
		GLDrawer.drawOutline(posX, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS*2, posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS*2, posY);
		GLDrawer.useTexture(Textures.ITEM_SANDELUGE_ORE);
		GLDrawer.drawQuad(posX + INNER_PADDING_PIXELS, posX + BOX_SIZE_PIXELS + INNER_PADDING_PIXELS,
				posY - BOX_SIZE_PIXELS - INNER_PADDING_PIXELS, posY - INNER_PADDING_PIXELS);
		
		
	}
	
	private void setColorForItemBoxBackground(boolean fulfillsRequirements) {
		if (fulfillsRequirements) {
			GLDrawer.setColor(0.122, 0.141, 0.161);
		} else {
			GLDrawer.setColor(0.188, 0.094, 0.094);
		}
	}
	
	private void setGlColorForItemBoxOutline(boolean fulfillsRequirements) {
		if (fulfillsRequirements) {
			GLDrawer.setColor(1, 1, 1);
		} else {
			GLDrawer.setColor(1, 0.651, 0.651);
		}
	}
	
}
