package board.tiles;

import entities.Entity;
import geometry.Coordinate;

public class CollisionDetectionTile {
	private final int xPos;
	private final int yPos;
	private final Tile tile;
	
	public CollisionDetectionTile(int xPos, int yPos, Tile tile) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.tile = tile;
	}
	
	public boolean entityBodyCollides(Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		return tile.playerBodyCollides(xPos, yPos, entity, startingPosition, endingPosition);
	}
	
	// Returns null if no collision, or a coordinate for a raised position if they do
	public Coordinate entityFeetCollide(Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return tile.playerFeetCollide(xPos, yPos, entity, startingPosition, endingPosition, includeHorizontalFeetLine);
	}
	
}
