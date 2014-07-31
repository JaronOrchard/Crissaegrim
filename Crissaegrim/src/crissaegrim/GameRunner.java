package crissaegrim;

import items.Item;
import items.ItemPartyPopper;
import items.ItemPickaxe;
import items.LocalDroppedItem;
import items.Weapon;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import players.InventoryRunner;
import players.Player;
import datapacket.AttackPacket;
import datapacket.BoardDoodadsPacket;
import datapacket.ChunkPacket;
import datapacket.MineRockRequestPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.ParticleSystemPacket;
import datapacket.PlayerIsChangingBoardsPacket;
import datapacket.RequestPlayerIdPacket;
import datapacket.SendChatMessagePacket;
import dialogboxes.DialogBox;
import dialogboxes.DialogBoxRunner;
import doodads.Doodad;
import doodads.DoodadActions;
import doodads.Door;
import doodads.MineableRock;
import effects.ParticleSystem;
import entities.Entity;
import entities.EntityMovementHelper;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;
import gldrawer.GLDrawer;
import attack.Attack;
import board.Board;
import board.Chunk;
import board.ClientBoard;
import board.MissingChunk;
import board.tiles.Tile.TileLayer;
import busy.MiningRockBusy;
import busy.SwordSwingBusy;
import smithing.SmeltingRunner;
import textblock.TextBlock;
import textblock.TextTexture;
import textures.Textures;
import thunderbrand.TextUtils;
import thunderbrand.Thunderbrand;

public class GameRunner {
	
	public static final long FRAMES_PER_SECOND = 100;
	public static final long MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
	
	private long lastFPSTitleUpdate;
	private long millisecondsSkipped = 0;
	private short framesRendered = 0;
	
	private Map<String, Board> boardMap = new HashMap<String, Board>();
	private String destinationBoardName = "";
	private Coordinate destinationCoordinate = null;
	private List<TextBlock> waitingChatMessages = Collections.synchronizedList(new ArrayList<TextBlock>());
	private List<ParticleSystem> particleSystems = Collections.synchronizedList(new ArrayList<ParticleSystem>());
	private List<LocalDroppedItem> localDroppedItems = new ArrayList<LocalDroppedItem>();
	
	public void addWaitingChatMessage(TextBlock tb) { waitingChatMessages.add(tb); }
	public void addParticleSystem(ParticleSystem ps) { particleSystems.add(ps); }
	public void addLocalDroppedItem(LocalDroppedItem ldi) { localDroppedItems.add(ldi); }
	
