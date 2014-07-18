package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thunderbrand.Thunderbrand;
import board.tiles.CollisionDetectionTile;
import doodads.Doodad;
import geometry.Coordinate;

public class Board {
	
	protected Map<String, Chunk> chunkMap;
	protected final String boardName;
	private final int backgroundTextureId;
	
	private Map<Integer, Doodad> doodads;
	
	public Map<String, Chunk> getChunkMap() { return chunkMap; }
	public String getName() { return boardName; }
	public int getBackgroundTextureId() { return backgroundTextureId; }
	public Map<Integer, Doodad> getDoodads() { return doodads; }
	
	public Board(String bName) {
		chunkMap = new HashMap<String, Chunk>();
		boardName = bName;
		backgroundTextureId = BoardInfo.getBackgroundTextureIdForBoard(boardName);
		doodads = new HashMap<Integer, Doodad>();
	}
	
	/**
	 * @param position Current position
	 * @param xLeft The amount of tiles to also grab in the left direction
	 * @param xRight The amount of tiles to also grab in the right direction
	 * @param yDown The amount of tiles to also grab in the down direction
	 * @param yUp The amount of tiles to also grab in the up direction
	 * @return
	 * <code>(When 1, 1, 1, 4:)<br/>
	 * 15 16 17<br/>
	 * 12 13 14<br/>
	 * 09 10 11<br/>
	 * 06 07 08<br/>
	 * 03 04 05<br/>
	 * 00 01 02
	 * </code>
	 */
	public List<CollisionDetectionTile> getCollisionDetectionTilesNearPosition(Coordinate position, int xLeft, int xRight, int yDown, int yUp) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = (int)position.getX() - ((int)position.getX() % chunkSideSize);
		int chunkYOrigin = (int)position.getY() - ((int)position.getY() % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		Chunk chunk = chunkMap.get(chunkName);
		
		List<CollisionDetectionTile> nearbyTiles = new ArrayList<CollisionDetectionTile>();
		int localizedPlayerTileX = (int)position.getX() - chunk.getXOrigin();
		int localizedPlayerTileY = (int)position.getY() - chunk.getYOrigin();
		
		if (localizedPlayerTileX > 0 && localizedPlayerTileX < chunkSideSize - xRight &&
				localizedPlayerTileY > 0 && localizedPlayerTileY < chunkSideSize - yUp) {
			// Case 1: Player is well within a chunk
			for (int x = localizedPlayerTileX - xLeft; x <= localizedPlayerTileX + xRight; x++) {
				for (int y = localizedPlayerTileY - yDown; y <= localizedPlayerTileY + yUp; y++) {
					nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
				}
			}
		} else if ((localizedPlayerTileX == 0 || localizedPlayerTileX == chunkSideSize - xRight) &&
				localizedPlayerTileY > 0 && localizedPlayerTileY < chunkSideSize - yUp) {
			// Case 2: Player is on left or right edge of chunk, but isn't crossing vertical boundaries
			Chunk leftChunk = chunkMap.get((chunkXOrigin - chunkSideSize) + "_" + chunkYOrigin);
			Chunk rightChunk = chunkMap.get((chunkXOrigin + chunkSideSize) + "_" + chunkYOrigin);
			for (int x = localizedPlayerTileX - xLeft; x <= localizedPlayerTileX + xRight; x++) {
				for (int y = localizedPlayerTileY - yDown; y <= localizedPlayerTileY + yUp; y++) {
					if (x < 0) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, leftChunk.getTile(x + chunkSideSize, y)));
					} else if (x == chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, rightChunk.getTile(x - chunkSideSize, y)));
					} else {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
					}
				}
			}
		} else if (localizedPlayerTileX > 0 && localizedPlayerTileX < chunkSideSize - xRight &&
				(localizedPlayerTileY == 0 || localizedPlayerTileY >= chunkSideSize - yUp)) {
			// Case 3: Player is on top or bottom edge of chunk, but isn't crossing horizontal boundaries
			Chunk topChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin + chunkSideSize));
			Chunk bottomChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin - chunkSideSize));
			for (int x = localizedPlayerTileX - xLeft; x <= localizedPlayerTileX + xRight; x++) {
				for (int y = localizedPlayerTileY - yDown; y <= localizedPlayerTileY + yUp; y++) {
					if (y < 0) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, bottomChunk.getTile(x, y + chunkSideSize)));
					} else if (y >= chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, topChunk.getTile(x, y - chunkSideSize)));
					} else {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
					}
				}
			}
		} else { // Automatic combination of left/right and top/bottom cases
			// Case 4: Player is moving through a corner area of four chunks at once
			Chunk leftChunk = chunkMap.get((chunkXOrigin - chunkSideSize) + "_" + chunkYOrigin);
			Chunk rightChunk = chunkMap.get((chunkXOrigin + chunkSideSize) + "_" + chunkYOrigin);
			Chunk topChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin + chunkSideSize));
			Chunk bottomChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin - chunkSideSize));
			Chunk bottomLeftChunk = chunkMap.get((chunkXOrigin - chunkSideSize) + "_" + (chunkYOrigin - chunkSideSize));
			Chunk bottomRightChunk = chunkMap.get((chunkXOrigin + chunkSideSize) + "_" + (chunkYOrigin - chunkSideSize));
			Chunk topLeftChunk = chunkMap.get((chunkXOrigin - chunkSideSize) + "_" + (chunkYOrigin + chunkSideSize));
			Chunk topRightChunk = chunkMap.get((chunkXOrigin + chunkSideSize) + "_" + (chunkYOrigin + chunkSideSize));
			for (int x = localizedPlayerTileX - xLeft; x <= localizedPlayerTileX + xRight; x++) {
				for (int y = localizedPlayerTileY - yDown; y <= localizedPlayerTileY + yUp; y++) {
					if (x < 0 && y < 0) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, bottomLeftChunk.getTile(x + chunkSideSize, y + chunkSideSize)));
					} else if (x < 0 && y >= chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, topLeftChunk.getTile(x + chunkSideSize, y - chunkSideSize)));
					} else if (x < 0 /* && not crossing vertical boundary */) { 
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, leftChunk.getTile(x + chunkSideSize, y)));
					} else if (x == chunkSideSize && y < 0) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, bottomRightChunk.getTile(x - chunkSideSize, y + chunkSideSize)));
					} else if (x == chunkSideSize && y >= chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, topRightChunk.getTile(x - chunkSideSize, y - chunkSideSize)));
					} else if (x == chunkSideSize /* && not crossing vertical boundary */) { 
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, rightChunk.getTile(x - chunkSideSize, y)));
					} else if (y < 0 /* && not crossing horizontal boundary */) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, bottomChunk.getTile(x, y + chunkSideSize)));
					} else if (y >= chunkSideSize /* && not crossing horizontal boundary */) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, topChunk.getTile(x, y - chunkSideSize)));
					} else /* in middle chunk */ {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
					}
				}
			}
		}
		return nearbyTiles;
	}
	
}
