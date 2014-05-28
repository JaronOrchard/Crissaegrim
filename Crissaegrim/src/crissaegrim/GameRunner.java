package crissaegrim;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import players.Player;
import datapacket.AttackPacket;
import datapacket.ChunkPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.RequestEntireBoardPacket;
import datapacket.RequestPlayerIdPacket;
import doodads.Doodad;
import doodads.Door;
import entities.EntityMovementHelper;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.RectUtils;
import attack.Attack;
import board.Board;
import board.BoardInfo;
import board.Chunk;
import board.ClientBoard;
import board.MissingChunk;
import board.tiles.Tile.TileLayer;
import busy.GotHitByAttackBusy;
import textblock.TextBlock;
import textures.Textures;
import thunderbrand.Thunderbrand;

public class GameRunner {
	
	private static final long FRAMES_PER_SECOND = 100;
	private static final long MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
	
	private long lastFPSTitleUpdate;
	private long millisecondsSkipped = 0;
	private short framesRendered = 0;
	
	private Map<String, Board> boardMap = new HashMap<String, Board>();
	private String destinationBoardName = "";
	private Coordinate destinationCoordinate = null;
	private List<TextBlock> waitingChatMessages = Collections.synchronizedList(new ArrayList<TextBlock>());
	
	public void addWaitingChatMessage(TextBlock tb) { waitingChatMessages.add(tb); }
	
	public void run() throws InterruptedException, IOException {
		GameInitializer.initializeDisplay();
		Textures.initializeTextures();
		Crissaegrim.initializePlayer(boardMap);
		
		Crissaegrim.getValmanwayConnection().connectToValmonwayServer();
		if (Crissaegrim.connectionStable) { // Wait to get player id...
			long lastSend = 0;
			while (Crissaegrim.getPlayer().getId() == -1) {
				if (Thunderbrand.getTime() - lastSend > Crissaegrim.getValmanwayConnection().getConnectionTimeoutMillis()) {
					lastSend = Thunderbrand.getTime();
					Crissaegrim.addOutgoingDataPacket(new RequestPlayerIdPacket(Crissaegrim.getClientVersion()));
				}
			}
		} else { // Couldn't connect + offline mode disallowed; display error and quit
			displayMessageForever(Textures.NO_CONNECTION_MESSAGE, 458, 64, null);
			return;
		}
		if (Crissaegrim.getPlayer().getId() == -2) { // playerId of -2 signifies outdated version
			displayMessageForever(Textures.CLIENT_OUTDATED_MESSAGE, 595, 102, "Your version is outdated!");
			return;
		}
		
		destinationBoardName = "tower_of_preludes";
		destinationCoordinate = new Coordinate(10044, 10084); // tower_of_preludes
		
		//destinationPosition = new Coordinate(10044, 10020); // dawning
		goToDestinationBoard();
		
		//initializeGame();
		
		lastFPSTitleUpdate = Thunderbrand.getTime();
		GameInitializer.initializeOpenGLFor2D();
		
		long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
		while (!Display.isCloseRequested()) {
			startTime = Thunderbrand.getTime();
			
			if (!Crissaegrim.connectionStable) { // Lost connection to server
				Crissaegrim.getValmanwayConnection().closeConnections();
				displayMessageForever(Textures.LOST_CONNECTION_MESSAGE, 423, 64, "Connection lost - Please restart");
				return;
			}
			
			// Update the board, including all entities and bullets:
			if (!Crissaegrim.currentlyLoading) {
				ClientBoard.verifyChunksExist(Crissaegrim.getBoard());
				if (Crissaegrim.currentlyLoading) { continue; }
				Crissaegrim.getPlayer().update();
				actionDoodadList();
			
				// Draw new scene:
				drawScene();
			
				// Get input and move the player:
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput();
				} else {
					getKeyboardAndMouseInput();
				}
				Attack playerAttack = Crissaegrim.getPlayer().getMovementHelper().moveEntity();
				if (playerAttack != null) { Crissaegrim.addOutgoingDataPacket(new AttackPacket(playerAttack)); }
			
				// Transmit data to the server
				Crissaegrim.getValmanwayConnection().sendPlayerStatus();
			} else {
				drawScene();
				drawLoadingMessage();
				drawLoadingProgressBar();
			}
			
			Display.update();
			endTime = Thunderbrand.getTime();
			elaspedTime = endTime - startTime;
			Thread.sleep(Math.max(0, MILLISECONDS_PER_FRAME - elaspedTime));
			updateFPS(Math.max(0, MILLISECONDS_PER_FRAME - elaspedTime));
		}

