package board;

import static org.lwjgl.opengl.GL11.*;
import gldrawer.GLDrawer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import textures.Textures;
import thunderbrand.Thunderbrand;
import board.tiles.TileBlank;
import board.tiles.TileUtils;

public class MapmakerChunk extends Chunk {
	private final String boardName;
	private boolean isEmpty;

	public MapmakerChunk(String bName, int xOrig, int yOrig) {
		super(xOrig, yOrig);
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		boardName = bName;
		for (int i = 0; i < chunkSideSize; i++) {
			for (int j = 0; j < chunkSideSize; j++) {
				tiles[i][j] = new TileBlank();
			}
		}
		isEmpty = true;
	}
	
	public MapmakerChunk(String bName, int xOrig, int yOrig, byte[] bytes) {
		super(xOrig, yOrig, bytes);
		boardName = bName;
		isEmpty = false;
	}
	
	public void drawPreGrid() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		GLDrawer.disableTextures();
		if (isEmpty) {
			GLDrawer.setColor(0.1, 0.15, 0.1);
		} else {
			GLDrawer.setColor(0.2, 0.2, 0.2);
		}
		glBegin(GL_LINES);
			for (double i = xOrigin + 1; i < xOrigin + chunkSideSize; i += 1) {
				glVertex2d(i, (double)yOrigin);
				glVertex2d(i, (double)yOrigin + chunkSideSize);
			}
			for (double j = yOrigin + 1; j < yOrigin + chunkSideSize; j += 1) {
				glVertex2d((double)xOrigin, j);
				glVertex2d((double)xOrigin + chunkSideSize, j);
			}
		glEnd();
	}
	
	public void drawPostGrid() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.5, 0.4, 0.1);
		GLDrawer.drawOutline(xOrigin, xOrigin + chunkSideSize, yOrigin, yOrigin + chunkSideSize);
	}
	
	/**
	 * Partially taken from http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
	 */
	public void save() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		File chunksDir = new File("C:/CrissaegrimChunks/" + boardName);
		chunksDir.mkdirs();
		String chunkName = boardName + "@" + xOrigin + "_" + yOrigin;
		File chunkFile = new File(chunksDir, chunkName);
		
		// If ENTIRE chunk is blank, then delete it!  Else save it.
		boolean chunkIsEmpty = true;
		for (int i = 0; i < chunkSideSize && chunkIsEmpty; i++) {
			for (int j = 0; j < chunkSideSize && chunkIsEmpty; j++) {
				if (!(tiles[i][j] instanceof TileBlank) ||
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
				for (int i = 0; i < chunkSideSize; i++) {
					for (int j = 0; j < chunkSideSize; j++) {
						byte[] tileBytes = new byte[7];
						tileBytes[0] = (byte)(TileUtils.getTileTypeInt(tiles[i][j]) & 0xFF);
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
	
	public void checkAndSetIsEmpty() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		isEmpty = true;
		for (int i = 0; i < chunkSideSize; i++) {
			for (int j = 0; j < chunkSideSize; j++) {
				if (!(tiles[i][j] instanceof TileBlank) ||
						tiles[i][j].getBackgroundTexture() != Textures.NONE ||
						tiles[i][j].getMiddlegroundTexture() != Textures.NONE ||
						tiles[i][j].getForegroundTexture() != Textures.NONE) {
					isEmpty = false;
					return;
				}
			}
		}
	}
	
}
