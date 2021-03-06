package board.tiles;

import java.util.Arrays;
import java.util.List;

import entities.Entity;
import geometry.Coordinate;
import geometry.CoordinateUtils;
import geometry.Line;
import geometry.LineUtils;
import geometry.Rect;
import geometry.RectUtils;
import textures.Textures;

public class TilePassRight extends Tile {
	
	public TilePassRight() {
		super(Textures.NONE, Textures.TILE_PASS_RIGHT, Textures.NONE);
	}
	
	public TilePassRight(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		if (!CoordinateUtils.isMovingLeft(startingPosition, endingPosition)) { return false; }
		
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = entity.getEntityEntireRect(endingPosition);
		
		if (!RectUtils.rectsOverlap(tileBoundingBox, playerBodyBox)) { return false; }
		return (startingPosition.getX() - entity.getBodyRadius() > xPos + 1 && endingPosition.getX() - entity.getBodyRadius() <= xPos + 1);
	}

	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine, boolean onTheGround) {
		return null; // Can't be pushed up by this tile
	}

	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return position; // Can't be pushed up by this tile
	}
	
	@Override
	public boolean lineIntersectsTile(int xPos, int yPos, Line line) {
		return (line.getPoint1().getX() > xPos + 1 && LineUtils.lineSetsIntersect(line, getTileBoundaryLines(xPos, yPos)));
	}
	
	@Override
	protected List<Line> getTileBoundaryLines(int xPos, int yPos) {
		return Arrays.asList(
				new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos + 1, yPos + 1)));
	}
	
}
