package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import geometry.LineUtils;
import geometry.RectUtils;

import java.util.Arrays;
import java.util.List;

import textures.Textures;

public class Tile2L extends Tile {
	
	public Tile2L() {
		super(Textures.NONE, Textures.TILE_2L, Textures.NONE);
	}
	
	public Tile2L(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(entity.getEntityBodyRect(endingPosition));
		return LineUtils.lineSetsIntersect(getTileBoundaryLines(xPos, yPos), playerBoundaryLines);
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
		return new Coordinate(position.getX(), yPos + Math.max(Math.min((position.getX() - (double)xPos) / 2, 0.5), 0) + entity.getTileCollisionPadding());
	}
	
	@Override
	public boolean lineIntersectsTile(int xPos, int yPos, Line line) {
		return LineUtils.lineSetsIntersect(line, getTileBoundaryLines(xPos, yPos));
	}
	
	@Override
	protected List<Line> getTileBoundaryLines(int xPos, int yPos) {
		return Arrays.asList(
				new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos)),
				new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos + 1, yPos + 0.5)),
				new Line(new Coordinate(xPos + 1, yPos + 0.5), new Coordinate(xPos, yPos)));
	}
	
}