		Display.destroy();
		Crissaegrim.getValmanwayConnection().closeConnections();
	}
	
	private void drawScene() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Board currentBoard = Crissaegrim.getBoard();
		
		if (Crissaegrim.getBoard() != null) {
			GameInitializer.initializeNewFrameForWindow();
			ClientBoard.drawBackground(currentBoard);
			
			GameInitializer.initializeNewFrameForScene();
			ClientBoard.draw(currentBoard, TileLayer.BACKGROUND);
			ClientBoard.draw(currentBoard, TileLayer.MIDDLEGROUND);
			for (Doodad doodad : Crissaegrim.getBoard().getDoodadList()) {
				if (!Crissaegrim.getDebugMode()) {
					doodad.draw();
				} else {
					doodad.drawDebugMode();
				}
			}
			Crissaegrim.getPlayer().draw();
			if (Crissaegrim.getDebugMode()) {
				Crissaegrim.getPlayer().drawDebugMode();
			}
			drawGhosts();
			ClientBoard.draw(currentBoard, TileLayer.FOREGROUND);
		}
		
		GameInitializer.initializeNewFrameForWindow();
		Crissaegrim.getPlayer().getInventory().draw();
		while (!waitingChatMessages.isEmpty()) {
			Crissaegrim.getChatBox().addChatMessage(waitingChatMessages.remove(0));
		}
		Crissaegrim.getChatBox().draw();
	}
	
	private void actionDoodadList() {
		Iterator<Doodad> doodadIter = Crissaegrim.getBoard().getDoodadList().iterator();
		while (doodadIter.hasNext()) {
			Doodad doodad = doodadIter.next();
			if (doodad instanceof Door) {
				if (RectUtils.coordinateIsInRect(Crissaegrim.getPlayer().getPosition(), doodad.getBounds())) {
					Crissaegrim.getPlayer().setIcon("X");
				}
			}
		}
	}
	int x = 0;
	/**
	 * Detects keyboard and mouse input and makes the main player react accordingly.
	 */
	private void getKeyboardAndMouseInput() {
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper pmh = player.getMovementHelper();
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) { pmh.requestLeftMovement(); }
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) { pmh.requestRightMovement(); }
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) { pmh.requestJumpMovement(); }
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { // Key was pressed (not released)
				
				if (Keyboard.getEventKey() == Keyboard.KEY_B) {
					player.setBusy(new GotHitByAttackBusy(false));
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_T ||
						Keyboard.getEventKey() == Keyboard.KEY_RETURN) {	// T or Enter: Enter chat mode
					Crissaegrim.getChatBox().enableTypingMode();
					return; // Don't process any more keys!
				} else if (Keyboard.getEventKey() == Keyboard.KEY_UP) {		// Up arrow: Select previous inventory item
					player.getInventory().selectPreviousItem();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {	// Down arrow: Select next inventory item
					player.getInventory().selectNextItem();
				} else if (Keyboard.getEventKey() >= Keyboard.KEY_1 && Keyboard.getEventKey() <= Keyboard.KEY_8) { // 1-8: Select inventory item
					player.getInventory().selectSpecificItem(Keyboard.getEventKey() - Keyboard.KEY_1);
				} else if (Keyboard.getEventKey() == 41) {					// Backtick (`) key
					Crissaegrim.toggleDebugMode();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_TAB) {	// Tab key: Toggle zoom
					Crissaegrim.toggleZoom();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_M) {		// M key: Toggle window size
					Crissaegrim.toggleWindowSize();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_X) {		// X key: Enter door
					for (Doodad doodad : Crissaegrim.getBoard().getDoodadList()) {
						if (!player.isBusy() && doodad instanceof Door &&
								RectUtils.coordinateIsInRect(player.getPosition(), doodad.getBounds())) {
							Door door = (Door)doodad;
							pmh.resetMovementRequests();
							destinationBoardName = door.getDestinationBoardName();
							destinationCoordinate = door.getDestinationCoordinate();
							goToDestinationBoard();
						}
					}
				}
			}
		}
		
		if (!Crissaegrim.getChatBox().isTypingMode()) {
			while (Mouse.next()) {
				if (Mouse.getEventButtonState()) { // Button was clicked (not released)
					if (Mouse.getEventButton() == 0) {
						pmh.requestUseItem(Crissaegrim.getPlayer().getInventory().getCurrentItem());
					}
				}
			}
			int scrollDelta = Mouse.getDWheel();
			if (scrollDelta < 0) {
				Crissaegrim.getPlayer().getInventory().selectNextItem();
			} else if (scrollDelta > 0) {
				Crissaegrim.getPlayer().getInventory().selectPreviousItem();
			}
		}
	}
	
	/**
	 * Count the number of frames per second.  Update the title bar with the count every second.
	 * @param millisSkipped The number of milliseconds skipped in the current frame
	 */
	public void updateFPS(long millisSkipped) {
		millisecondsSkipped += millisSkipped;
		framesRendered += 1;
	    if (Thunderbrand.getTime() - lastFPSTitleUpdate > 1000) { // Update the title in one-second increments
	        GameInitializer.setWindowTitle("FPS: " + framesRendered + " | Idle time: " + (millisecondsSkipped / 10) + "%");
	        framesRendered = 0; // Reset the frames rendered
	        millisecondsSkipped = 0; // Reset the milliseconds skipped
	        lastFPSTitleUpdate += 1000; // Add one second
	    }
	}
	
	private void drawGhosts() {
		synchronized (Crissaegrim.getGhosts()) {
			String currentBoardName = Crissaegrim.getBoard().getName();
			for (Entry<Integer, EntityStatus> ghost : Crissaegrim.getGhosts().entrySet()) {
				if (ghost.getValue().getBoardName().equalsIgnoreCase(currentBoardName)) {
					drawGhost(ghost.getValue());
				}
			}
		}		
	}
	
	private void drawGhost(EntityStatus ghost) {
		boolean facingRight = ghost.getFacingRight();
		double xPos = ghost.getXPos();
		double yPos = ghost.getYPos();
		glColor3d(1.0, 1.0, 1.0);
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D, ghost.getCurrentTexture());
			glBegin(GL_QUADS);
				glTexCoord2d(facingRight ? 0 : 1, 0);
				glVertex2d(xPos - 1.5, yPos + 3);
				glTexCoord2d(facingRight ? 1 : 0, 0);
				glVertex2d(xPos + 1.5, yPos + 3);
				glTexCoord2d(facingRight ? 1 : 0, 1);
				glVertex2d(xPos + 1.5, yPos);
				glTexCoord2d(facingRight ? 0 : 1, 1);
				glVertex2d(xPos - 1.5, yPos);
			glEnd();
		glPopMatrix();
	}
	
	private void drawLoadingMessage() {
		GameInitializer.initializeNewFrameForWindow();
		// Loading message texture size is 372 x 64
		glBindTexture(GL_TEXTURE_2D, Textures.LOADING_MESSAGE);
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 96);
				glTexCoord2d(1, 1);
				glVertex2d(404, Crissaegrim.getWindowHeight() - 96);
				glTexCoord2d(1, 0);
				glVertex2d(404, Crissaegrim.getWindowHeight() - 32);
				glTexCoord2d(0, 0);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 32);
			glEnd();
		glPopMatrix();
	}
	
	private void drawLoadingProgressBar() {
		if (Crissaegrim.numPacketsToReceive != 0) {
			// Loading message texture size is 372 x 64, so use that width...
			double amtLoaded = (double)Crissaegrim.numPacketsReceived / (double)Crissaegrim.numPacketsToReceive;
			int progressBarRight = (int)(((1.0 - amtLoaded) * 32.0) + (amtLoaded * 404));  
			GameInitializer.initializeNewFrameForWindow();
			glDisable(GL_TEXTURE_2D);
			glColor3d(0.15, 0.45, 0.15);
			glBegin(GL_QUADS);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 106);
				glVertex2d(progressBarRight, Crissaegrim.getWindowHeight() - 106);
				glVertex2d(progressBarRight, Crissaegrim.getWindowHeight() - 136);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 136);
			glEnd();
			glColor3d(0.66, 0.66, 0.66);
			glLineWidth(2);
			glBegin(GL_LINE_LOOP);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 106);
				glVertex2d(404, Crissaegrim.getWindowHeight() - 106);
				glVertex2d(404, Crissaegrim.getWindowHeight() - 136);
				glVertex2d(32, Crissaegrim.getWindowHeight() - 136);
			glEnd();
			glLineWidth(1);
			glEnable(GL_TEXTURE_2D);
		}
	}
	
	/**
	 * Displays one of the message textures made in MSPaint until the client is closed.
	 * @param texture The texture id of the message to display
	 * @param width The width of the texture
	 * @param height The height of the texture
	 * @param optionalTitleChange This message will be shown on the title bar.  A {@code null} or empty string will be ignored.
	 * @throws InterruptedException
	 */
	private void displayMessageForever(int texture, int width, int height, String optionalTitleChange) throws InterruptedException {
		if (optionalTitleChange != null && !optionalTitleChange.trim().isEmpty()) {
			GameInitializer.setWindowTitle(optionalTitleChange);
		}
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			GameInitializer.initializeNewFrameForWindow();
			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, texture);
			glPushMatrix();
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(32, Crissaegrim.getWindowHeight() - (32 + height));
					glTexCoord2d(1, 1);
					glVertex2d((32 + width), Crissaegrim.getWindowHeight() - (32 + height));
					glTexCoord2d(1, 0);
					glVertex2d((32 + width), Crissaegrim.getWindowHeight() - 32);
					glTexCoord2d(0, 0);
					glVertex2d(32, Crissaegrim.getWindowHeight() - 32);
				glEnd();
			glPopMatrix();
			
			Display.update();
			Thread.sleep(100);
		}
		Display.destroy();
	}
	
	public void goToDestinationBoard() {
		if (!destinationBoardName.isEmpty() && destinationCoordinate != null) {
			if (boardMap.containsKey(destinationBoardName)) {
				Crissaegrim.getPlayer().setCurrentBoardName(destinationBoardName);
				//Crissaegrim.setBoard(boardMap.get(destinationBoardName));
				Crissaegrim.getPlayer().getPosition().setAll(destinationCoordinate);
				destinationBoardName = "";
				destinationCoordinate = null;
				Crissaegrim.currentlyLoading = false;
			} else {
				Crissaegrim.addOutgoingDataPacket(new RequestEntireBoardPacket(destinationBoardName));
				Crissaegrim.currentlyLoading = true;
				Crissaegrim.numPacketsReceived = 0;
				Crissaegrim.numPacketsToReceive = 0;
			}
		}
	}
	
	public void addChunk(ChunkPacket cp) {
		addBoardToMapIfNeeded(cp.getBoardName());
		boardMap.get(cp.getBoardName()).getChunkMap().put(
				cp.getChunkXOrigin() + "_" + cp.getChunkYOrigin(),
				new Chunk(cp.getChunkXOrigin(), cp.getChunkYOrigin(), cp.getData()));
	}
	
	public void addNonexistentChunk(NonexistentChunkPacket ncp) {
		addBoardToMapIfNeeded(ncp.getBoardName());
		boardMap.get(ncp.getBoardName()).getChunkMap().put(
				ncp.getChunkXOrigin() + "_" + ncp.getChunkYOrigin(),
				new MissingChunk(ncp.getChunkXOrigin(), ncp.getChunkYOrigin()));
	}
	
	private void addBoardToMapIfNeeded(String boardName) {
		if (!boardMap.containsKey(boardName)) {
			boardMap.put(boardName, new Board(boardName));
			
			// Set up doodads:
			BoardInfo.addDoodadsToBoard(boardName, boardMap.get(boardName).getDoodadList());
		}
	}
	
}
