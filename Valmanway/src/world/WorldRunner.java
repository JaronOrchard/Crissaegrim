package world;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import npc.NPC;
import npc.NPCChargingSpike;
import npc.NPCPhanto;
import npc.NPCStickNinja;
import npc.SpawnCondition;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import thunderbrand.Thunderbrand;
import valmanway.Valmanway;
import board.Board;
import board.Chunk;

public class WorldRunner {
	
	private static final long FRAMES_PER_SECOND = 100;
	private static final long MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
	
	private long lastFPSTitleUpdate;
	private long millisecondsSkipped = 0;
	private short framesRendered = 0;
	
	public void run() throws InterruptedException {
		System.out.print("Loading world...");
		loadAllChunks();
		createNPCs();
		System.out.println("done.");
		
		lastFPSTitleUpdate = Thunderbrand.getTime();
		long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
		Map<String, List<NPC>> npcMap = Valmanway.getSharedData().getNPCs();
		while (true) {
			startTime = Thunderbrand.getTime();
			
			// Update all NPCs:
			synchronized (npcMap) {
				Iterator<List<NPC>> npcListIter = npcMap.values().iterator();
				while (npcListIter.hasNext()) {
					List<NPC> npcList = npcListIter.next();
					synchronized (npcList) {
						Iterator<NPC> npcIter = npcList.iterator();
						while (npcIter.hasNext()) {
							NPC npc = npcIter.next();
							npc.update();
							Valmanway.getSharedData().updateNpcStatusMap(npc.getId(), npc.getCurrentBoardName(),
									new EntityStatus(Valmanway.getSharedData().getPlayerName(npc.getId()),
											npc.getCurrentBoardName(), npc.getPosition().getX(), npc.getPosition().getY(),
											npc.getCurrentTexture(), npc.getFacingRight(), npc.getHealthBar().getAmtHealth(),
											npc.getTextureHalfWidth(), npc.getTextureHeight()));
						}
					}
				}
			}
			
			endTime = Thunderbrand.getTime();
			elaspedTime = endTime - startTime;
			Thread.sleep(Math.max(0, MILLISECONDS_PER_FRAME - elaspedTime));
			updateFPS(Math.max(0, MILLISECONDS_PER_FRAME - elaspedTime));
		}
	}
	
	public void updateFPS(long millisSkipped) {
		millisecondsSkipped += millisSkipped;
		framesRendered += 1;
		if (Thunderbrand.getTime() - lastFPSTitleUpdate > 10000) { // Update the title in ten-second increments
			System.out.println("FP10S: " + framesRendered + " | Millis skipped: " + (millisecondsSkipped / 10));
			framesRendered = 0; // Reset the frames rendered
			millisecondsSkipped = 0; // Reset the milliseconds skipped
			lastFPSTitleUpdate += 10000; // Add ten seconds
		}
	}
	
	private void loadAllChunks() {
		int chunkSizeSide = Thunderbrand.getChunkSideSize();
		for (String boardName : Valmanway.getBoardNames()) {
			Board curBoard = new Board(boardName);
			File chunksDir = new File((Thunderbrand.isLinuxBuild() ? "./" : "C:/") + "CrissaegrimChunks/" + boardName);
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
					curBoard.getChunkMap().put(chunkXOrigin + "_" + chunkYOrigin, new Chunk(chunkXOrigin, chunkYOrigin, bytes));
				}
			}
			Valmanway.getSharedData().getBoardMap().put(boardName, curBoard);
		}
	}
	
	private void createNPCs() {
		Map<String, List<NPC>> npcMap = Valmanway.getSharedData().getNPCs();
		
		npcMap.get("tower_of_preludes").add(new NPCStickNinja(new SpawnCondition(new Coordinate(10042, 10084), NPCStickNinja.getMillisToRespawn()), "tower_of_preludes", 1));
		npcMap.get("dawning").add(new NPCStickNinja(new SpawnCondition(new Coordinate(10050, 10023), NPCStickNinja.getMillisToRespawn()), "dawning", 2));
				
		npcMap.get("sotn_clock_tower").add(new NPCPhanto(new SpawnCondition(new Rect(new Coordinate(10091, 10036), new Coordinate(10152, 10042)), NPCPhanto.getMillisToRespawn()), "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCPhanto(new SpawnCondition(new Rect(new Coordinate(10091, 10036), new Coordinate(10152, 10042)), NPCPhanto.getMillisToRespawn()), "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCPhanto(new SpawnCondition(new Rect(new Coordinate(10091, 10036), new Coordinate(10152, 10042)), NPCPhanto.getMillisToRespawn()), "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCPhanto(new SpawnCondition(new Coordinate(10035, 10056), NPCPhanto.getMillisToRespawn()), "sotn_clock_tower"));
		
		npcMap.get("sotn_clock_tower").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10125, 10013)), new Coordinate(10115, 10013), false, "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10140, 10013)), new Coordinate(10154, 10013), true, "sotn_clock_tower"));
		
		
		for (List<NPC> npcList : npcMap.values()) {
			for (NPC npc : npcList) {
				npc.respawn();
			}
		}
	}
	
}
