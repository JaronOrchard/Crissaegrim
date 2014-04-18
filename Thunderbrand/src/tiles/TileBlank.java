package tiles;

import entities.Entity;
import geometry.Coordinate;
import textures.Textures;

public class TileBlank extends Tile {
	
	public TileBlank() {
		super(Textures.NONE, Textures.NONE, Textures.NONE);
	}
	
	public TileBlank(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition) {
		return false; // You can never collide with empty space
	}
	
	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Entity entity, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return null; // You can never collide with empty space
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Entity entity, Coordinate position) {
		return position; // You can never collide with empty space
	}
	
}
