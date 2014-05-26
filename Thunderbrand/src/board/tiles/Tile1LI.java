package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import geometry.LineUtils;
import geometry.RectUtils;

import java.util.ArrayList;
import java.util.List;

import textures.Textures;

public class Tile1LI extends Tile {
	
	public Tile1LI() {
		super(Textures.NONE, Textures.TILE_1LI, Textures.NONE);
	}
	
	public Tile1LI(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos + 1, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos + 1), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(entity.getEntityBodyRect(endingPosition));
		
		return LineUtils.lineSetsIntersect(tileBoundaryLines, playerBoundaryLines);
	}
	
	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos + 1, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos + 1), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		
		if (LineUtils.lineSetsIntersect(tileBoundaryLines, playerFeetLines)) {
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
