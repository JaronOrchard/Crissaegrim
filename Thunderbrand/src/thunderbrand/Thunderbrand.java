package thunderbrand;

import org.lwjgl.Sys;

import random.RandomNumbers;

public final class Thunderbrand {
	
	private static final boolean LINUX_BUILD = false; // Change to true when deploying to the VM server, keep false if on my Windows machine
	private static final int VALMANWAY_SERVER_PORT = 22112;
	private static final boolean START_WORLD_RUNNER = false; // If false, doesn't start the WorldRunner in Valmanway
	
	public static boolean isLinuxBuild() { return LINUX_BUILD; }
	public static int getValmanwayServerPort() { return VALMANWAY_SERVER_PORT; }
	public static boolean getStartWorldRunner() { return START_WORLD_RUNNER; }
	
	private static final RandomNumbers randomNumbers = new RandomNumbers();
	
	public static RandomNumbers getRandomNumbers() { return randomNumbers; }
	
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
