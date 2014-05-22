package board;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import geometry.Coordinate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import thunderbrand.Thunderbrand;
import badelaire.Badelaire;
import board.tiles.Tile;
import board.tiles.Tile.TileLayer;
import board.tiles.TileBlank;

public class MapmakerBoard {
	
	private final String boardName;
	
	private Map<String, MapmakerChunk> chunkMap;
	private Set<String> modifiedChunks;
	
	public Map<String, MapmakerChunk> getChunkMap() { return chunkMap; }
	
	public MapmakerBoard(String bName) {
		boardName = bName;
		chunkMap = new HashMap<String, MapmakerChunk>();
		modifiedChunks = new HashSet<String>();
		
		// Load all chunks for the current board:
		loadChunks();
	}
	
	private void loadChunks() {
		File chunksDir = new File("C:/CrissaegrimChunks/" + boardName);
		chunksDir.mkdirs();
		for (File f : chunksDir.listFiles()) {
			if (!f.isDirectory() && f.getName().startsWith(boardName + "@")) {
				loadChunkFile(f);
			}
		}
		System.out.println("~~~~~ (Load complete)");
		
		Map<String, MapmakerChunk> tempChunkMap = new HashMap<String, MapmakerChunk>();
		tempChunkMap.putAll(chunkMap);
		for (Map.Entry<String, MapmakerChunk> entry : tempChunkMap.entrySet()) {
		    createBoundaryChunksIfNeeded(
		    		Integer.parseInt(entry.getKey().substring(0, entry.getKey().indexOf("_"))),
		    		Integer.parseInt(entry.getKey().substring(entry.getKey().indexOf("_") + 1)));
		}
		// If no chunks exist, make one:
		if (chunkMap.isEmpty()) {
			chunkMap.put(10000 + "_" + 10000, new MapmakerChunk(boardName, 10000, 10000));
		}
	}
	
	private void loadChunkFile(File f) {
		int chunkXOrigin = Integer.parseInt(f.getName().substring(f.getName().indexOf('@') + 1, f.getName().lastIndexOf('_')));
		int chunkYOrigin = Integer.parseInt(f.getName().substring(f.getName().lastIndexOf('_') + 1));
		MapmakerChunk chunk = null;
		
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(f);
			byte[] dummyEntitiesHeader = new byte[3];
			byte[] chunkData = new byte[70000];
			fin.read(dummyEntitiesHeader);
			fin.read(chunkData);
			chunk = new MapmakerChunk(boardName, chunkXOrigin, chunkYOrigin, chunkData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load chunk " + chunkXOrigin + "_" + chunkYOrigin + "!");
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		chunkMap.put(chunkXOrigin + "_" + chunkYOrigin, chunk);
		System.out.println("Chunk " + chunkXOrigin + "_" + chunkYOrigin + " loaded");
	}
	
	public void saveModifiedChunks() {
		for (String chunkName : modifiedChunks) {
			chunkMap.get(chunkName).save();
		}
		modifiedChunks.clear();
		System.out.println("~~~~~ (Save complete)");
	}
	
	/**
	 * Checks to see if the four chunks adjacent to the chunk at the given coordinates exists in the
	 * mapmaker, and creates it if not.
	 * @param chunkXOrigin The chunk's x origin
	 * @param chunkYOrigin The chunk's y origin
	 */
	private void createBoundaryChunksIfNeeded(int chunkXOrigin, int chunkYOrigin) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		if (!chunkMap.containsKey((chunkXOrigin - chunkSideSize) + "_" + chunkYOrigin)) {
			chunkMap.put((chunkXOrigin - chunkSideSize) + "_" + chunkYOrigin,
					new MapmakerChunk(boardName, chunkXOrigin - chunkSideSize, chunkYOrigin)); 
		}
		if (!chunkMap.containsKey((chunkXOrigin + chunkSideSize) + "_" + chunkYOrigin)) {
			chunkMap.put((chunkXOrigin + chunkSideSize) + "_" + chunkYOrigin,
					new MapmakerChunk(boardName, chunkXOrigin + chunkSideSize, chunkYOrigin)); 
		}
		if (!chunkMap.containsKey(chunkXOrigin + "_" + (chunkYOrigin - chunkSideSize))) {
			chunkMap.put(chunkXOrigin + "_" + (chunkYOrigin - chunkSideSize),
					new MapmakerChunk(boardName, chunkXOrigin, chunkYOrigin - chunkSideSize)); 
		}
		if (!chunkMap.containsKey(chunkXOrigin + "_" + (chunkYOrigin + chunkSideSize))) {
			chunkMap.put(chunkXOrigin + "_" + (chunkYOrigin + chunkSideSize),
					new MapmakerChunk(boardName, chunkXOrigin, chunkYOrigin + chunkSideSize)); 
		}
	}
	