	public void run() throws InterruptedException, IOException {
		GameInitializer.initializeDisplay();
		Textures.initializeTextures();
		Crissaegrim.initializePlayer(boardMap);
		Player player = Crissaegrim.getPlayer();
		EntityMovementHelper playerMovementHelper = player.getMovementHelper();
		
		Crissaegrim.getValmanwayConnection().connectToValmonwayServer();
		if (Crissaegrim.connectionStable) { // Wait to get player id...
			long lastSend = 0;
			while (player.getId() == -1) {
				if (Thunderbrand.getTime() - lastSend > Crissaegrim.getValmanwayConnection().getConnectionTimeoutMillis()) {
					lastSend = Thunderbrand.getTime();
					Crissaegrim.addOutgoingDataPacket(new RequestPlayerIdPacket(Crissaegrim.getClientVersion()));
				}
			}
		} else { // Couldn't connect + offline mode disallowed; display error and quit
			displayMessageForever(Textures.NO_CONNECTION_MESSAGE, 458, 64, null);
			return;
		}
		if (player.getId() == -2) { // playerId of -2 signifies outdated version
			displayMessageForever(Textures.CLIENT_OUTDATED_MESSAGE, 595, 102, "Your version is outdated!");
			return;
		}
		
		Crissaegrim.addSystemMessage("Welcome to Crissaegrim!");
		setNewDestinationToSpawn();
		requestTravelToDestinationBoard();
		
		// Set name to last used username if applicable:
		String lastUsername = Crissaegrim.getPreferenceHandler().getLastUsername();
		if (lastUsername != null) {
			Crissaegrim.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock("/setname " + lastUsername, Color.WHITE)));
		} else {
			Crissaegrim.addSystemMessage("Tip: You can use /setname to permanently set your username.");
		}
		
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
				ClientBoard.verifyChunksExist(Crissaegrim.getCurrentBoard());
				if (Crissaegrim.currentlyLoading) { continue; }
				player.update();
				setIconForDoodads();
				setIconForLocalDroppedItems();
				
				// Draw new scene:
				drawScene();
				
				// Get input and move the player:
				if (Crissaegrim.getChatBox().isTypingMode()) {
					Crissaegrim.getChatBox().getKeyboardInput(true);
				} else {
					getKeyboardAndMouseInput();
				}
				drawMouseHoverStatus();
				playerMovementHelper.moveEntityPre();
				Item itemToUse = playerMovementHelper.getItemToUse();
				if (itemToUse != null && !player.isBusy()) {
					// Cycle through relevant click-to-interact Doodads:
					for (Doodad doodad : Crissaegrim.getCurrentBoard().getDoodads().values()) {
						if (!player.isBusy() && RectUtils.coordinateIsInRect(player.getPosition(), doodad.getBounds())) {
							switch (doodad.getDoodadAction()) {
								case DoodadActions.MINE_ROCK:
									if (!(player.getInventory().getCurrentItem() instanceof ItemPickaxe)) {
										Crissaegrim.addSystemMessage("You need to be holding a pickaxe to mine this rock.");
									} else if (player.getInventory().isFull()) {
										new DialogBoxRunner().run(new DialogBox("Your inventory is full.", "Ok"));
									} else {
										MineableRock mineableRock = (MineableRock)doodad;
										if (!mineableRock.isDepleted()) {
											Crissaegrim.addSystemMessage("You start mining the rock...");
											player.setBusy(new MiningRockBusy(player.getPosition()));
											Crissaegrim.addOutgoingDataPacket(new MineRockRequestPacket(
													mineableRock.getId(), player.getId(), player.getBusy().getId(), player.getCurrentBoardName(), mineableRock.getChanceOfSuccess()));
										}
									}
									break;
								default:
									break;
							}
							break; // Found the relevant Doodad; ignore the rest
						}
					}
					// Use item if necessary:
					if (itemToUse instanceof Weapon) {
						// TODO: This should be split up depending upon the weapon and attack type
						// TODO: Bounding rect of sword swing should not be entire entity
						player.setBusy(new SwordSwingBusy());
						Crissaegrim.addOutgoingDataPacket(new AttackPacket(new Attack(
								player.getId(), player.getCurrentBoardName(), player.getSwordSwingRect(), ((Weapon)(itemToUse)).getAttackPower(), 1)));
					} else if (itemToUse instanceof ItemPartyPopper) {
						ItemPartyPopper popper = (ItemPartyPopper)(itemToUse);
						Crissaegrim.addOutgoingDataPacket(new ParticleSystemPacket(
								125, playerMovementHelper.getCoordinateClicked(), player.getCurrentBoardName(), popper.getColor()));
						popper.decrementUses();
						if (popper.getUsesLeft() <= 0) {
							player.getInventory().removeCurrentItem();
						}
					}
				}
				playerMovementHelper.moveEntityPost();
				
				drawHUD();
				
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
	
	public void drawScene() {
		GLDrawer.clear();
		Board currentBoard = Crissaegrim.getCurrentBoard();
		
		if (Crissaegrim.getCurrentBoard() != null) {
			GameInitializer.initializeNewFrameForWindow();
			ClientBoard.drawBackground(currentBoard);
			
			GameInitializer.initializeNewFrameForScene();
			ClientBoard.draw(currentBoard, TileLayer.BACKGROUND);
			ClientBoard.draw(currentBoard, TileLayer.MIDDLEGROUND);
			for (Doodad doodad : currentBoard.getDoodads().values()) {
				if (!Crissaegrim.getDebugMode()) {
					doodad.draw();
				} else {
					doodad.drawDebugMode();
				}
			}
			drawLocalDroppedItems();
			Crissaegrim.getPlayer().draw();
			if (Crissaegrim.getDebugMode()) {
				Crissaegrim.getPlayer().drawDebugMode();
			}
			drawGhosts();
			ClientBoard.draw(currentBoard, TileLayer.FOREGROUND);
			drawParticleSystems();
		}
		
		GameInitializer.initializeNewFrameForWindow();
		Crissaegrim.getPlayer().getInventory().draw();
		while (!waitingChatMessages.isEmpty()) {
			Crissaegrim.getChatBox().addChatMessage(waitingChatMessages.remove(0));
		}
		Crissaegrim.getChatBox().draw();
	}
	
	public void drawHUD() {
		drawPlayerHealthBar();
	}
	
	private void drawPlayerHealthBar() {
		int healthBarRight = Crissaegrim.getWindowWidth() - 10;
		int healthBarLeft = healthBarRight - 120;
		int healthBarBottom = 10;
		int healthBarTop = healthBarBottom + 25;
		double amtHealth = Crissaegrim.getPlayer().getHealthBar().getAmtHealth();
		int healthBarMiddle = (int)(((1.0 - amtHealth) * healthBarLeft) + (amtHealth * healthBarRight));
		
		GameInitializer.initializeNewFrameForWindow();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.812, 0.188, 0.188);
		GLDrawer.drawQuad(healthBarLeft, healthBarRight, healthBarBottom, healthBarTop); // Draw red backing
		GLDrawer.setColor(0.102, 0.533, 0.227);
		GLDrawer.drawQuad(healthBarLeft, healthBarMiddle, healthBarBottom, healthBarTop); // Draw green remaining
		GLDrawer.setColor(0.686, 0.741, 0.686);
		GLDrawer.setLineWidth(2);
		GLDrawer.drawOutline(healthBarLeft, healthBarRight, healthBarBottom, healthBarTop); // Draw outline
		GLDrawer.setLineWidth(1);
	}
	
	private void setIconForDoodads() {
		if (Crissaegrim.getPlayer().isBusy()) { return; }
		Iterator<Doodad> doodadIter = Crissaegrim.getCurrentBoard().getDoodads().values().iterator();
		while (doodadIter.hasNext()) {
			Doodad doodad = doodadIter.next();
			if (doodad.isActionable() && RectUtils.coordinateIsInRect(Crissaegrim.getPlayer().getPosition(), doodad.getBounds())) {
				if (doodad instanceof Door) {
					Crissaegrim.getPlayer().setIcon("F");
				} else if (doodad instanceof MineableRock) {
					Crissaegrim.getPlayer().setIcon("LEFT_CLICK");
				}
			}
		}
	}
	
	private void setIconForLocalDroppedItems() {
		Iterator<LocalDroppedItem> localDroppedItemsIter = localDroppedItems.iterator();
		while (localDroppedItemsIter.hasNext()) {
			LocalDroppedItem localDroppedItem = localDroppedItemsIter.next();
			if (!Crissaegrim.getPlayer().isBusy() && Crissaegrim.getPlayer().getCurrentBoardName().equals(localDroppedItem.getBoardName()) &&
					RectUtils.rectsOverlap(Crissaegrim.getPlayer().getEntityEntireRect(), localDroppedItem.getBounds())) {
				Crissaegrim.getPlayer().setIcon("F");
			}
		}
	}
	
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
				int pressedKey = Keyboard.getEventKey();
				
