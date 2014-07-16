package dialogboxes;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import players.Player;
import textures.Textures;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;
import crissaegrim.GameRunner;
import entities.EntityMovementHelper;

public class DialogBoxRunner {
	
	public DialogBox.Result run(DialogBox dialogBox) {
		GameRunner gameRunner = Crissaegrim.getGameRunner();
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		GameInitializer.setWindowTitle("Dialog box");
		
		try {
			long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
			while (!Display.isCloseRequested()) {
				startTime = Thunderbrand.getTime();
				
				if (!Crissaegrim.connectionStable) { // Lost connection to server
					Crissaegrim.getValmanwayConnection().closeConnections();
					Crissaegrim.getGameRunner().displayMessageForever(Textures.LOST_CONNECTION_MESSAGE, 423, 64, "Connection lost - Please restart");
					break;
				}
				
				player.update(); // (Don't negate gravity)
				gameRunner.drawScene();
				
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput(false);
				} else {
					getKeyboardInput(dialogBox);
				}
				// If a button was clicked, find out which one and return it if valid
				while (Mouse.next()) {
					if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
						int clickedButton = dialogBox.getHoveredButtonIndex();
						if (clickedButton != -1) {
							return getDialogResultForIndex(clickedButton);
						}
					}
				}
				
				playerMovementHelper.moveEntity();
				
				gameRunner.drawHUD();
				
				dialogBox.draw();
				
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
		return null;
	}
	
	/**
	 * Detects keyboard input and reacts accordingly.
	 */
	private void getKeyboardInput(DialogBox dialogBox) {
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
					dialogBox.recalculateDialogDimensions();
				}
			}
		}
	}
	
	private DialogBox.Result getDialogResultForIndex(int index) {
		switch (index) {
			case 0: return DialogBox.Result.BUTTON_1;
			case 1: return DialogBox.Result.BUTTON_2;
			case 2: return DialogBox.Result.BUTTON_3;
			case 3: return DialogBox.Result.BUTTON_4;
			case 4: return DialogBox.Result.BUTTON_5;
			case 5: return DialogBox.Result.BUTTON_6;
			case 6: return DialogBox.Result.BUTTON_7;
			case 7: return DialogBox.Result.BUTTON_8;
			case 8: return DialogBox.Result.BUTTON_9;
			case 9: return DialogBox.Result.BUTTON_10;
		}
		return null;
	}
	
}
