package badelaire;

import static org.lwjgl.opengl.GL11.*;
import geometry.Coordinate;
import gldrawer.GLDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import doodads.Doodad;
import board.MapmakerBoard;
import board.MapmakerChunk;
import board.tiles.Tile;
import board.tiles.TileBlank;
import board.tiles.TileUtils;
import textures.Textures;
import thunderbrand.Thunderbrand;

public class Mapmaker {
	
	private static final int THREAD_SLEEP_MILLIS = 50; // Some number to let the mapmaker rest?
	private static final double PAN_AMOUNT = 3.0;
	private static final int RECENTLY_USED_CHOICES = 7; // First one is the current selection
	
	private int mode = 0; // 0 = View, 1 = BG, 2 = MG, 3 = FG, 4 = Tile types only
	private FillArea fillArea = new FillArea();
	private boolean showGrid = true;
	private boolean textureSelectionModeEnabled = false;
	private boolean tileTypeSelectionModeEnabled = false;
	
	private MapmakerBoard mapmakerBoard;
	private Coordinate center;
	
	private Tile currentTileType;
	private int currentTexture;
	private List<Tile> previouslyUsedTileTypes;
	private List<Integer> previouslyUsedTextures;
	private Tile replacementTile;
	private int replacementTexture;
	
	public Mapmaker() {
		previouslyUsedTileTypes = new ArrayList<Tile>();
		previouslyUsedTextures = new ArrayList<Integer>();
		for (int i = 0; i < RECENTLY_USED_CHOICES; i++) {
			previouslyUsedTileTypes.add(new TileBlank());
			previouslyUsedTextures.add(Textures.NONE);
		}
		currentTileType = new TileBlank();
		replacementTile = new TileBlank();
		currentTexture = Textures.NONE;
		replacementTexture = Textures.NONE;
	}
	
