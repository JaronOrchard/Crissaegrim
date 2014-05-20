package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.CoordinateUtils;
import geometry.Line;
import geometry.LineUtils;
import geometry.RectUtils;

import java.util.ArrayList;
import java.util.List;

import textures.Textures;

public class TilePass1L extends Tile {
	
	public TilePass1L() {
		super(Textures.NONE, Textures.TILE_PASS_1L, Textures.NONE);
	}
	
	public TilePass1L(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		if (CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return false; }
		
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1)));
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(entity.getEntityBoundingRect(endingPosition));
		
		double slopeYatX = yPos + Math.max(Math.min(startingPosition.getX() - (double)xPos, 1), 0);
		
		return (startingPosition.getY() >= slopeYatX && LineUtils.lineSetsIntersect(tileBoundaryLines, playerBoundaryLines));
	}
	
	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		if (CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return null; }
		
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1)));
		
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		
		double slopeYatX = yPos + Math.max(Math.min(startingPosition.getX() - (double)xPos, 1), 0);
		double playerYatX = startingPosition.getY();
		if (startingPosition.getY() < yPos) { playerYatX += entity.getFeetHeight(); }
		
		if (playerYatX >= slopeYatX && LineUtils.lineSetsIntersect(tileBoundaryLines, playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, entity, endingPosition);
		} else {
			return null;
		}
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return new Coordinate(position.getX(), yPos + Math.max(Math.min(position.getX() - (double)xPos, 1), 0) + entity.getTileCollisionPadding());
	}
	
}
