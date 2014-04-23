package world;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import thunderbrand.Thunderbrand;
import valmanway.Valmanway;
import board.Board;
import board.Chunk;

public class WorldRunner {
	
	private static List<String> boards = Arrays.asList(
			"tower_of_preludes", "dawning"
	);
	
	public void run() {
		loadAllChunks();
	}
	
	private void loadAllChunks() {
		System.out.print("Loading world...");
		int chunkSizeSide = Thunderbrand.getChunkSideSize();
		for (String boardName : boards) {
			Board curBoard = new Board(boardName);
			File chunksDir = new File("C:/CrissaegrimChunks/" + boardName);
			for (File f : chunksDir.listFiles()) {
				if (!f.isDirectory() && f.getName().startsWith(boardName + "@")) {
					int chunkXOrigin = Integer.parseInt(f.getName().substring(f.getName().indexOf('@') + 1, f.getName().lastIndexOf('_')));
					int chunkYOrigin = Integer.parseInt(f.getName().substring(f.getName().lastIndexOf('_') + 1));
					byte[] bytes = new byte[70000];
					
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(f));
						int numEntities = Integer.parseInt(br.readLine());
						for (int i = 0; i < numEntities; i++) {
							br.readLine(); // Skip entities
						}
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						char[] tileChar = new char[7];
						for (int i = 0; i < chunkSizeSide; i++) {
							for (int j = 0; j < chunkSizeSide; j++) {
								br.read(tileChar, 0, 7);
								for (int k = 0; k < 7; k++) {
									outStream.write(tileChar[k]);
								}
							}
						}
						bytes = outStream.toByteArray();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Failed to load chunk " + chunkXOrigin + "_" + chunkYOrigin + "!");
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					curBoard.getChunkMap().put(chunkXOrigin + "_" + chunkYOrigin, new Chunk(chunkYOrigin, chunkYOrigin, bytes));
				}
			}
			Valmanway.getSharedData().getBoardMap().put(boardName, curBoard);
		}
		System.out.println("done.");
	}
	
}
