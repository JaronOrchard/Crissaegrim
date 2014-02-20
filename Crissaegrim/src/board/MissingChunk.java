package board;

import textures.Textures;
import board.tiles.TileBlank;
import board.tiles.TileFull;
import crissaegrim.Crissaegrim;

public class MissingChunk extends Chunk {
	
	public MissingChunk(int xOrig, int yOrig) {
		super(xOrig, yOrig);
		createMissingChunk();
	}
	
	private void createMissingChunk() {
		int chunkSideSize = Crissaegrim.getChunkSideSize();
		for (int i = 0; i < chunkSideSize; i++) {
			tiles[i][0] = new TileFull(Textures.NONE, Textures.CHUNK_NOT_FOUND, Textures.NONE);
			tiles[i][chunkSideSize-1] = new TileFull(Textures.NONE, Textures.CHUNK_NOT_FOUND, Textures.NONE);
		}
		for (int i = 1; i < chunkSideSize - 1; i++) {
			tiles[0][i] = new TileFull(Textures.NONE, Textures.CHUNK_NOT_FOUND, Textures.NONE);
			tiles[chunkSideSize-1][i] = new TileFull(Textures.NONE, Textures.CHUNK_NOT_FOUND, Textures.NONE);
		}
		for (int i = 1; i < chunkSideSize - 1; i++) {
			for (int j = 1; j < chunkSideSize - 1; j++) {
				tiles[i][j] = new TileBlank();
			}
		}
	}
	
}
