package world;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import datapacket.MineRockResultPacket;
import datapacket.UpdatedDoodadPacket;
import doodads.MineableRock;
import npc.NPC;
import npc.NPCChargingSpike;
import npc.NPCPhanto;
import npc.NPCSpikePit;
import npc.NPCStickNinja;
import npc.SpawnCondition;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import thunderbrand.Constants;
import thunderbrand.Thunderbrand;
import valmanway.Valmanway;
import actions.Action;
import actions.MineRockAction;
import actions.ReplenishRockAction;
import board.Board;
import board.BoardInfo;
import board.Chunk;

public class WorldRunner {
	
	private static final long FRAMES_PER_SECOND = 100;
	private static final long MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
	
	private long lastFPSTitleUpdate;
	private long millisecondsSkipped = 0;
	private short framesRendered = 0;
	
	public WorldRunner() {
		System.out.print("Loading world...");
		loadAllChunks();
		if (Thunderbrand.isLinuxBuild() || Thunderbrand.getCreateNPCs()) { createNPCs(); }
		System.out.println("done.");
	}
	
	public void run() throws InterruptedException {
		lastFPSTitleUpdate = Thunderbrand.getTime();
		long startTime, endTime, elaspedTime; // Per-loop times to keep FRAMES_PER_SECOND
		Map<String, List<NPC>> npcMap = Valmanway.getSharedData().getNPCs();
		PriorityQueue<Action> actionQueue = Valmanway.getSharedData().getActionQueue();
		while (true) {
			startTime = Thunderbrand.getTime();
			
			// Process Actions:
			synchronized (actionQueue) {
				while (actionQueue.peek() != null && actionQueue.peek().getActionTime() <= startTime) {
					processAction(actionQueue.remove());
				}
			}
			
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
			BoardInfo.addDoodadsToBoard(curBoard);
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
		
		npcMap.get("sotn_clock_tower").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10125, 10013)), new Coordinate(10115, 10013), "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10140, 10013)), new Coordinate(10154, 10013), "sotn_clock_tower"));
		npcMap.get("sotn_clock_tower").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10080, 10025)), new Coordinate(10090, 10025), "sotn_clock_tower"));
		
		npcMap.get("morriston").add(new NPCSpikePit(new SpawnCondition(new Coordinate(10124.5, 9976)), "morriston", 15));
		npcMap.get("morriston").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10180, 9969)), new Coordinate(10205, 9969), "morriston"));
		npcMap.get("morriston").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10205, 9969)), new Coordinate(10180, 9969), "morriston"));
		npcMap.get("morriston").add(new NPCChargingSpike(new SpawnCondition(new Coordinate(10185, 10017)), new Coordinate(10173, 10017), "morriston"));
		
		for (List<NPC> npcList : npcMap.values()) {
			for (NPC npc : npcList) {
				npc.respawn();
			}
		}
	}
	
	private void processAction(Action action) {
		if (action instanceof MineRockAction) {
			MineRockAction mra = (MineRockAction)(action);
			MineableRock mineableRock = (MineableRock)(Valmanway.getSharedData().getBoardMap().get(mra.getBoardName()).getDoodads().get(mra.getDoodadId()));
			if (mineableRock.isDepleted()) { // Another player has mined this rock already
				Valmanway.sendPacketToPlayer(mra.getPlayerId(), new MineRockResultPacket(false, mra.getDoodadId(), mra.getBusyId(), mra.getBoardName()));
			} else {
				if (Thunderbrand.getRandomNumbers().getDouble() < mra.getChanceOfSuccess()) { // Success!
					Valmanway.sendPacketToPlayer(mra.getPlayerId(), new MineRockResultPacket(true, mra.getDoodadId(), mra.getBusyId(), mra.getBoardName()));
				} else { // Failed; requeue action
					Valmanway.getSharedData().addActionToQueue(new MineRockAction(
							Constants.MILLIS_TO_MINE_A_ROCK, mra.getDoodadId(), mra.getPlayerId(), mra.getBusyId(), mra.getBoardName(), mra.getChanceOfSuccess()));
				}
			}
		} else if (action instanceof ReplenishRockAction) {
			ReplenishRockAction rra = (ReplenishRockAction)(action);
			MineableRock mineableRock = (MineableRock)(Valmanway.getSharedData().getBoardMap().get(rra.getBoardName()).getDoodads().get(rra.getDoodadId()));
			mineableRock.setHasOre(true);
			Valmanway.getSharedData().addDataPacket(new UpdatedDoodadPacket(rra.getBoardName(), mineableRock));
		}
		
	}
	
}
