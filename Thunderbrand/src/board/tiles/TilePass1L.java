package board.tiles;

import entities.Entity;
import geometry.Coordinate;
import geometry.CoordinateUtils;
import geometry.Line;
import geometry.LineUtils;
import geometry.RectUtils;

import java.util.Arrays;
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
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(entity.getEntityBodyRect(endingPosition));
		
		double slopeYatX = yPos + Math.max(Math.min(startingPosition.getX() - (double)xPos, 1), 0);
		
		return (startingPosition.getY() >= slopeYatX && LineUtils.lineSetsIntersect(getTileBoundaryLines(xPos, yPos), playerBoundaryLines));
	}
	
	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine, boolean onTheGround) {
		if (CoordinateUtils.isMovingUp(startingPosition, endingPosition)) { return null; }
		
		List<Line> playerFeetLines = entity.getEntityFeetLines(endingPosition, includeHorizontalFeetLine);
		
		double slopeYatX = yPos + Math.max(Math.min(startingPosition.getX() - (double)xPos, 1), 0);
		double playerYatX = startingPosition.getY();
		if (onTheGround && startingPosition.getY() < yPos) { playerYatX += entity.getFeetHeight(); }
		
		if (playerYatX >= slopeYatX && LineUtils.lineSetsIntersect(getTileBoundaryLines(xPos, yPos), playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, entity, endingPosition);
		} else {
			return null;
		}
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return new Coordinate(position.getX(), yPos + Math.max(Math.min(position.getX() - (double)xPos, 1), 0) + entity.getTileCollisionPadding());
	}
	
	@Override
	public boolean lineIntersectsTile(int xPos, int yPos, Line line) {
		double slopeYatX = yPos + Math.max(Math.min(line.getPoint1().getX() - (double)xPos, 1), 0);
		return (line.getPoint1().getY() >= slopeYatX && LineUtils.lineSetsIntersect(line, getTileBoundaryLines(xPos, yPos)));
	}
	
	@Override
	protected List<Line> getTileBoundaryLines(int xPos, int yPos) {
		return Arrays.asList(
				new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1)));
	}
	
}
