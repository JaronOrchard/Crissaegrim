package thunderbrand;

import org.lwjgl.Sys;

public final class Thunderbrand {
	
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
