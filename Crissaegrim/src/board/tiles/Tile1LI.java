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

public class Tile1LI extends Tile {
	
	public Tile1LI() {
		super(Textures.NONE, Textures.TILE_1LI, Textures.NONE);
	}
	
	public Tile1LI(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos + 1, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos + 1), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		
		List<Line> playerBoundaryLines = RectUtils.getLinesFromRect(PlayerUtils.getPlayerBodyRect(endingPosition));
		
		return LineUtils.lineSetsIntersect(tileBoundaryLines, playerBoundaryLines);
	}
	
	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		List<Line> tileBoundaryLines = new ArrayList<Line>();
		tileBoundaryLines.add(new Line(new Coordinate(xPos, yPos + 1), new Coordinate(xPos + 1, yPos + 1)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos + 1), new Coordinate(xPos + 1, yPos)));
		tileBoundaryLines.add(new Line(new Coordinate(xPos + 1, yPos), new Coordinate(xPos, yPos + 1)));
		
		List<Line> playerFeetLines = PlayerUtils.getPlayerFeetLines(endingPosition, includeHorizontalFeetLine);
		
		if (LineUtils.lineSetsIntersect(tileBoundaryLines, playerFeetLines)) {
			return raisePositionToAboveTile(xPos, yPos, endingPosition);
		} else {
			return null;
		}
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Coordinate position) {
		return new Coordinate(position.getX(), yPos + 1 + PlayerMovementHelper.getTileCollisionPadding());
	}
	
}