//				if (pressedKey == Keyboard.KEY_B) {
//					DialogBoxRunner dbr = new DialogBoxRunner();
//					DialogBox.Result res = dbr.run(new DialogBox(
//							Arrays.asList("A wild dialog box appeared!", "Do you want to set this location as your respawn point?"),
//							Arrays.asList("Yes", "No", "I-I don't know, I thought I heard a thump?")));
//					if (res == DialogBox.Result.BUTTON_1) { Crissaegrim.addSystemMessage("Cool."); }
//					else if (res == DialogBox.Result.BUTTON_2) { Crissaegrim.addSystemMessage("Fine!"); }
//					else if (res == DialogBox.Result.BUTTON_3) { Crissaegrim.addSystemMessage("P-President Fabio?"); }
//				}
				if (pressedKey == Keyboard.KEY_B)
					new SmeltingRunner().run();
				
				if (pressedKey == Keyboard.KEY_T ||
						pressedKey == Keyboard.KEY_RETURN) {	// T or Enter: Enter chat mode
					Crissaegrim.getChatBox().enableTypingMode();
					return; // Don't process any more keys!
				} else if (pressedKey == Keyboard.KEY_UP) {		// Up arrow: Select previous inventory item
					player.getInventory().selectPreviousItem();
				} else if (pressedKey == Keyboard.KEY_DOWN) {	// Down arrow: Select next inventory item
					player.getInventory().selectNextItem();
				} else if (pressedKey >= Keyboard.KEY_1 && pressedKey <= Keyboard.KEY_8) { // 1-8: Select inventory item
					player.getInventory().selectSpecificItem(pressedKey - Keyboard.KEY_1);
				} else if (pressedKey == 41) {					// Backtick (`) key
					Crissaegrim.toggleDebugMode();
				} else if (pressedKey == Keyboard.KEY_TAB) {	// Tab key: Toggle zoom
					Crissaegrim.toggleZoom();
				} else if (pressedKey == Keyboard.KEY_M) {		// M key: Toggle window size
					Crissaegrim.toggleWindowSize();
				} else if (pressedKey == Keyboard.KEY_E) {		// E key: Open inventory
					new InventoryRunner().run();
				} else if (pressedKey == Keyboard.KEY_F) {		// F key: Activate F-key doodads or pick up items
					boolean pickedUpAnItem = false;
					Iterator<LocalDroppedItem> localDroppedItemsIter = localDroppedItems.iterator();
					while (localDroppedItemsIter.hasNext()) {
						LocalDroppedItem localDroppedItem = localDroppedItemsIter.next();
						if (!player.isBusy() && player.getCurrentBoardName().equals(localDroppedItem.getBoardName()) &&
								RectUtils.rectsOverlap(Crissaegrim.getPlayer().getEntityEntireRect(), localDroppedItem.getBounds())) {
							if (player.getInventory().isFull()) {
								Crissaegrim.addSystemMessage("Your inventory is full.");
							} else {
								Item item = localDroppedItem.getItem();
								Crissaegrim.addSystemMessage("You picked up " + TextUtils.aOrAn(item.getName()) + " " + item.getName() + ".");
								player.getInventory().addItem(item);
								localDroppedItemsIter.remove();
								pickedUpAnItem = true;
							}
							break;
						}
					}
					if (!pickedUpAnItem) { // If the F key was not used to pick up an item, it will be used to activate a doodad
						for (Doodad doodad : Crissaegrim.getCurrentBoard().getDoodads().values()) {
							if (!player.isBusy() && RectUtils.coordinateIsInRect(player.getPosition(), doodad.getBounds())) {
								switch (doodad.getDoodadAction()) {
									case DoodadActions.GO_THROUGH_DOORWAY:
										Door door = (Door)doodad;
										pmh.resetMovementRequests();
										setNewDestination(door.getDestinationBoardName(), door.getDestinationCoordinate());
										requestTravelToDestinationBoard();
										break;
									default:
										break;
								}
								break; // Found the relevant Doodad; ignore the rest
							}
						}
					}
				}
			}
		}
		
		if (!Crissaegrim.getChatBox().isTypingMode()) {
			while (Mouse.next()) {
				if (Mouse.getEventButtonState()) { // Button was clicked (not released)
					if (Mouse.getEventButton() == 0) {
						pmh.requestUseItem(getCoordinatesForMouse(), Crissaegrim.getPlayer().getInventory().getCurrentItem());
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
	
	private void drawMouseHoverStatus() {
		Coordinate mouseCoords = getCoordinatesForMouse();
		String players = "";
		synchronized (Crissaegrim.getPlayerGhosts()) {
			for (EntityStatus ghost : Crissaegrim.getPlayerGhosts().values()) {
				Coordinate ghostCoordinate = new Coordinate(ghost.getXPos(), ghost.getYPos());
				Rect playerBounds = RectUtils.getPlayerBoundingRect(ghostCoordinate);
				RectUtils.expandRect(playerBounds, 0.6);
				if (RectUtils.coordinateIsInRect(mouseCoords, playerBounds)) {
					players += ", " + ghost.getName();
				}
			}
		}
		if (!players.isEmpty()) {
			TextTexture mouseHoverStatus = Crissaegrim.getCommonTextures().getTextTexture(players.substring(2));
			int top = Crissaegrim.getWindowHeight() - 5;
			GLDrawer.useTexture(mouseHoverStatus.getTextureId());
			GLDrawer.drawQuad(5, 5 + mouseHoverStatus.getWidth(), top - 20, top);
		}
	}
	
	/**
	 * @return A {@link Coordinate} in world coordinates for the spot where the mouse pointer is currently located
	 */
	public Coordinate getCoordinatesForMouse() {
		Player player = Crissaegrim.getPlayer();
		double pixelsPerTile = (double)Crissaegrim.getPixelsPerTile();
		double ptX = player.getPosition().getX() - (Crissaegrim.getWindowWidth() / 2 / pixelsPerTile) + (Mouse.getX() / pixelsPerTile);
		double ptY = player.getPosition().getY() - (Crissaegrim.getWindowHeight() / 2 / pixelsPerTile) + (Mouse.getY() / pixelsPerTile);
		return new Coordinate(ptX, ptY);
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
		synchronized (Crissaegrim.getNpcGhosts()) {
			for (EntityStatus ghost : Crissaegrim.getNpcGhosts().values()) {
				drawGhost(ghost);
			}
		}
		synchronized (Crissaegrim.getPlayerGhosts()) {
			for (EntityStatus ghost : Crissaegrim.getPlayerGhosts().values()) {
				drawGhost(ghost);
			}
		}
	}
	
	private void drawGhost(EntityStatus ghost) {
		// TODO: Don't draw if off-screen!
		boolean facingRight = ghost.getFacingRight();
		double xPos = ghost.getXPos();
		double yPos = ghost.getYPos();
		double textureHalfWidth = ghost.getTextureHalfWidth();
		double textureHeight = ghost.getTextureHeight();
		double left = xPos - textureHalfWidth;
		double right = xPos + textureHalfWidth;
		GLDrawer.useTexture(ghost.getCurrentTexture());
		GLDrawer.drawQuad((facingRight ? left : right), (facingRight ? right : left), yPos, yPos + textureHeight);
		
		Entity.drawMiniHealthBar(xPos, yPos + textureHeight, ghost.getAmtHealth());
	}
	
	private void drawParticleSystems() {
		synchronized (particleSystems) {
			Iterator<ParticleSystem> particleSystemIter = particleSystems.iterator();
			while (particleSystemIter.hasNext()) {
				ParticleSystem particleSystem = particleSystemIter.next();
				if (particleSystem.getBoardName().equals(Crissaegrim.getPlayer().getCurrentBoardName())) {
					particleSystem.draw();
				}
				if (particleSystem.update()) {
					particleSystemIter.remove();
				}
			}
		}
	}
	
	private void drawLocalDroppedItems() {
		Iterator<LocalDroppedItem> localDroppedItemsIter = localDroppedItems.iterator();
		while (localDroppedItemsIter.hasNext()) {
			LocalDroppedItem localDroppedItem = localDroppedItemsIter.next();
			if (localDroppedItem.getBoardName().equals(Crissaegrim.getPlayer().getCurrentBoardName())) {
				if (!Crissaegrim.getDebugMode()) {
					localDroppedItem.draw();
				} else {
					localDroppedItem.drawDebugMode();
				}
			}
			if (localDroppedItem.update()) {
				localDroppedItemsIter.remove();
			}
		}
	}
	
	
	private void drawLoadingMessage() {
		GameInitializer.initializeNewFrameForWindow();
		// Loading message texture size is 372 x 64
		GLDrawer.useTexture(Textures.LOADING_MESSAGE);
		GLDrawer.drawQuad(32, 404, Crissaegrim.getWindowHeight() - 96, Crissaegrim.getWindowHeight() - 32);
	}
	
	private void drawLoadingProgressBar() {
		if (Crissaegrim.numPacketsToReceive != 0) {
			// Loading message texture size is 372 x 64, so use that width...
			double amtLoaded = (double)Crissaegrim.numPacketsReceived / (double)Crissaegrim.numPacketsToReceive;
			int progressBarRight = (int)(((1.0 - amtLoaded) * 32.0) + (amtLoaded * 404));  
			GameInitializer.initializeNewFrameForWindow();
			GLDrawer.disableTextures();
			GLDrawer.setColor(0.15, 0.45, 0.15);
			GLDrawer.drawQuad(32, progressBarRight, Crissaegrim.getWindowHeight() - 136, Crissaegrim.getWindowHeight() - 106);
			GLDrawer.setColor(0.66, 0.66, 0.66);
			GLDrawer.setLineWidth(2);
			GLDrawer.drawOutline(32, 404, Crissaegrim.getWindowHeight() - 136, Crissaegrim.getWindowHeight() - 106);
			GLDrawer.setLineWidth(1);
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
	public void displayMessageForever(int texture, int width, int height, String optionalTitleChange) throws InterruptedException {
		if (optionalTitleChange != null && !optionalTitleChange.trim().isEmpty()) {
			GameInitializer.setWindowTitle(optionalTitleChange);
		}
		while (!Display.isCloseRequested()) {
			GameInitializer.initializeNewFrameForWindow();
			GLDrawer.clear();
			GLDrawer.useTexture(texture);
			GLDrawer.drawQuad(32, 32 + width, Crissaegrim.getWindowHeight() - 32 - height, Crissaegrim.getWindowHeight() - 32);
			
			Display.update();
			Thread.sleep(100);
		}
		Display.destroy();
	}
	
	public void setNewDestination(String destBoardName, Coordinate destCoordinate) {
		destinationBoardName = destBoardName;
		destinationCoordinate = destCoordinate;
	}
	
	public void setNewDestinationToSpawn() {
//		setNewDestination("tower_of_preludes", new Coordinate(10044, 10084));
//		setNewDestination("sotn_clock_tower", new Coordinate(10132, 10038));
//		setNewDestination("dawning", new Coordinate(10246, 10003));

//		setNewDestination("morriston", new Coordinate(10019, 10035));
//		setNewDestination("morriston", new Coordinate(10280, 9956));
		setNewDestination("morriston", new Coordinate(10110, 9994));
		
	}
	
	public void requestTravelToDestinationBoard() {
		if (!destinationBoardName.isEmpty() && destinationCoordinate != null) {
			Crissaegrim.addOutgoingDataPacket(new PlayerIsChangingBoardsPacket(destinationBoardName, !boardMap.containsKey(destinationBoardName)));
			Crissaegrim.currentlyLoading = true;
			Crissaegrim.numPacketsReceived = 0;
			Crissaegrim.numPacketsToReceive = 0;
		}
	}
	
	public void sendPlayerToDestinationBoard() {
		Crissaegrim.getPlayer().setCurrentBoardName(destinationBoardName);
		Crissaegrim.getPlayer().getPosition().setAll(destinationCoordinate);
		setNewDestination("", null);
		Crissaegrim.currentlyLoading = false;
		Crissaegrim.clearGhosts();
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
	
	public void setDoodadsForBoard(BoardDoodadsPacket bdp) {
		addBoardToMapIfNeeded(bdp.getBoardName());
		boardMap.get(bdp.getBoardName()).getDoodads().putAll(bdp.getDoodads());
	}
	
	private void addBoardToMapIfNeeded(String boardName) {
		if (!boardMap.containsKey(boardName)) {
			boardMap.put(boardName, new Board(boardName));
		}
	}
	
}
