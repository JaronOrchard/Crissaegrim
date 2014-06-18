package dialogboxes;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import players.Player;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import crissaegrim.GameRunner;
import entities.EntityMovementHelper;

public class DialogBoxRunner {
	
	public DialogBox.Result run(DialogBox dialogBox) {
		GameRunner gameRunner = Crissaegrim.getGameRunner();
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		
		try {
	//		if (Crissaegrim.currentlyLoading) { break; }
			
			long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
			while (!Display.isCloseRequested()) {
				startTime = Thunderbrand.getTime();
				
				if (!Crissaegrim.connectionStable) { break; }
				
				// Update the board, including all entities and bullets:
	//			if (!Crissaegrim.currentlyLoading) {
	//				ClientBoard.verifyChunksExist(Crissaegrim.getBoard());
	//				if (Crissaegrim.currentlyLoading) { continue; }
					player.update();
	//				actionDoodadList();
					
					// Draw new scene:
					gameRunner.drawScene();
					
					// Get input and move the player:
	//				if (Crissaegrim.getChatBox().isTypingMode()) {
	//					Crissaegrim.getChatBox().getKeyboardInput();
	//				} else {
	//					getKeyboardAndMouseInput();
	//				}
	//				drawMouseHoverStatus();
	//				playerMovementHelper.moveEntityPre();
	//				Item itemToUse = playerMovementHelper.getItemToUse();
	//				if (itemToUse != null && !player.isBusy()) {
	//					if (itemToUse instanceof Weapon) {
	//						// TODO: This should be split up depending upon the weapon and attack type
	//						// TODO: Bounding rect of sword swing should not be entire entity
	//						player.setBusy(new SwordSwingBusy());
	//						Crissaegrim.addOutgoingDataPacket(new AttackPacket(new Attack(
	//								player.getId(), player.getCurrentBoardName(), player.getSwordSwingRect(), ((Weapon)(itemToUse)).getAttackPower(), 1)));
	//					} else if (itemToUse instanceof ItemPartyPopper) {
	//						ItemPartyPopper popper = (ItemPartyPopper)(itemToUse);
	//						Crissaegrim.addOutgoingDataPacket(new ParticleSystemPacket(
	//								125, playerMovementHelper.getCoordinateClicked(), player.getCurrentBoardName(), popper.getColor()));
	//						popper.decrementUses();
	//						if (popper.getUsesLeft() <= 0) {
	//							player.getInventory().removeCurrentItem();
	//						}
	//					}
	//				}
	//				playerMovementHelper.moveEntityPost();
					playerMovementHelper.moveEntity();
					
					gameRunner.drawHUD();
					
					dialogBox.draw();
					
					// Transmit data to the server
					Crissaegrim.getValmanwayConnection().sendPlayerStatus();
	//			} else {
	//				drawScene();
	//				drawLoadingMessage();
	//				drawLoadingProgressBar();
	//			}
					
				Display.update();
				endTime = Thunderbrand.getTime();
				elaspedTime = endTime - startTime;
				Thread.sleep(Math.max(0, GameRunner.MILLISECONDS_PER_FRAME - elaspedTime));
	//			updateFPS(Math.max(0, gameRunner.MILLISECONDS_PER_FRAME - elaspedTime));
			}
			
			Display.destroy();
			Crissaegrim.getValmanwayConnection().closeConnections();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
		return null;
	}
	
}
