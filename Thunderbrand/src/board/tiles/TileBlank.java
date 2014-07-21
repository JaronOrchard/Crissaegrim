package board.tiles;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import geometry.Coordinate;
import geometry.Line;
import textures.Textures;

public class TileBlank extends Tile {
	
	public TileBlank() {
		super(Textures.NONE, Textures.NONE, Textures.NONE);
	}
	
	public TileBlank(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean entityBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		return false; // You can never collide with empty space
	}
	
	@Override
	public Coordinate entityFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine, boolean onTheGround) {
		return null; // You can never collide with empty space
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return position; // You can never collide with empty space
	}
	
	@Override
	public boolean lineIntersectsTile(int xPos, int yPos, Line line) {
		return false; // You can never collide with empty space
	}
	
	@Override
	protected List<Line> getTileBoundaryLines(int xPos, int yPos) {
		return new ArrayList<Line>(); // Empty space contains no boundary lines
	}
}
