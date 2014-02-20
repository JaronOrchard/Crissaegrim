package board;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import textures.Textures;
import badelaire.Badelaire;
import board.tiles.MapmakerTile;
import board.tiles.MapmakerTileBlank;
import board.tiles.MapmakerTileUtils;

public class MapmakerChunk {
	private final String boardName;
	private final int xOrigin; // The origin of the chunk is
	private final int yOrigin; //   its bottom-left corner
	private MapmakerTile[][] tiles;
	public MapmakerTile[][] getTiles() { return tiles; }
	public MapmakerTile getTile(int x, int y) { return tiles[x][y]; }
	
	public int getXOrigin() { return xOrigin; }
	public int getYOrigin() { return yOrigin; }
	
	public MapmakerChunk(String bName, int xOrig, int yOrig) {
		boardName = bName;
		xOrigin = xOrig;
		yOrigin = yOrig;
		tiles = new MapmakerTile[Badelaire.getChunkSideSize()][Badelaire.getChunkSideSize()];
		for (int i = 0; i < Badelaire.getChunkSideSize(); i++) {
			for (int j = 0; j < Badelaire.getChunkSideSize(); j++) {
				tiles[i][j] = new MapmakerTileBlank();
			}
		}
	}
	
	public void setTile(int tileX, int tileY, MapmakerTile tile) { tiles[tileX][tileY] = tile; }
	
	public void draw(boolean drawGrid, boolean drawBG, boolean drawMG, boolean drawFG, boolean drawDefaults) {
		if (drawGrid) {
			glDisable(GL_TEXTURE_2D);
			glColor3d(0.2, 0.2, 0.2);
			glBegin(GL_LINES);
				for (double i = xOrigin + 1; i < xOrigin + Badelaire.getChunkSideSize(); i += 1) {
					glVertex2d(i, (double)yOrigin);
					glVertex2d(i, (double)yOrigin + Badelaire.getChunkSideSize());
				}
				for (double j = yOrigin + 1; j < yOrigin + Badelaire.getChunkSideSize(); j += 1) {
					glVertex2d((double)xOrigin, j);
					glVertex2d((double)xOrigin + Badelaire.getChunkSideSize(), j);
				}
			glEnd();
			glColor3d(1.0, 1.0, 1.0);
			glEnable(GL_TEXTURE_2D);
		}
		
		double tileX, tileY;
		for (int i = 0; i < Badelaire.getChunkSideSize(); i++) {
			for (int j = 0; j < Badelaire.getChunkSideSize(); j++) {
				tileX = xOrigin + i;
				tileY = yOrigin + j;
				
				// Skip this iteration if the texture is empty:
				if ((!drawBG || tiles[i][j].getBackgroundTexture() == Textures.NONE) &&
						(!drawMG || tiles[i][j].getMiddlegroundTexture() == Textures.NONE) &&
						(!drawFG || tiles[i][j].getForegroundTexture() == Textures.NONE) &&
						(!drawDefaults || tiles[i][j].getDefaultTexture() == Textures.NONE)) { continue; }
				
				if (drawDefaults) {
					glPushMatrix();
						glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
						glBindTexture(GL_TEXTURE_2D, tiles[i][j].getDefaultTexture());
						glBegin(GL_QUADS);
							glTexCoord2d(0, 1);
							glVertex2d(tileX, tileY);
							glTexCoord2d(1, 1);
							glVertex2d(tileX + 1, tileY);
							glTexCoord2d(1, 0);
							glVertex2d(tileX + 1, tileY + 1);
							glTexCoord2d(0, 0);
							glVertex2d(tileX, tileY + 1);
						glEnd();
					glPopMatrix();
				}
				
				if (drawBG) {
					glPushMatrix();
						glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
						glBindTexture(GL_TEXTURE_2D, tiles[i][j].getBackgroundTexture());
						glBegin(GL_QUADS);
							glTexCoord2d(0, 1);
							glVertex2d(tileX, tileY);
							glTexCoord2d(1, 1);
							glVertex2d(tileX + 1, tileY);
							glTexCoord2d(1, 0);
							glVertex2d(tileX + 1, tileY + 1);
							glTexCoord2d(0, 0);
							glVertex2d(tileX, tileY + 1);
						glEnd();
					glPopMatrix();
				}
				
				if (drawMG) {
					glPushMatrix();
						glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
						glBindTexture(GL_TEXTURE_2D, tiles[i][j].getMiddlegroundTexture());
						glBegin(GL_QUADS);
							glTexCoord2d(0, 1);
							glVertex2d(tileX, tileY);
							glTexCoord2d(1, 1);
							glVertex2d(tileX + 1, tileY);
							glTexCoord2d(1, 0);
							glVertex2d(tileX + 1, tileY + 1);
							glTexCoord2d(0, 0);
							glVertex2d(tileX, tileY + 1);
						glEnd();
					glPopMatrix();
				}
				
				if (drawFG) {
					glPushMatrix();
						glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
						glBindTexture(GL_TEXTURE_2D, tiles[i][j].getForegroundTexture());
						glBegin(GL_QUADS);
							glTexCoord2d(0, 1);
							glVertex2d(tileX, tileY);
							glTexCoord2d(1, 1);
							glVertex2d(tileX + 1, tileY);
							glTexCoord2d(1, 0);
							glVertex2d(tileX + 1, tileY + 1);
							glTexCoord2d(0, 0);
							glVertex2d(tileX, tileY + 1);
						glEnd();
					glPopMatrix();
				}
			}
		}
		
		if (drawGrid) {
			glDisable(GL_TEXTURE_2D);
			glColor3d(0.5, 0.4, 0.1);
			glBegin(GL_LINE_LOOP);
				glVertex2d(xOrigin, yOrigin);
				glVertex2d(xOrigin + Badelaire.getChunkSideSize(), yOrigin);
				glVertex2d(xOrigin + Badelaire.getChunkSideSize(), yOrigin + Badelaire.getChunkSideSize());
				glVertex2d(xOrigin, yOrigin + Badelaire.getChunkSideSize());
			glEnd();
			glColor3d(1.0, 1.0, 1.0);
			glEnable(GL_TEXTURE_2D);
		}
	}
	