	/**
	 * Dangerous, important function.
	 * Used for a one-time remapping of texture values.
	 */
	@SuppressWarnings("unused")
	private void textureRemap() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		for (MapmakerChunk mc : mapmakerBoard.getChunkMap().values()) {
			for (int x = 0; x < chunkSideSize; x++) {
				for (int y = 0; y < chunkSideSize; y++) {
					if (mc.getTile(x, y).getBackgroundTexture() > 180 && mc.getTile(x, y).getBackgroundTexture() < 182)
						mc.getTile(x, y).setBackgroundTexture(172);
					if (mc.getTile(x, y).getMiddlegroundTexture() > 180 && mc.getTile(x, y).getMiddlegroundTexture() < 182)
						mc.getTile(x, y).setMiddlegroundTexture(172);
					if (mc.getTile(x, y).getForegroundTexture() > 180 && mc.getTile(x, y).getForegroundTexture() < 182)
						mc.getTile(x, y).setForegroundTexture(172);
					
				}
			}
			mc.save();
		}
	}
	
	public void run() throws InterruptedException, IOException {
		MapmakerInitializer.initializeDisplay();
		Textures.initializeTextures();
		MapmakerInitializer.initializeOpenGLFor2D();
		
		//mapmakerBoard = new MapmakerBoard("sotn_clock_tower");
		//mapmakerBoard = new MapmakerBoard("dawning_interior");
		//mapmakerBoard = new MapmakerBoard("dawning");
		//mapmakerBoard = new MapmakerBoard("tower_of_preludes");
		mapmakerBoard = new MapmakerBoard("morriston");
		//mapmakerBoard = new MapmakerBoard("barrett_station");
		
		//textureRemap(); if(true) return;
		
		center = new Coordinate(10050, 10000);
		updateDisplayTitle();
		
		while (!Display.isCloseRequested()) {
			
			// Draw new scene:
			drawScene();
			drawHUD();
			
			// Get input and move the player:
			getKeyboardAndMouseInput();
			
			Display.update();
			Thread.sleep(THREAD_SLEEP_MILLIS);
		}
		Display.destroy();
	}
	
	private void drawScene() {
		MapmakerInitializer.initializeNewFrameForScene(center);
		if (!textureSelectionModeEnabled && !tileTypeSelectionModeEnabled) {
			if (mode == 0 || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				mapmakerBoard.drawAll(center, showGrid);
			} else if (mode == 1) {
				mapmakerBoard.drawBG(center, showGrid);
			} else if (mode == 2) {
				mapmakerBoard.drawMG(center, showGrid);
			} else if (mode == 3) {
				mapmakerBoard.drawFG(center, showGrid);
			} else if (mode == 4) {
				mapmakerBoard.drawTileTypes(center, showGrid);
			}
			for (Doodad doodad : mapmakerBoard.getDoodads().values()) {
				doodad.draw();
			}
			if (!fillArea.isIdle()) {
				int tileX = (int)center.getX() - (Badelaire.getWindowWidth() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getX() / Badelaire.getPixelsPerTile());
				int tileY = (int)center.getY() - (Badelaire.getWindowHeight() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getY() / Badelaire.getPixelsPerTile());
				fillArea.draw(tileX, tileY);
			}
			
			if (showGrid) { // Draw Crissaegrim player view limits
				int crissaegrimWindowWidth = 1024;
				int crissaegrimWindowHeight = 768;
				int viewLimitLeft = (Badelaire.getWindowWidth() / 2) - (crissaegrimWindowWidth / 2);
				int viewLimitRight = viewLimitLeft + crissaegrimWindowWidth;
				int viewLimitBottom = (Badelaire.getWindowHeight() / 2) - (crissaegrimWindowHeight / 2);
				int viewLimitTop = viewLimitBottom + crissaegrimWindowHeight;
				int middleX = Badelaire.getWindowWidth() / 2;
				int middleY = Badelaire.getWindowHeight() / 2;
				MapmakerInitializer.initializeNewFrameForWindow();
				GLDrawer.disableTextures();
				GLDrawer.setColor(0, 0.4, 0.4);
				glBegin(GL_LINES);
					glVertex2d(viewLimitLeft, viewLimitBottom);
					glVertex2d(viewLimitLeft, viewLimitTop);
					
					glVertex2d(viewLimitRight, viewLimitBottom);
					glVertex2d(viewLimitRight, viewLimitTop);
					
					if (Badelaire.getWindowHeight() != crissaegrimWindowHeight) {
						glVertex2d(viewLimitLeft, viewLimitTop);
						glVertex2d(viewLimitRight, viewLimitTop);
						
						glVertex2d(viewLimitLeft, viewLimitBottom);
						glVertex2d(viewLimitRight, viewLimitBottom);
					}
					
					glVertex2d(middleX - 5, middleY - 5);
					glVertex2d(middleX + 5, middleY + 5);
					
					glVertex2d(middleX - 5, middleY + 5);
					glVertex2d(middleX + 5, middleY - 5);
				glEnd();
			}
		} else if (textureSelectionModeEnabled) {
			drawSelectableTextures();
		} else if (tileTypeSelectionModeEnabled) {
			drawSelectableTileTypes();
		}
	}
	
	private void drawHUD() {
		if (textureSelectionModeEnabled || tileTypeSelectionModeEnabled || mode == 0 ||
				Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			return;
		}
		MapmakerInitializer.initializeNewFrameForScene(center);
		double tileX = center.getX() - Badelaire.getWindowWidthRadiusInTiles() + 0.5;
		double tileY = center.getY() + Badelaire.getWindowHeightRadiusInTiles() - 1.5;
		
		// Draw recently used choices, including the current selection
		for (int i = 0; i < RECENTLY_USED_CHOICES; i++) {
			GLDrawer.useTexture(mode == 4 ? previouslyUsedTileTypes.get(i).getDefaultTexture() : previouslyUsedTextures.get(i));
			GLDrawer.drawQuad(tileX, tileX + 1, tileY, tileY + 1);
			tileX += 2;
		}
		
		// Draw replacement choice
		GLDrawer.useTexture(mode == 4 ? replacementTile.getDefaultTexture() : replacementTexture);
		GLDrawer.drawQuad(tileX, tileX + 1, tileY, tileY + 1);
		
		// Draw all lines
		tileX = center.getX() - Badelaire.getWindowWidthRadiusInTiles() + 0.5;
		GLDrawer.disableTextures();
		for (int i = 0; i < 1 + RECENTLY_USED_CHOICES; i++) {
			if (i == 0)								{ GLDrawer.setColor(0.7, 0.2, 0.2); } // Current selection
			else if (i == RECENTLY_USED_CHOICES)	{ GLDrawer.setColor(0.2, 0.7, 0.2); } // Replacement choice slot
			else									{ GLDrawer.setColor(0.2, 0.7, 0.7); } // Recently used
			GLDrawer.drawOutline(tileX, tileX + 1, tileY, tileY + 1);
			tileX += 2;
		}
	}
	
	private void drawSelectableTextures() {
		double tileX = center.getX() - Badelaire.getWindowWidthRadiusInTiles();
		double tileY = center.getY() + Badelaire.getWindowHeightRadiusInTiles() - 1;
		GLDrawer.clear();
		List<Integer> selectableTextures = Textures.getSelectableTextures();
		for (int i = 0; i < selectableTextures.size(); i++) {
			GLDrawer.useTexture(selectableTextures.get(i));
			GLDrawer.drawQuad(tileX, tileX + 1, tileY, tileY + 1);
			tileX += 1;
			if (i != 0 && (i+1) % (Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile()) == 0) {
				tileY -= 1;
				tileX -= (Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile());
			}
		}
		
		// Draw hovered texture in the next spot for clarity:
		int mouseTileX = Mouse.getX() / Badelaire.getPixelsPerTile();
		int mouseTileY = (Badelaire.getWindowHeight() / Badelaire.getPixelsPerTile()) - 1 - Mouse.getY() / Badelaire.getPixelsPerTile();
		int textureHovered = ((Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile()) * mouseTileY) + mouseTileX;
		if (textureHovered >= Textures.getSelectableTextures().size()) {
			textureHovered = Textures.NONE;
		} else {
			textureHovered = Textures.getSelectableTextures().get(textureHovered);
		}
		GLDrawer.useTexture(textureHovered);
		GLDrawer.drawQuad(tileX, tileX + 1, tileY, tileY + 1);
	}
	
	private void drawSelectableTileTypes() {
		double tileX = center.getX() - Badelaire.getWindowWidthRadiusInTiles();
		double tileY = center.getY() + Badelaire.getWindowHeightRadiusInTiles() - 1;
		GLDrawer.clear();
		List<Tile> selectableTiles = TileUtils.getMapmakerSelectableTiles();
		for (int i = 0; i < selectableTiles.size(); i++) {
			GLDrawer.useTexture(selectableTiles.get(i).getDefaultTexture());
			GLDrawer.drawQuad(tileX, tileX + 1, tileY, tileY + 1);
			tileX += 1;
			if (i != 0 && (i+1) % (Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile()) == 0) {
				tileY -= 1;
				tileX -= (Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile());
			}
		}
	}
	
	private void updateDisplayTitle() {
		StringBuilder sb = new StringBuilder("Badelaire [Mode: ");
		if (mode == 0) sb.append("<All> "); else sb.append("All ");
		if (mode == 1) sb.append("<BG> "); else sb.append("BG ");
		if (mode == 2) sb.append("<MG> "); else sb.append("MG ");
		if (mode == 3) sb.append("<FG> "); else sb.append("FG ");
		if (mode == 4) sb.append("<Types>"); else sb.append("Types" );
		sb.append("] [Grid: ");
		if (showGrid) sb.append("On"); else sb.append("Off");
		sb.append("]");
		if (mapmakerBoard.getModifiedChunksExist()) { sb.append(" (*)"); }
		Display.setTitle(sb.toString());
	}
	
	private void toggleShowGrid() { showGrid = !showGrid; }
	
	/**
	 * Detects keyboard and mouse input and acts accordingly.
	 * @throws InterruptedException 
	 */
	private void getKeyboardAndMouseInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) { center.incrementX(-PAN_AMOUNT); }
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) { center.incrementX(PAN_AMOUNT); }
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) { center.incrementY(PAN_AMOUNT); }
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) { center.incrementY(-PAN_AMOUNT); }
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { // Key was pressed (not released)
				int pressedKey = Keyboard.getEventKey();
				if (pressedKey == Keyboard.KEY_G) {
					toggleShowGrid();
					updateDisplayTitle();
				} else if (pressedKey == Keyboard.KEY_TAB && !textureSelectionModeEnabled && !tileTypeSelectionModeEnabled) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						mode = (mode + 5 - 1) % 5;
					} else {
						mode = (mode + 1) % 5;
					}
					updateDisplayTitle();
				} else if (pressedKey == Keyboard.KEY_RETURN) {
					mapmakerBoard.saveModifiedChunks();
				} else if (pressedKey == Keyboard.KEY_Z) { // Z key: toggle zoom
					Badelaire.toggleZoom();
				} else if (pressedKey == Keyboard.KEY_Q) { // Q key: toggle fill area
					int tileX = (int)center.getX() - (Badelaire.getWindowWidth() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getX() / Badelaire.getPixelsPerTile());
					int tileY = (int)center.getY() - (Badelaire.getWindowHeight() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getY() / Badelaire.getPixelsPerTile());
					fillArea.nextState(tileX, tileY);
				} else if (pressedKey == Keyboard.KEY_F) { // F key: Fill selected fill area
					if (fillArea.readyToFill()) {
						// This needs refactoring obvs.
						int leftmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint1TileX() : fillArea.getPoint2TileX();
						int rightmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint2TileX() : fillArea.getPoint1TileX();
						int topmostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint2TileY() : fillArea.getPoint1TileY();
						int bottommostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint1TileY() : fillArea.getPoint2TileY();
						for (int tileX = leftmostTile; tileX <= rightmostTile; tileX++) {
							for (int tileY = bottommostTile; tileY <= topmostTile; tileY++) {
								if (mode == 1) {
									mapmakerBoard.setBG(tileX, tileY, currentTexture);
								} else if (mode == 2) {
									mapmakerBoard.setMG(tileX, tileY, currentTexture);
								} else if (mode == 3) {
									mapmakerBoard.setFG(tileX, tileY, currentTexture);
								} else if (mode == 4) {
									try {
										mapmakerBoard.setTileType(tileX, tileY, currentTileType.getClass().getConstructor().newInstance());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						fillArea.nextState(0, 0);
					}
				} else if (pressedKey == Keyboard.KEY_C) {
					if (fillArea.readyToFill()) {
						// This needs refactoring obvs.
						int leftmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint1TileX() : fillArea.getPoint2TileX();
						int rightmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint2TileX() : fillArea.getPoint1TileX();
						int topmostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint2TileY() : fillArea.getPoint1TileY();
						int bottommostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint1TileY() : fillArea.getPoint2TileY();
						for (int tileX = leftmostTile; tileX <= rightmostTile; tileX++) {
							for (int tileY = bottommostTile; tileY <= topmostTile; tileY++) {
								mapmakerBoard.resetTile(tileX, tileY);
							}
						}
						fillArea.nextState(0, 0);
					}
				} else if (pressedKey == Keyboard.KEY_R) {
					if (fillArea.readyToFill()) {
						// This needs refactoring obvs.
						int leftmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint1TileX() : fillArea.getPoint2TileX();
						int rightmostTile = fillArea.getPoint1TileX() < fillArea.getPoint2TileX() ? fillArea.getPoint2TileX() : fillArea.getPoint1TileX();
						int topmostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint2TileY() : fillArea.getPoint1TileY();
						int bottommostTile = fillArea.getPoint1TileY() < fillArea.getPoint2TileY() ? fillArea.getPoint1TileY() : fillArea.getPoint2TileY();
						for (int tileX = leftmostTile; tileX <= rightmostTile; tileX++) {
							for (int tileY = bottommostTile; tileY <= topmostTile; tileY++) {
								if (mode == 1 && mapmakerBoard.getBG(tileX, tileY) == replacementTexture) {
									mapmakerBoard.setBG(tileX, tileY, currentTexture);
								} else if (mode == 2 && mapmakerBoard.getMG(tileX, tileY) == replacementTexture) {
									mapmakerBoard.setMG(tileX, tileY, currentTexture);
								} else if (mode == 3 && mapmakerBoard.getFG(tileX, tileY) == replacementTexture) {
									mapmakerBoard.setFG(tileX, tileY, currentTexture);
								} else if (mode == 4 && mapmakerBoard.getTileType(tileX, tileY) == TileUtils.getTileTypeInt(replacementTile)) {
									try {
										mapmakerBoard.setTileType(tileX, tileY, currentTileType.getClass().getConstructor().newInstance());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				} else if (pressedKey == Keyboard.KEY_E) {
					if (mode == 1 || mode == 2 || mode == 3) {
						textureSelectionModeEnabled = !textureSelectionModeEnabled;
					} else if (mode == 4) {
						tileTypeSelectionModeEnabled = !tileTypeSelectionModeEnabled;
					}
				} else if (!textureSelectionModeEnabled && !tileTypeSelectionModeEnabled &&
						pressedKey >= Keyboard.KEY_1 && pressedKey < Keyboard.KEY_1 + RECENTLY_USED_CHOICES - 1) {
					int recentChoice = pressedKey - Keyboard.KEY_1 + 1;
					if (mode == 4) {
						selectTileType(null, recentChoice);
					} else if (mode != 0) {
						selectTexture(0, recentChoice);
					}
				}
				updateDisplayTitle();
			}
		}
		
		if (!textureSelectionModeEnabled && !tileTypeSelectionModeEnabled) {
			if (Mouse.isButtonDown(0)) { // Left mouse button down
				int tileX = (int)center.getX() - (Badelaire.getWindowWidth() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getX() / Badelaire.getPixelsPerTile());
				int tileY = (int)center.getY() - (Badelaire.getWindowHeight() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getY() / Badelaire.getPixelsPerTile());
				if (mode == 1) {
					mapmakerBoard.setBG(tileX, tileY, currentTexture);
				} else if (mode == 2) {
					mapmakerBoard.setMG(tileX, tileY, currentTexture);
				} else if (mode == 3) {
					mapmakerBoard.setFG(tileX, tileY, currentTexture);
				} else if (mode == 4) {
					try {
						mapmakerBoard.setTileType(tileX, tileY, currentTileType.getClass().getConstructor().newInstance());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				updateDisplayTitle();
			} else if (Mouse.isButtonDown(1)) { // Right mouse button down
				int tileX = (int)center.getX() - (Badelaire.getWindowWidth() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getX() / Badelaire.getPixelsPerTile());
				int tileY = (int)center.getY() - (Badelaire.getWindowHeight() / 2 / Badelaire.getPixelsPerTile()) + (Mouse.getY() / Badelaire.getPixelsPerTile());
				System.out.println("(X, Y) = (" + tileX + ", " + tileY + ")");
				drawStickPlayer(tileX+0.5, tileY);
				drawStickPlayer(tileX+0.5, (double)(tileY) + 4.5);
			}
		} else if (textureSelectionModeEnabled) {
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				int tileX = Mouse.getX() / Badelaire.getPixelsPerTile();
				int tileY = (Badelaire.getWindowHeight() / Badelaire.getPixelsPerTile()) - 1 - Mouse.getY() / Badelaire.getPixelsPerTile();
				int indexSelected = ((Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile()) * tileY) + tileX;
				if (indexSelected >= Textures.getSelectableTextures().size()) {
					if (Mouse.isButtonDown(0)) { replacementTexture = Textures.NONE; }
					else { selectTexture(Textures.NONE, -1); }
				} else {
					if (Mouse.isButtonDown(0)) { replacementTexture = Textures.getSelectableTextures().get(indexSelected); }
					else { selectTexture(Textures.getSelectableTextures().get(indexSelected), -1); }
				}
				if (Mouse.isButtonDown(1)) { textureSelectionModeEnabled = false; }
			}
		} else if (tileTypeSelectionModeEnabled) {
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				int tileX = Mouse.getX() / Badelaire.getPixelsPerTile();
				int tileY = (Badelaire.getWindowHeight() / Badelaire.getPixelsPerTile()) - 1 - Mouse.getY() / Badelaire.getPixelsPerTile();
				int indexSelected = ((Badelaire.getWindowWidth() / Badelaire.getPixelsPerTile()) * tileY) + tileX;
				if (indexSelected >= TileUtils.getMapmakerSelectableTiles().size()) {
					if (Mouse.isButtonDown(0)) { replacementTile = new TileBlank(); }
					else { selectTileType(new TileBlank(), -1); }
				} else {
					if (Mouse.isButtonDown(0)) { replacementTile = TileUtils.getMapmakerSelectableTiles().get(indexSelected); }
					else { selectTileType(TileUtils.getMapmakerSelectableTiles().get(indexSelected), -1); }
				}
				if (Mouse.isButtonDown(1)) { tileTypeSelectionModeEnabled = false; }
			}
		}
	}
	
	private void selectTexture(int texture, int choiceFromRecent) {
		if (choiceFromRecent >= 1 && choiceFromRecent < RECENTLY_USED_CHOICES) { texture = previouslyUsedTextures.get(choiceFromRecent); }
		currentTexture = texture;
		for (int i = 0; i < RECENTLY_USED_CHOICES; i++) {
			if (previouslyUsedTextures.get(i) == texture) {
				previouslyUsedTextures.remove(i);
				break;
			}
		}
		previouslyUsedTextures.add(0, texture);
	}
	
	private void selectTileType(Tile tile, int choiceFromRecent) {
		if (choiceFromRecent >= 1 && choiceFromRecent < RECENTLY_USED_CHOICES) { tile = previouslyUsedTileTypes.get(choiceFromRecent); }
		currentTileType = tile;
		for (int i = 0; i < RECENTLY_USED_CHOICES; i++) {
			if (TileUtils.getTileTypeInt(previouslyUsedTileTypes.get(i)) == TileUtils.getTileTypeInt(tile)) {
				previouslyUsedTileTypes.remove(i);
				break;
			}
		}
		previouslyUsedTileTypes.add(0, tile);
	}
	
	private void drawStickPlayer(double x, double y) {
		MapmakerInitializer.initializeNewFrameForScene(center);
		GLDrawer.useTexture(Textures.STICK_PLAYER);
		GLDrawer.drawQuad(x - 1.5, x + 1.5, y, y + 3);
	}
	
}
