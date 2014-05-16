package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.CoordinateUtils;
import geometry.Rect;
import textures.Textures;

public class TilePassRight extends Tile {
	
	public TilePassRight() {
		super(Textures.NONE, Textures.TILE_PASS_RIGHT, Textures.NONE);
	}
	
	public TilePassRight(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		if (!CoordinateUtils.isMovingLeft(startingPosition, endingPosition)) { return false; }
		
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = entity.getEntityBoundingRect(endingPosition);
		
		if (tileBoundingBox.getRight() < playerBodyBox.getLeft() ||
				tileBoundingBox.getLeft() > playerBodyBox.getRight() ||
				tileBoundingBox.getTop() < playerBodyBox.getBottom() ||
				tileBoundingBox.getBottom() > playerBodyBox.getTop()) {
			return false;
		}
		return (startingPosition.getX() - entity.getBodyRadius() > xPos + 1 && endingPosition.getX() - entity.getBodyRadius() <= xPos + 1);
	}

	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return null; // Can't be pushed up by this tile
	}

	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return position; // Can't be pushed up by this tile
	}
	
}
