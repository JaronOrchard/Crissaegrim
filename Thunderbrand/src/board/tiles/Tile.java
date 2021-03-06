package board.tiles;

import java.util.List;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;

public abstract class Tile {
	
	public enum TileLayer { BACKGROUND, MIDDLEGROUND, FOREGROUND, DEFAULT }
	
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
	
	public Tile(int fgTexture, int mgTexture, int bgTexture) {
		foregroundTexture = fgTexture;
		middlegroundTexture = mgTexture;
		backgroundTexture = bgTexture;
		defaultTexture = mgTexture;
	}
	
	/**
	 * Checks to see if the {@link Player}'s head {@link Line} or body {@link Rect} collides with the given {@link Tile}.
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @param entity The player being checked
	 * @param startingPosition The player's starting {@link Coordinate}
	 * @param endingPosition The player's desired ending {@link Coordinate}
	 * @return {@code true} if the Player's head line or body rect collides with the given Tile, {@code false} otherwise
	 */
	public abstract boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition);
	
	/**
	 * Checks to see if the {@link Player}'s feet collide with the given {@link Tile}, returning
	 * a new, elevated {@link Coordinate} for the desired ending position if so
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @param entity The player being checked
	 * @param startingPosition The player's starting {@link Coordinate}
	 * @param endingPosition The player's desired ending Coordinate
	 * @param includeHorizontalFeetLine {@code true} if the player's horizontal feet line should be included in its feet,
	 * 		or {@code false} if only the vertical feet line should be considered
	 * @param onTheGround {@code true} if the player is on the ground, {@code false} otherwise or if jumping
	 * @return {@code null} if there was no collision, or a raised Coordinate if there was 
	 */
	public abstract Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine, boolean onTheGround);
	
	protected abstract Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position);
	
	/**
	 * Checks to see if a given {@link Line} intersects the lines of the given {@link Tile} type.
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @param line The Line to check for intersection
	 * @return {@code true} if the Line intersects the Tile, {@code false} otherwise
	 */
	public abstract boolean lineIntersectsTile(int xPos, int yPos, Line line);
	
	/**
	 * Returns a {@link List} of {@link Line}s that make up the edges of the given Tile type.
	 * @param xPos X position of the Tile in overall space
	 * @param yPos Y position of the Tile in overall space
	 * @return A List of Lines of the Tile's edges
	 */
	protected abstract List<Line> getTileBoundaryLines(int xPos, int yPos);
	
}
