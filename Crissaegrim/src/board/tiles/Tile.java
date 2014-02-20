package board.tiles;

import players.Player;
import crissaegrim.Coordinate;
import crissaegrim.Rect;
import crissaegrim.Line;

public abstract class Tile {
	
	private int foregroundTexture;
	private int middlegroundTexture;
	private int backgroundTexture;
	
	public int getForegroundTexture() { return foregroundTexture; }
	public int getMiddlegroundTexture() { return middlegroundTexture; }
	public int getBackgroundTexture() { return backgroundTexture; }
	
	public void setForegroundTexture(int tex) { foregroundTexture = tex; }
	public void setMiddlegroundTexture(int tex) { middlegroundTexture = tex; }
	public void setBackgroundTexture(int tex) { backgroundTexture = tex; }
	
	public Tile(int fgTexture, int mgTexture, int bgTexture) {
		foregroundTexture = fgTexture;
		middlegroundTexture = mgTexture;
		backgroundTexture = bgTexture;
	}
	
	/**
	 * Checks to see if the {@link Player}'s head {@link Line} or body {@link Rect} collides with the given {@link Tile}.
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @param player The player being checked
	 * @param startingPosition The player's starting {@link Coordinate}
	 * @param endingPosition The player's desired ending {@link Coordinate}
	 * @return {@code true} if the Player's head line or body rect collides with the given Tile, {@code false} otherwise
	 */
	public abstract boolean playerBodyCollides(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition);
	
	/**
	 * Checks to see if the {@link Player}'s feet collide with the given {@link Tile}, returning
	 * a new, elevated {@link Coordinate} for the desired ending position if so
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @param player The player being checked
	 * @param startingPosition The player's starting {@link Coordinate}
	 * @param endingPosition The player's desired ending Coordinate
	 * @param includeHorizontalFeetLine {@code true} if the player's horizontal feet line should be included in its feet,
	 * 		or {@code false} if only the vertical feet line should be considered
	 * @return {@code null} if there was no collision, or a raised Coordinate if there was 
	 */
	public abstract Coordinate playerFeetCollide(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine);
	
	protected abstract Coordinate raisePositionToAboveTile(int xPos, int yPos, Coordinate position);
	
}
