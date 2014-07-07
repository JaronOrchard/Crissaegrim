package players;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;
import crissaegrim.GameRunner;
import entities.EntityMovementHelper;

public class InventoryRunner {
	
	private boolean closeInventory;
	
	public InventoryRunner() {
		closeInventory = false;
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
				// If a button was clicked, find out which one and return it if valid
//				while (Mouse.next()) {
//					if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
//						int clickedButton = dialogBox.getHoveredButtonIndex();
//						if (clickedButton != -1) {
//							return getDialogResultForIndex(clickedButton);
//						}
//					}
//				}
				
				playerMovementHelper.moveEntity();
				
				gameRunner.drawHUD();
				
				// --- DRAW INVENTORY BOX
				
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
				} else if (pressedKey == Keyboard.KEY_ESCAPE ||
						pressedKey == Keyboard.KEY_E) {			// E / Escape: Close inventory
					closeInventory = true;
				}
			}
		}
	}
	
}
