package crissaegrim;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import datapacket.RequestPlayerIdPacket;
import entities.Door;
import entities.Entity;
import entities.Target;
import player.PlayerStatus;
import players.PlayerMovementHelper;
import attack.Attack;
import board.Board;
import textures.Textures;
import thunderbrand.TextBlock;
import thunderbrand.Thunderbrand;

public class GameRunner {
	
	public enum TileLayer { BACKGROUND, MIDDLEGROUND, FOREGROUND }
	
	private static final long FRAMES_PER_SECOND = 100;
	private static final long MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
	
	private long lastFPSTitleUpdate;
	private long millisecondsSkipped = 0;
	
	private Map<String, Board> boardMap;
	private PlayerMovementHelper playerMovementHelper;
	private List<TextBlock> waitingChatMessages = Collections.synchronizedList(new ArrayList<TextBlock>());
	
	public void addWaitingChatMessage(TextBlock tb) { waitingChatMessages.add(tb); }
	
	public void run() throws InterruptedException, IOException {
		GameInitializer.initializeDisplay();
		Textures.initializeTextures();
		
		Crissaegrim.getValmanwayConnection().connectToValmonwayServer();
		if (Crissaegrim.getValmanwayConnection().getOnline()) { // If online, wait to get player id...
			long lastSend = 0;
			while (Crissaegrim.getPlayer().getId() == -1) {
				if (Thunderbrand.getTime() - lastSend > 1000) {
					lastSend = Thunderbrand.getTime();
					Crissaegrim.addOutgoingDataPacket(new RequestPlayerIdPacket());
				}
			}
		} else {
			Crissaegrim.getPlayer().assignPlayerId(999); // No Valmanway connection
		}
		
		setupBoards();
		Crissaegrim.setBoard(boardMap.get("tower_of_preludes"));
		//currentBoard = boardMap.get("dawning");
		playerMovementHelper = new PlayerMovementHelper();
		
		//initializeGame();
		
		lastFPSTitleUpdate = Thunderbrand.getTime();
		GameInitializer.initializeOpenGLFor2D();
		
		long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
		while (!Display.isCloseRequested()) {
			startTime = Thunderbrand.getTime();
			
			// Update the board, including all entities and bullets:
			Crissaegrim.getBoard().preloadChunks(); // WE COULD PROBABLY DO THIS NOT THAT OFTEN
			actionAttackList();
			Crissaegrim.getPlayer().update();
			actionEntityList();
			
			// Draw new scene:
			drawScene();
			
			// Get input and move the player:
			if (Crissaegrim.getChatBox().isTypingMode()) {
				Crissaegrim.getChatBox().getKeyboardInput();
			} else {
				getKeyboardAndMouseInput();
			}
			playerMovementHelper.movePlayer();
			Crissaegrim.getBoard().getAttackList().addAll(playerMovementHelper.getAttackList());
			
			// Transmit data to the server
			Crissaegrim.getValmanwayConnection().sendPlayerStatus();
			
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
		
		GameInitializer.initializeNewFrameForWindow();
		Crissaegrim.getBoard().drawBackground();
		
		GameInitializer.initializeNewFrameForScene();
		Crissaegrim.getBoard().draw(TileLayer.BACKGROUND);
		Crissaegrim.getBoard().draw(TileLayer.MIDDLEGROUND);
		for (Entity entity : Crissaegrim.getBoard().getEntityList()) {
			if (!Crissaegrim.getDebugMode()) {
				entity.draw();
			} else {
				entity.drawDebugMode();
			}
		}
		Crissaegrim.getPlayer().draw();
		if (Crissaegrim.getDebugMode()) {
			Crissaegrim.getPlayer().drawDebugMode();
		}
		drawGhosts();
		Crissaegrim.getBoard().draw(TileLayer.FOREGROUND);
		
		GameInitializer.initializeNewFrameForWindow();
		while (!waitingChatMessages.isEmpty()) {
			Crissaegrim.getChatBox().addChatMessage(waitingChatMessages.remove(0));
		}
		Crissaegrim.getChatBox().draw();
	}
	
	/**
	 * Goes through the {@link Attack} list, actioning Attacks if necessary
	 */
	private void actionAttackList() {
		Iterator<Attack> attackIter = Crissaegrim.getBoard().getAttackList().iterator();
		while (attackIter.hasNext()) {
			Attack attack = attackIter.next();
			
			Iterator<Entity> entityIter = Crissaegrim.getBoard().getEntityList().iterator();
			while (entityIter.hasNext()) {
				Entity entity = entityIter.next();
				if (entity instanceof Target) {
					if (LineUtils.lineSetsIntersect(RectUtils.getLinesFromRect(attack.getBounds()), RectUtils.getLinesFromRect(entity.getBounds()))) {
						System.out.println("USER ID " + attack.getAttackerId() + " BROKE A TARGET!");
						entityIter.remove();
					}
				}
			}
			
			
			if (attack.getOneFrameLifetime()) {
				attackIter.remove();
			}
		}
	}
	
	private void actionEntityList() {
		Iterator<Entity> entityIter = Crissaegrim.getBoard().getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity entity = entityIter.next();
			if (entity instanceof Door) {
				if (RectUtils.coordinateIsInRect(Crissaegrim.getPlayer().getPosition(), entity.getBounds())) {
					Crissaegrim.getPlayer().setIcon("X");
				}
			}
		}
	}
	
