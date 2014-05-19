package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.CoordinateUtils;
import geometry.Rect;
import textures.Textures;

public class TilePassBottom extends Tile {
	
	public TilePassBottom() {
		super(Textures.NONE, Textures.TILE_PASS_BOTTOM, Textures.NONE);
	}
	
	public TilePassBottom(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		if (!CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return false; }
		
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = entity.getEntityBoundingRect(endingPosition);
		
		if (tileBoundingBox.getRight() < playerBodyBox.getLeft() ||
				tileBoundingBox.getLeft() > playerBodyBox.getRight() ||
				tileBoundingBox.getTop() < playerBodyBox.getBottom() ||
				tileBoundingBox.getBottom() > playerBodyBox.getTop()) {
			return false;
		}
		return (startingPosition.getY() + entity.getEntireHeight() < yPos && endingPosition.getY() + entity.getEntireHeight() >= yPos);
	}

	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return null; // Can't be pushed up by this tile
	}

	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return position; // Can't be pushed up by this tile
	}
	
}
