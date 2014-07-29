package thunderbrand;

import org.lwjgl.Sys;

import random.RandomNumbers;

public final class Thunderbrand {
	
	private static final boolean LINUX_BUILD = false; // Change to true when deploying to the VM server, keep false if on my Windows machine
	private static final int VALMANWAY_SERVER_PORT = 22112;
	private static final boolean CREATE_NPCS = true; // If false, the WorldRunner doesn't create NPCs in Valmanway
	
	public static boolean isLinuxBuild() { return LINUX_BUILD; }
	public static int getValmanwayServerPort() { return VALMANWAY_SERVER_PORT; }
	public static boolean getCreateNPCs() { return CREATE_NPCS; }
	
	private static final RandomNumbers randomNumbers = new RandomNumbers();
	public static RandomNumbers getRandomNumbers() { return randomNumbers; }
	
	// (Pretty sure these could all be consolidated safely, but would it become confusing...?)
	private static int nextDroppedItemId = 1;
	private static int nextDoodadId = 1;
	private static int nextBusyId = 1;
	public synchronized static int getNextDroppedItemId() { nextDroppedItemId++; return nextDroppedItemId - 1; }
	public synchronized static int getNextDoodadId() { nextDoodadId++; return nextDoodadId - 1; }
	public synchronized static int getNextBusyId() { nextBusyId++; return nextBusyId - 1; }
	
	private static final int CHUNK_SIDE_SIZE = 100;
	public static int getChunkSideSize() { return CHUNK_SIDE_SIZE; }
	
	/**
	 * Get the current time in milliseconds
	 * @return The current system time in milliseconds
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
