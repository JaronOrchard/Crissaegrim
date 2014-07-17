package board;

import geometry.Coordinate;
import gldrawer.GLDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crissaegrim.Crissaegrim;
import thunderbrand.Thunderbrand;
import board.tiles.Tile.TileLayer;
import datapacket.RequestSpecificChunkPacket;

public class ClientBoard {
	
	public static void verifyChunksExist(Board board) {
		Coordinate playerPosition = Crissaegrim.getPlayer().getPosition();
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		// Get the coordinates of the Chunk the player is in:
		int chunkXOrigin = (int)playerPosition.getX() - ((int)playerPosition.getX() % chunkSideSize);
		int chunkYOrigin = (int)playerPosition.getY() - ((int)playerPosition.getY() % chunkSideSize);
		// Ensure that the current Chunk and the 8 around it exist:
		for (int xOrig = chunkXOrigin - chunkSideSize; xOrig <= chunkXOrigin + chunkSideSize; xOrig += chunkSideSize) {
			for (int yOrig = chunkYOrigin - chunkSideSize; yOrig <= chunkYOrigin + chunkSideSize; yOrig += chunkSideSize) {
				if (!board.getChunkMap().containsKey(xOrig + "_" + yOrig)) {
					// Chunk is missing!  Request and set to loading, then break out.
					Crissaegrim.addOutgoingDataPacket(new RequestSpecificChunkPacket(board.getName(), xOrig, yOrig));
					Crissaegrim.currentlyLoading = true;
					Crissaegrim.numPacketsReceived = 0;
					Crissaegrim.numPacketsToReceive = 0;
					System.out.println("Warning!  Chunk '" + board.getName() + "@" + xOrig + "_" + yOrig + "' was missing!"); 
					return;
				}
			}
		}
	}
	
	public static void draw(Board board, TileLayer layer) {
		Map<String, Chunk> chunkMap = board.getChunkMap();
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
		
		int xRange = (int)Math.ceil(Crissaegrim.getWindowWidthRadiusInTiles()) + 1;
		int yRange = (int)Math.ceil(Crissaegrim.getWindowHeightRadiusInTiles()) + 1;
		for (Chunk chunk : chunks) {
			if (chunk != null) {
				chunk.draw(playerPosition, layer, xRange, yRange);
			}
		}
	}
	
	public static void drawBackground(Board board) {
		GLDrawer.useTexture(board.getBackgroundTextureId());
		GLDrawer.drawQuad(0, Crissaegrim.getWindowWidth(), 0, Crissaegrim.getWindowHeight());
	}
	
}
