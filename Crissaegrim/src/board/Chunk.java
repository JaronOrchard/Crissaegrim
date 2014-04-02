package board;

import static org.lwjgl.opengl.GL11.glTexCoord2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import textures.Textures;
import crissaegrim.Coordinate;
import crissaegrim.Crissaegrim;
import crissaegrim.GameRunner.TileLayer;
import board.tiles.Tile;
import board.tiles.TileUtils;

public class Chunk {
	protected int xOrigin; // The origin of the chunk is
	protected int yOrigin; //   its bottom-left corner
	protected Tile[][] tiles;
	public Tile[][] getTiles() { return tiles; }
	public Tile getTile(int x, int y) { return tiles[x][y]; }
	
	public int getXOrigin() { return xOrigin; }
	public int getYOrigin() { return yOrigin; }
	
	public Chunk(int xOrig, int yOrig) {
		xOrigin = xOrig;
		yOrigin = yOrig;
		tiles = new Tile[Crissaegrim.getChunkSideSize()][Crissaegrim.getChunkSideSize()];
	}
	
	public Chunk(int xOrig, int yOrig, byte[] bytes) {
		xOrigin = xOrig;
		yOrigin = yOrig;
		tiles = new Tile[Crissaegrim.getChunkSideSize()][Crissaegrim.getChunkSideSize()];
		
		for (int x = 0; x < Crissaegrim.getChunkSideSize(); x++) {
			for (int y = 0; y < Crissaegrim.getChunkSideSize(); y++) {
				int base = (x * 7) + (y * 100 * 7);
				// The "& 0xff" basically converts signed bytes to unsigned to fix high textures.
				tiles[y][x] = TileUtils.getTileType(bytes[base]);
				tiles[y][x].setBackgroundTexture(((bytes[base+1] & 0xff) * 256) + (bytes[base+2] & 0xff));
				tiles[y][x].setMiddlegroundTexture(((bytes[base+3] & 0xff) * 256) + (bytes[base+4] & 0xff));
				tiles[y][x].setForegroundTexture(((bytes[base+5] & 0xff) * 256) + (bytes[base+6] & 0xff));
			}
		}
	}
	
	public void setTile(int tileX, int tileY, Tile tile) { tiles[tileX][tileY] = tile; }
	
	/**
	 * Draw the entire chunk.
	 * @param playerPosition The {@link Coordinate} of the center of vision, so we can avoid drawing unnecessary tiles
	 * @param layer Whether to draw the foreground, middleground, or background
	 */
	public void draw(Coordinate playerPosition, TileLayer layer) {
		Map<Integer, List<Coordinate>> tilesToDraw = new HashMap<Integer, List<Coordinate>>();
		double tileX, tileY;
		
		int startX, startY, endX, endY;
		int xRange = (int)Math.ceil(Crissaegrim.getWindowWidthRadiusInTiles()) + 1;
		int yRange = (int)Math.ceil(Crissaegrim.getWindowHeightRadiusInTiles()) + 1;
		
		int xOffset = (int)playerPosition.getX() - xOrigin;
		int yOffset = (int)playerPosition.getY() - yOrigin;
		if (coordinateIsInChunkColumn(playerPosition)) {
			startX = Math.max(xOffset - xRange, 0);
			endX = Math.min(xOffset + xRange, Crissaegrim.getChunkSideSize());
		} else {
			if (xOffset >= 0) {
				startX = Math.min(xOffset - xRange, Crissaegrim.getChunkSideSize());
				endX = Crissaegrim.getChunkSideSize();
			} else {
				startX = 0;
				endX = Math.max(xOffset + xRange, 0);
			}
		}
		if (coordinateIsInChunkRow(playerPosition)) {
			startY = Math.max(yOffset - yRange, 0);
			endY = Math.min(yOffset + yRange, Crissaegrim.getChunkSideSize());
		} else {
			if (yOffset >= 0) {
				startY = Math.min(yOffset - yRange, Crissaegrim.getChunkSideSize());
				endY = Crissaegrim.getChunkSideSize();
			} else {
				startY = 0;
				endY = Math.max(yOffset + yRange, 0);
			}
		}
		
		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++) {
				tileX = xOrigin + i;
				tileY = yOrigin + j;
				
				// Skip this iteration if the texture is empty:
				if ((layer == TileLayer.BACKGROUND && tiles[i][j].getBackgroundTexture() == Textures.NONE) ||
						(layer == TileLayer.MIDDLEGROUND && tiles[i][j].getMiddlegroundTexture() == Textures.NONE) ||
						(layer == TileLayer.FOREGROUND && tiles[i][j].getForegroundTexture() == Textures.NONE)) { continue; }
				
				int texture;
				if (layer == TileLayer.BACKGROUND) { texture = tiles[i][j].getBackgroundTexture(); }
				else if (layer == TileLayer.MIDDLEGROUND) { texture = tiles[i][j].getMiddlegroundTexture(); }
				else /*if (layer == TileLayer.FOREGROUND)*/ { texture = tiles[i][j].getForegroundTexture(); }
				
				if (!tilesToDraw.containsKey(texture)) {
					tilesToDraw.put(texture, new ArrayList<Coordinate>());
				}
				tilesToDraw.get(texture).add(new Coordinate(tileX, tileY));
			}
		}
		// Draw from a map so we only have to bind each different texture once
		for (Integer texture : tilesToDraw.keySet()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			for (Coordinate tileCoord : tilesToDraw.get(texture)) {
				GL11.glPushMatrix();
					GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
					GL11.glBegin(GL11.GL_QUADS);
						glTexCoord2d(0, 1);
						GL11.glVertex2d(tileCoord.getX(), tileCoord.getY());
						glTexCoord2d(1, 1);
						GL11.glVertex2d(tileCoord.getX() + 1, tileCoord.getY());
						glTexCoord2d(1, 0);
						GL11.glVertex2d(tileCoord.getX() + 1, tileCoord.getY() + 1);
						glTexCoord2d(0, 0);
						GL11.glVertex2d(tileCoord.getX(), tileCoord.getY() + 1);
					GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
	}
	
	private boolean coordinateIsInChunkColumn(Coordinate position) {
		return (!(position.getX() < xOrigin || position.getX() >= xOrigin + Crissaegrim.getChunkSideSize()));
	}
	
	private boolean coordinateIsInChunkRow(Coordinate position) {
		return (!(position.getY() < yOrigin || position.getY() >= yOrigin + Crissaegrim.getChunkSideSize()));
	}
	
}
