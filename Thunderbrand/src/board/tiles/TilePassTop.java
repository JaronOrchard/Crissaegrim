package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import geometry.LineUtils;
import geometry.Rect;
import geometry.RectUtils;

import java.util.List;

import textures.Textures;

public class TilePassTop extends Tile {
	
	public TilePassTop() {
		super(Textures.NONE, Textures.TILE_PASS_TOP, Textures.NONE);
	}
	
	public TilePassTop(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = entity.getEntityBoundingRect(endingPosition);
		
		if (tileBoundingBox.getRight() < playerBodyBox.getLeft() ||
				tileBoundingBox.getLeft() > playerBodyBox.getRight() ||
				tileBoundingBox.getTop() < playerBodyBox.getBottom() ||
				tileBoundingBox.getBottom() > playerBodyBox.getTop()) {
			return false;
		}
		return (startingPosition.getY() > yPos + 1 && endingPosition.getY() < yPos + 1);
	}

	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		List<Line> tileBoundingBoxLines = RectUtils.getLinesFromRect(new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1)));
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		if (startingPosition.getY() > yPos + 1 && endingPosition.getY() < yPos + 1 && LineUtils.lineSetsIntersect(tileBoundingBoxLines, playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, entity, endingPosition);
		} else {
			return null;
		}
	}

	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return new Coordinate(position.getX(), yPos + 1 + entity.getTileCollisionPadding());
	}
	
}
