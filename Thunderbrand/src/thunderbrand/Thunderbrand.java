package thunderbrand;

import org.lwjgl.Sys;

public final class Thunderbrand {
	
	private static final boolean LINUX_BUILD = false; // Change to true when deploying to the VM server, keep false if on my Windows machine
	private static final int VALMANWAY_SERVER_PORT = 22112;
	
	public static boolean isLinuxBuild() { return LINUX_BUILD; }
	public static int getValmanwayServerPort() { return VALMANWAY_SERVER_PORT; }
	
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
