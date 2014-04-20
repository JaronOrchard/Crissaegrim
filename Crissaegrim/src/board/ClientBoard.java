package board;

import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glVertex2d;
import geometry.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crissaegrim.Crissaegrim;
import textures.Textures;
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
		glColor3d(1.0, 1.0, 1.0);
		for (Chunk chunk : chunks) {
			if (chunk != null) {
				chunk.draw(playerPosition, layer, xRange, yRange);
			}
		}
	}
	
	public static void drawBackground(Board board) {
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
	
}
