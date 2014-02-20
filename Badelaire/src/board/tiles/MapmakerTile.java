package board.tiles;

public abstract class MapmakerTile {
	private int foregroundTexture;
	private int middlegroundTexture;
	private int backgroundTexture;
	private final int defaultTexture;
	
	public int getForegroundTexture() { return foregroundTexture; }
	public int getMiddlegroundTexture() { return middlegroundTexture; }
	public int getBackgroundTexture() { return backgroundTexture; }
	public int getDefaultTexture() { return defaultTexture; }
	
	public void setForegroundTexture(int tex) { foregroundTexture = tex; }
	public void setMiddlegroundTexture(int tex) { middlegroundTexture = tex; }
	public void setBackgroundTexture(int tex) { backgroundTexture = tex; }
	
	public MapmakerTile(int fgTexture, int mgTexture, int bgTexture) {
		foregroundTexture = fgTexture;
		middlegroundTexture = mgTexture;
		backgroundTexture = bgTexture;
		defaultTexture = mgTexture;
	}
	
}
