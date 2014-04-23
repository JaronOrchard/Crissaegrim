package badelaire;

import java.io.IOException;

public class Badelaire {
	
	private static final int WINDOW_WIDTH = 1536; //1024;
	private static final int WINDOW_HEIGHT = 768;
	
	public static int getWindowWidth() { return WINDOW_WIDTH; }
	public static int getWindowHeight() { return WINDOW_HEIGHT; }
	
	private static int pixelsPerTile = 16; // For best results, use multiples of 16
	private static double windowWidthRadiusInTiles = ((double)WINDOW_WIDTH / 2.0 / (double)pixelsPerTile);
	private static double windowHeightRadiusInTiles = ((double)WINDOW_HEIGHT / 2.0 / (double)pixelsPerTile);
	public static int getPixelsPerTile() { return pixelsPerTile; }
	public static double getWindowWidthRadiusInTiles() { return windowWidthRadiusInTiles; }
	public static double getWindowHeightRadiusInTiles() { return windowHeightRadiusInTiles; }
	
	public static void toggleZoom() {
		pixelsPerTile = (pixelsPerTile % 32) + 16;
		windowWidthRadiusInTiles = ((double)WINDOW_WIDTH / 2.0 / (double)pixelsPerTile);
		windowHeightRadiusInTiles = ((double)WINDOW_HEIGHT / 2.0 / (double)pixelsPerTile);
	}
	
	public static void main(String[] argv) throws IOException {
		try {
			Mapmaker mapmaker = new Mapmaker();
			mapmaker.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
