package board.tiles;

import geometry.Coordinate;
import players.Player;

public class CollisionDetectionTile {
	private final int xPos;
	private final int yPos;
	private final Tile tile;
	
	public CollisionDetectionTile(int xPos, int yPos, Tile tile) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.tile = tile;
	}
	
	public boolean playerBodyCollides(Player player, Coordinate startingPosition, Coordinate endingPosition) {
		return tile.playerBodyCollides(xPos, yPos, player, startingPosition, endingPosition);
	}
	
	// Returns null if no collision, or a coordinate for a raised position if they do
	public Coordinate playerFeetCollide(Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return tile.playerFeetCollide(xPos, yPos, player, startingPosition, endingPosition, includeHorizontalFeetLine);
	}
	
}
