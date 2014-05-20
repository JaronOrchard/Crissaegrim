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

public class TilePass2LH extends Tile {
	
	public TilePass2LH() {
		super(Textures.NONE, Textures.TILE_PASS_2LH, Textures.NONE);
	}
	
	public TilePass2LH(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		if (CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return false; }
		
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 0.5), new Coordinate(xPos + 1, yPos + 1)));
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(entity.getEntityBoundingRect(endingPosition));
		
		double slopeYatX = yPos + Math.max(Math.min(((startingPosition.getX() - (double)xPos) / 2) + 0.5, 1), 0.5);
		
		return (startingPosition.getY() >= slopeYatX && LineUtils.lineSetsIntersect(tileBoundaryLines, playerBoundaryLines));
	}
	
	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		if (CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return null; }
		
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 0.5), new Coordinate(xPos + 1, yPos + 1)));
		
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		
		double slopeYatX = yPos + Math.max(Math.min(((startingPosition.getX() - (double)xPos) / 2) + 0.5, 1), 0.5);
		double playerYatX = startingPosition.getY();
		if (startingPosition.getY() < yPos + 0.5) { playerYatX += entity.getFeetHeight(); }
		
		if (playerYatX >= slopeYatX && LineUtils.lineSetsIntersect(tileBoundaryLines, playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, entity, endingPosition);
		} else {
			return null;
		}
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return new Coordinate(position.getX(), yPos + Math.max(Math.min(((position.getX() - (double)xPos) / 2) + 0.5, 1), 0.5) + entity.getTileCollisionPadding());
	}
	
}
