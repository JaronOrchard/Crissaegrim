package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import textures.Textures;
import thunderbrand.Thunderbrand;
import static org.lwjgl.opengl.GL11.*;
import attack.Attack;
import board.tiles.CollisionDetectionTile;
import crissaegrim.Crissaegrim;
import crissaegrim.GameRunner.TileLayer;
import datapacket.RequestSpecificChunkPacket;
import doodads.Doodad;
import geometry.Coordinate;

public class Board {
	
	private Map<String, Chunk> chunkMap;
	private final String boardName;
	
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
	
	public void verifyChunksExist() {
		Coordinate playerPosition = Crissaegrim.getPlayer().getPosition();
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		// Get the coordinates of the Chunk the player is in:
		int chunkXOrigin = (int)playerPosition.getX() - ((int)playerPosition.getX() % chunkSideSize);
		int chunkYOrigin = (int)playerPosition.getY() - ((int)playerPosition.getY() % chunkSideSize);
		// Ensure that the current Chunk and the 8 around it exist:
		for (int xOrig = chunkXOrigin - chunkSideSize; xOrig <= chunkXOrigin + chunkSideSize; xOrig += chunkSideSize) {
			for (int yOrig = chunkYOrigin - chunkSideSize; yOrig <= chunkYOrigin + chunkSideSize; yOrig += chunkSideSize) {
				if (!chunkMap.containsKey(xOrig + "_" + yOrig)) {
					// Chunk is missing!  Request and set to loading, then break out.
					Crissaegrim.addOutgoingDataPacket(new RequestSpecificChunkPacket(boardName, xOrig, yOrig));
					Crissaegrim.currentlyLoading = true;
					System.out.println("Warning!  Chunk '" + boardName + "@" + xOrig + "_" + yOrig + "' was missing!"); 
					return;
				}
			}
		}
	}
	
	public void draw(TileLayer layer) {
		Coordinate playerPosition = Crissaegrim.getPlayer().getPosition();
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = (int)playerPosition.getX() - ((int)playerPosition.getX() % chunkSideSize);
		int chunkYOrigin = (int)playerPosition.getY() - ((int)playerPosition.getY() % chunkSideSize);
		List<Chunk> chunks = new ArrayList<Chunk>(9);
		chunks.add(chunkMap.get((chunkXOrigin-chunkSideSize) + "_" + (chunkYOrigin-chunkSideSize)));
		chunks.add(chunkMap.get((chunkXOrigin-chunkSideSize) + "_" + (chunkYOrigin)));
		chunks.add(chunkMap.get((chunkXOrigin-chunkSideSize) + "_" + (chunkYOrigin+chunkSideSize)));
		chunks.add(chunkMap.get((chunkXOrigin) + "_" + (chunkYOrigin-chunkSideSize)));
		chunks.add(chunkMap.get((chunkXOrigin) + "_" + (chunkYOrigin)));
		chunks.add(chunkMap.get((chunkXOrigin) + "_" + (chunkYOrigin+chunkSideSize)));
		chunks.add(chunkMap.get((chunkXOrigin+chunkSideSize) + "_" + (chunkYOrigin-chunkSideSize)));
		chunks.add(chunkMap.get((chunkXOrigin+chunkSideSize) + "_" + (chunkYOrigin)));
		chunks.add(chunkMap.get((chunkXOrigin+chunkSideSize) + "_" + (chunkYOrigin+chunkSideSize)));
		
		glColor3d(1.0, 1.0, 1.0);
		for (Chunk chunk : chunks) {
			if (chunk != null) {
				chunk.draw(playerPosition, layer);
			}
		}
	}
	
	public void drawBackground() {
		glColor3d(1.0, 1.0, 1.0);
		glBindTexture(GL_TEXTURE_2D, Textures.BACKGROUND_SOTN);
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 1);
				glVertex2d(0, 0);
				glTexCoord2d(1, 1);
				glVertex2d(Crissaegrim.getWindowWidth(), 0);
				glTexCoord2d(1, 0);
				glVertex2d(Crissaegrim.getWindowWidth(), Crissaegrim.getWindowHeight());
				glTexCoord2d(0, 0);
				glVertex2d(0, Crissaegrim.getWindowHeight());
			glEnd();
		glPopMatrix();
	}
	
	public List<CollisionDetectionTile> getCollisionDetectionTilesNearPlayer(Coordinate playerPosition) {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		int chunkXOrigin = (int)playerPosition.getX() - ((int)playerPosition.getX() % chunkSideSize);
		int chunkYOrigin = (int)playerPosition.getY() - ((int)playerPosition.getY() % chunkSideSize);
		String chunkName = chunkXOrigin + "_" + chunkYOrigin;
		Chunk chunk = chunkMap.get(chunkName);
		
		List<CollisionDetectionTile> nearbyTiles = new ArrayList<CollisionDetectionTile>();
		int localizedPlayerTileX = (int)playerPosition.getX() - chunk.getXOrigin();
		int localizedPlayerTileY = (int)playerPosition.getY() - chunk.getYOrigin();
		
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