	/**
	 * Partially taken from http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
	 */
	public void save() {
		File chunksDir = new File("C:/CrissaegrimChunks/" + boardName);
		chunksDir.mkdirs();
		String chunkName = boardName + "@" + xOrigin + "_" + yOrigin;
		File chunkFile = new File(chunksDir, chunkName);
		
		// If ENTIRE chunk is blank, then delete it!  Else save it.
		boolean chunkIsEmpty = true;
		for (int i = 0; i < Badelaire.getChunkSideSize() && chunkIsEmpty; i++) {
			for (int j = 0; j < Badelaire.getChunkSideSize() && chunkIsEmpty; j++) {
				if (!(tiles[i][j] instanceof MapmakerTileBlank) ||
						tiles[i][j].getBackgroundTexture() != Textures.NONE ||
						tiles[i][j].getMiddlegroundTexture() != Textures.NONE ||
						tiles[i][j].getForegroundTexture() != Textures.NONE) {
					chunkIsEmpty = false;
				}
			}
		}
		
		if (chunkIsEmpty) {
			chunkFile.delete();
			System.out.println("Chunk " + chunkName + " DELETED");
			if (chunksDir.listFiles().length == 0) {
				chunksDir.delete();
				System.out.println("Chunk directory " + boardName + " DELETED");
			}
		} else {
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(chunkFile);
				fileOutputStream.write((byte)('0')); // Number of entities to follow (none yet) (will need to convert to bytes)
				fileOutputStream.write(System.getProperty("line.separator").getBytes());
				for (int i = 0; i < Badelaire.getChunkSideSize(); i++) {
					for (int j = 0; j < Badelaire.getChunkSideSize(); j++) {
						byte[] tileBytes = new byte[7];
						tileBytes[0] = (byte)(MapmakerTileUtils.getTileTypeInt(tiles[i][j]) & 0xFF);
						tileBytes[1] = (byte)((tiles[i][j].getBackgroundTexture() >> 8) & 0xFF);
						tileBytes[2] = (byte)(tiles[i][j].getBackgroundTexture() & 0xFF);
						tileBytes[3] = (byte)((tiles[i][j].getMiddlegroundTexture() >> 8) & 0xFF);
						tileBytes[4] = (byte)(tiles[i][j].getMiddlegroundTexture() & 0xFF);
						tileBytes[5] = (byte)((tiles[i][j].getForegroundTexture() >> 8) & 0xFF);
						tileBytes[6] = (byte)(tiles[i][j].getForegroundTexture() & 0xFF);
						fileOutputStream.write(tileBytes);
					}
				}
				System.out.println("Chunk " + chunkName + " saved!");
			} catch (IOException ex){
				ex.printStackTrace();
				System.out.println("Failed to save chunk " + chunkName + "!");
			} finally {
				try { fileOutputStream.close(); } catch (Exception ex) {}
			}
		}
	}
	
}
