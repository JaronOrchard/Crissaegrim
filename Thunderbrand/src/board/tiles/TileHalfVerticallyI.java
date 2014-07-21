package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import geometry.LineUtils;
import geometry.Rect;
import geometry.RectUtils;

import java.util.List;

import textures.Textures;

public class TileHalfVerticallyI extends Tile {
	
	public TileHalfVerticallyI() {
		super(Textures.NONE, Textures.TILE_HALF_VERTICALLY_I, Textures.NONE);
	}
	
	public TileHalfVerticallyI(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos + 0.5), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = entity.getEntityBodyRect(endingPosition);
		
		return (RectUtils.rectsOverlap(tileBoundingBox, playerBodyBox));
	}

	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine, boolean onTheGround) {
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		if (LineUtils.lineSetsIntersect(getTileBoundaryLines(xPos, yPos), playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, entity, endingPosition);
		} else {
			return null;
		}
	}

	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return new Coordinate(position.getX(), yPos + 1 + entity.getTileCollisionPadding());
	}
	
	@Override
	public boolean lineIntersectsTile(int xPos, int yPos, Line line) {
		return LineUtils.lineSetsIntersect(line, getTileBoundaryLines(xPos, yPos));
	}
	
	@Override
	protected List<Line> getTileBoundaryLines(int xPos, int yPos) {
		return RectUtils.getLinesFromRect(new Rect(new Coordinate(xPos, yPos + 0.5), new Coordinate(xPos + 1, yPos + 1)));
	}
	
}
