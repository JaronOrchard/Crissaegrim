package board.tiles;

import java.util.List;

import players.Player;
import players.PlayerMovementHelper;
import players.PlayerUtils;
import crissaegrim.Coordinate;
import crissaegrim.Line;
import crissaegrim.LineUtils;
import crissaegrim.Rect;
import crissaegrim.RectUtils;
import textures.Textures;

public class TileFull extends Tile {
	
	public TileFull() {
		super(Textures.NONE, Textures.TILE_FULL, Textures.NONE);
	}
	
	public TileFull(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}

	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition) {
		Rect tileBoundingBox = new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1));
		Rect playerBodyBox = PlayerUtils.getPlayerBodyRect(endingPosition);
		
		return (!(tileBoundingBox.getRight() < playerBodyBox.getLeft() ||
				tileBoundingBox.getLeft() > playerBodyBox.getRight() ||
				tileBoundingBox.getTop() < playerBodyBox.getBottom() ||
				tileBoundingBox.getBottom() > playerBodyBox.getTop()));
	}

	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		List<Line> tileBoundingBoxLines = RectUtils.getLinesFromRect(new Rect(new Coordinate(xPos, yPos), new Coordinate(xPos + 1, yPos + 1)));
		List<Line> playerFeetLines = PlayerUtils.getPlayerFeetLines(endingPosition, includeHorizontalFeetLine);
		if (LineUtils.lineSetsIntersect(tileBoundingBoxLines, playerFeetLines)) {
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