	/**
	 * Detects keyboard and mouse input and makes the main player react accordingly.
	 */
	private void getKeyboardAndMouseInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) { playerMovementHelper.requestLeftMovement(); }
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) { playerMovementHelper.requestRightMovement(); }
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) { playerMovementHelper.requestJumpMovement(); }
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) { playerMovementHelper.requestAttack(); }
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { // Key was pressed (not released)
				
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {				// T key: Enter chat mode
					Crissaegrim.getChatBox().enableTypingMode();
					return; // Don't process any more keys!
				} else if (Keyboard.getEventKey() == 41) {					// Backtick (`) key
					Crissaegrim.toggleDebugMode();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_TAB) {	// Tab key: toggle zoom
					Crissaegrim.toggleZoom();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_X) {		// X key: Enter door
					for (Entity entity : Crissaegrim.getBoard().getEntityList()) {
						if (!Crissaegrim.getPlayer().isBusy() && entity instanceof Door &&
								RectUtils.coordinateIsInRect(Crissaegrim.getPlayer().getPosition(), entity.getBounds())) {
							Door door = (Door)entity;
							Crissaegrim.getPlayer().getPosition().setAll(door.getDestinationCoordinate().getX(), door.getDestinationCoordinate().getY());
							playerMovementHelper.resetMovementRequests();
							Crissaegrim.setBoard(boardMap.get(door.getDestinationBoardName()));
							Crissaegrim.getBoard().preloadChunks();
						}
					}
				}
				
			}
		}
	}
	
	/**
	 * Count the number of frames per second.  Update the title bar with the count every second.
	 * @param millisSkipped The number of milliseconds skipped in the current frame
	 */
	public void updateFPS(long millisSkipped) {
		millisecondsSkipped += millisSkipped;
	    if (Thunderbrand.getTime() - lastFPSTitleUpdate > 1000) { // Update the title in one-second increments
	        GameInitializer.setWindowTitle("Milliseconds skipped: " + millisecondsSkipped);
	        millisecondsSkipped = 0; // Reset the milliseconds skipped
	        lastFPSTitleUpdate += 1000; // Add one second
	    }
	}
	
	private void drawGhosts() {
		synchronized (Crissaegrim.getGhosts()) {
			String currentBoardName = Crissaegrim.getBoard().getName();
			for (Entry<Integer, PlayerStatus> ghost : Crissaegrim.getGhosts().entrySet()) {
				if (ghost.getValue().getBoardName().equalsIgnoreCase(currentBoardName)) {
					drawGhost(ghost.getValue());
				}
			}
		}		
	}
	
	private void drawGhost(PlayerStatus ghost) {
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
	
	private void setupBoards() {
		boardMap = new HashMap<String, Board>();
		// Add all existing boards:
		List<String> boardNames = Arrays.asList("first_board", "second_board", "tower_of_preludes", "dawning");
		for (String boardName : boardNames) {
			boardMap.put(boardName, new Board(boardName));
		}
		
		// Temporarily set up entities since it's not set up in Badelaire yet:
		
		//boardMap.get("first_board").getEntityList().add(new Target(new Coordinate(10050, 10010)));
		boardMap.get("first_board").getEntityList().add(new Door(new Coordinate(10028.5, 10001), "second_board", new Coordinate(10041.5, 10002)));
		boardMap.get("second_board").getEntityList().add(new Door(new Coordinate(10041.5, 10002), "first_board", new Coordinate(10028.5, 10001)));
		
		boardMap.get("dawning").getEntityList().add(new Door(new Coordinate(10052.5, 10013), "tower_of_preludes", new Coordinate(10050.5, 10016)));
		boardMap.get("tower_of_preludes").getEntityList().add(new Door(new Coordinate(10050.5, 10016), "dawning", new Coordinate(10052.5, 10013)));
		
	}
	
}
