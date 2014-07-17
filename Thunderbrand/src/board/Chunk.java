package board;

import geometry.Coordinate;
import gldrawer.GLDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.tiles.Tile;
import board.tiles.TileUtils;
import board.tiles.Tile.TileLayer;
import textures.Textures;
import thunderbrand.Thunderbrand;

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
		tiles = new Tile[Thunderbrand.getChunkSideSize()][Thunderbrand.getChunkSideSize()];
	}
	
	public Chunk(int xOrig, int yOrig, byte[] bytes) {
		xOrigin = xOrig;
		yOrigin = yOrig;
		tiles = new Tile[Thunderbrand.getChunkSideSize()][Thunderbrand.getChunkSideSize()];
		
		for (int x = 0; x < Thunderbrand.getChunkSideSize(); x++) {
			for (int y = 0; y < Thunderbrand.getChunkSideSize(); y++) {
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
	 * @param centerPosition The {@link Coordinate} of the center of vision, so we can avoid drawing unnecessary tiles
	 * @param layer Whether to draw the foreground, middleground, or background
	 * @param xRange The number of tiles to draw, width-wise
	 * @param yRange The number of tiles to draw, height-wise
	 */
	public void draw(Coordinate centerPosition, TileLayer layer, int xRange, int yRange) {
		Map<Integer, List<Coordinate>> tilesToDraw = new HashMap<Integer, List<Coordinate>>();
		double tileX, tileY;
		
		int startX, startY, endX, endY;
		int xOffset = (int)centerPosition.getX() - xOrigin;
		int yOffset = (int)centerPosition.getY() - yOrigin;
		if (coordinateIsInChunkColumn(centerPosition)) {
			startX = Math.max(xOffset - xRange, 0);
			endX = Math.min(xOffset + xRange, Thunderbrand.getChunkSideSize());
		} else {
			if (xOffset >= 0) {
				startX = Math.min(xOffset - xRange, Thunderbrand.getChunkSideSize());
				endX = Thunderbrand.getChunkSideSize();
			} else {
				startX = 0;
				endX = Math.max(xOffset + xRange, 0);
			}
		}
		if (coordinateIsInChunkRow(centerPosition)) {
			startY = Math.max(yOffset - yRange, 0);
			endY = Math.min(yOffset + yRange, Thunderbrand.getChunkSideSize());
		} else {
			if (yOffset >= 0) {
				startY = Math.min(yOffset - yRange, Thunderbrand.getChunkSideSize());
				endY = Thunderbrand.getChunkSideSize();
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
				else if (layer == TileLayer.FOREGROUND) { texture = tiles[i][j].getForegroundTexture(); }
				else /*if (layer == TileLayer.DEFAULT)*/ { texture = tiles[i][j].getDefaultTexture(); }
				
				if (!tilesToDraw.containsKey(texture)) {
					tilesToDraw.put(texture, new ArrayList<Coordinate>());
				}
				tilesToDraw.get(texture).add(new Coordinate(tileX, tileY));
			}
		}
		
		// Draw from a map so we only have to bind each different texture once
		for (Integer texture : tilesToDraw.keySet()) {
			GLDrawer.useTexture(texture);
			for (Coordinate tileCoord : tilesToDraw.get(texture)) {
				GLDrawer.drawQuad(tileCoord.getX(), tileCoord.getX() + 1, tileCoord.getY(), tileCoord.getY() + 1);
			}
		}
	}
	
	private boolean coordinateIsInChunkColumn(Coordinate position) {
		return (!(position.getX() < xOrigin || position.getX() >= xOrigin + Thunderbrand.getChunkSideSize()));
	}
	
	private boolean coordinateIsInChunkRow(Coordinate position) {
		return (!(position.getY() < yOrigin || position.getY() >= yOrigin + Thunderbrand.getChunkSideSize()));
	}
	
}
