package board.tiles;

import geometry.Coordinate;
import geometry.Line;
import geometry.LineUtils;
import geometry.RectUtils;

import java.util.ArrayList;
import java.util.List;

import players.Player;
import players.PlayerMovementHelper;
import players.PlayerUtils;
import textures.Textures;

public class Tile1R extends Tile {
	
	public Tile1R() {
		super(Textures.NONE, Textures.TILE_1R, Textures.NONE);
	}
	
	public Tile1R(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos, yPos)));
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(PlayerUtils.getPlayerBodyRect(endingPosition));
		
		return LineUtils.lineSetsIntersect(tileBoundaryLines, playerBoundaryLines);
	}
	
	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos, yPos)));
		
		List<Line> playerFeetLines = PlayerUtils.getPlayerFeetLines(endingPosition, includeHorizontalFeetLine);
		
		if (LineUtils.lineSetsIntersect(tileBoundaryLines, playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, endingPosition);
		} else {
			return null;
		}
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Coordinate position) {
		return new Coordinate(position.getX(), yPos + Math.max(Math.min(1 - (position.getX() - (double)xPos), 1), 0) + PlayerMovementHelper.getTileCollisionPadding());
	}
	
}