	public void drawAll(Coordinate centerPosition, boolean drawGrid) { draw(centerPosition, drawGrid, true, true, true, false); }
	public void drawBG(Coordinate centerPosition, boolean drawGrid) { draw(centerPosition, drawGrid, true, false, false, false); }
	public void drawMG(Coordinate centerPosition, boolean drawGrid) { draw(centerPosition, drawGrid, false, true, false, false); }
	public void drawFG(Coordinate centerPosition, boolean drawGrid) { draw(centerPosition, drawGrid, false, false, true, false); }
	public void drawTileTypes(Coordinate centerPosition, boolean drawGrid) { draw(centerPosition, drawGrid, false, false, false, true); }
	
	private void draw(Coordinate centerPosition, boolean drawGrid, boolean drawBG, boolean drawMG, boolean drawFG, boolean drawDefaults) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glColor3d(1.0, 1.0, 1.0);
		int xRange = (int)Math.ceil(Badelaire.getWindowWidthRadiusInTiles()) + 1;
		int yRange = (int)Math.ceil(Badelaire.getWindowHeightRadiusInTiles()) + 1;
		for (Map.Entry<String, MapmakerChunk> entry : chunkMap.entrySet()) {
		    if (drawGrid) { entry.getValue().drawPreGrid(); }
			if (drawDefaults) { entry.getValue().draw(centerPosition, TileLayer.DEFAULT, xRange, yRange); }
			if (drawBG) { entry.getValue().draw(centerPosition, TileLayer.BACKGROUND, xRange, yRange); }
			if (drawMG) { entry.getValue().draw(centerPosition, TileLayer.MIDDLEGROUND, xRange, yRange); }
			if (drawFG) { entry.getValue().draw(centerPosition, TileLayer.FOREGROUND, xRange, yRange); }
		    if (drawGrid) { entry.getValue().drawPostGrid(); }
		}
	}
	
	public void setBG(int tileX, int tileY, int texture) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = tileX - (tileX % chunkSideSize);
		int chunkYOrigin = tileY - (tileY % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		MapmakerChunk chunk = chunkMap.get(chunkName);
		if (chunk != null) {
			chunk.getTile(tileX % chunkSideSize, tileY % chunkSideSize).setBackgroundTexture(texture);
			modifiedChunks.add(chunkName);
			createBoundaryChunksIfNeeded(chunkXOrigin, chunkYOrigin);
		}
	}
	
	public void setMG(int tileX, int tileY, int texture) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = tileX - (tileX % chunkSideSize);
		int chunkYOrigin = tileY - (tileY % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		MapmakerChunk chunk = chunkMap.get(chunkName);
		if (chunk != null) {
			chunk.getTile(tileX % chunkSideSize, tileY % chunkSideSize).setMiddlegroundTexture(texture);
			modifiedChunks.add(chunkName);
			createBoundaryChunksIfNeeded(chunkXOrigin, chunkYOrigin);
		}
	}
	
	public void setFG(int tileX, int tileY, int texture) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = tileX - (tileX % chunkSideSize);
		int chunkYOrigin = tileY - (tileY % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		MapmakerChunk chunk = chunkMap.get(chunkName);
		if (chunk != null) {
			chunk.getTile(tileX % chunkSideSize, tileY % chunkSideSize).setForegroundTexture(texture);
			modifiedChunks.add(chunkName);
			createBoundaryChunksIfNeeded(chunkXOrigin, chunkYOrigin);
		}
	}
	
	public void setTileType(int tileX, int tileY, Tile tile) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = tileX - (tileX % chunkSideSize);
		int chunkYOrigin = tileY - (tileY % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		MapmakerChunk chunk = chunkMap.get(chunkName);
		if (chunk != null) {
			Tile currentTile = chunk.getTile(tileX % chunkSideSize, tileY % chunkSideSize);
			if (currentTile.getMiddlegroundTexture() != currentTile.getDefaultTexture()) { // Non-default tile; keep its textures
				tile.setForegroundTexture(currentTile.getForegroundTexture());
				tile.setMiddlegroundTexture(currentTile.getMiddlegroundTexture());
				tile.setBackgroundTexture(currentTile.getBackgroundTexture());
			}
			chunk.setTile(tileX % chunkSideSize, tileY % chunkSideSize, tile);
			modifiedChunks.add(chunkName);
			createBoundaryChunksIfNeeded(chunkXOrigin, chunkYOrigin);
		}
	}
	
	public void resetTile(int tileX, int tileY) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = tileX - (tileX % chunkSideSize);
		int chunkYOrigin = tileY - (tileY % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		MapmakerChunk chunk = chunkMap.get(chunkName);
		if (chunk != null) {
			chunk.getTiles()[tileX % chunkSideSize][tileY % chunkSideSize] = new TileBlank();
		}
	}
	
}
