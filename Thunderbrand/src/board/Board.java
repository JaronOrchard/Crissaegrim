package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thunderbrand.Thunderbrand;
import attack.Attack;
import board.tiles.CollisionDetectionTile;
import doodads.Doodad;
import geometry.Coordinate;

public class Board {
	
	protected Map<String, Chunk> chunkMap;
	protected final String boardName;
	
	private List<Attack> attackList;
	private List<Doodad> doodadList;
	
	public Map<String, Chunk> getChunkMap() { return chunkMap; }
	public String getName() { return boardName; }
	public List<Attack> getAttackList() { return attackList; }
	public List<Doodad> getDoodadList() { return doodadList; }
	
	public Board(String bName) {
		chunkMap = new HashMap<String, Chunk>();
		boardName = bName;
		attackList = new ArrayList<Attack>();
		doodadList = new ArrayList<Doodad>();
	}
	
	public List<CollisionDetectionTile> getCollisionDetectionTilesNearEntity(Coordinate entityPosition) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = (int)entityPosition.getX() - ((int)entityPosition.getX() % chunkSideSize);
		int chunkYOrigin = (int)entityPosition.getY() - ((int)entityPosition.getY() % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		Chunk chunk = chunkMap.get(chunkName);
		
		List<CollisionDetectionTile> nearbyTiles = new ArrayList<CollisionDetectionTile>();
		int localizedPlayerTileX = (int)entityPosition.getX() - chunk.getXOrigin();
		int localizedPlayerTileY = (int)entityPosition.getY() - chunk.getYOrigin();
		
		if (localizedPlayerTileX > 0 && localizedPlayerTileX < chunkSideSize - 1 &&
				localizedPlayerTileY > 0 && localizedPlayerTileY < chunkSideSize - 4) {
			// Case 1: Player is well within a chunk
			for (int x = localizedPlayerTileX - 1; x <= localizedPlayerTileX + 1; x++) {
				for (int y = localizedPlayerTileY - 1; y <= localizedPlayerTileY + 4; y++) {
					nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
				}
			}
		} else if ((localizedPlayerTileX == 0 || localizedPlayerTileX == chunkSideSize - 1) &&
				localizedPlayerTileY > 0 && localizedPlayerTileY < chunkSideSize - 4) {
			// Case 2: Player is on left or right edge of chunk, but isn't crossing vertical boundaries
			Chunk leftChunk = chunkMap.get((chunkXOrigin - chunkSideSize) + "_" + chunkYOrigin);
			Chunk rightChunk = chunkMap.get((chunkXOrigin + chunkSideSize) + "_" + chunkYOrigin);
			for (int x = localizedPlayerTileX - 1; x <= localizedPlayerTileX + 1; x++) {
				for (int y = localizedPlayerTileY - 1; y <= localizedPlayerTileY + 4; y++) {
					if (x == -1) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, leftChunk.getTile(x + chunkSideSize, y)));
					} else if (x == chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, rightChunk.getTile(x - chunkSideSize, y)));
					} else {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, chunk.getTile(x, y)));
					}
				}
			}
		} else if (localizedPlayerTileX > 0 && localizedPlayerTileX < chunkSideSize - 1 &&
				(localizedPlayerTileY == 0 || localizedPlayerTileY >= chunkSideSize - 4)) {
			// Case 3: Player is on top or bottom edge of chunk, but isn't crossing horizontal boundaries
			Chunk topChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin + chunkSideSize));
			Chunk bottomChunk = chunkMap.get(chunkXOrigin + "_" + (chunkYOrigin - chunkSideSize));
			for (int x = localizedPlayerTileX - 1; x <= localizedPlayerTileX + 1; x++) {
				for (int y = localizedPlayerTileY - 1; y <= localizedPlayerTileY + 4; y++) {
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
			for (int x = localizedPlayerTileX - 1; x <= localizedPlayerTileX + 1; x++) {
				for (int y = localizedPlayerTileY - 1; y <= localizedPlayerTileY + 4; y++) {
					if (x == -1 && y < 0) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, bottomLeftChunk.getTile(x + chunkSideSize, y + chunkSideSize)));
					} else if (x == -1 && y >= chunkSideSize) {
						nearbyTiles.add(new CollisionDetectionTile(chunk.getXOrigin() + x, chunk.getYOrigin() + y, topLeftChunk.getTile(x + chunkSideSize, y - chunkSideSize)));
					} else if (x == -1 /* && not crossing vertical boundary */) { 
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
