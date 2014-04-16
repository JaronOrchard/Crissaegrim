package board.tiles;

import geometry.Coordinate;
import players.Player;
import textures.Textures;

public class TileBlank extends Tile {
	
	public TileBlank() {
		super(Textures.NONE, Textures.NONE, Textures.NONE);
	}
	
	public TileBlank(int fgTexture, int mgTexture, int bgTexture) {
		super(fgTexture, mgTexture, bgTexture);
	}
	
	@Override
	public boolean playerBodyCollides(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition) {
		return false; // You can never collide with empty space
	}
	
	@Override
	public Coordinate playerFeetCollide(int xPos, int yPos, Player player, Coordinate startingPosition, Coordinate endingPosition, boolean includeHorizontalFeetLine) {
		return null; // You can never collide with empty space
	}
	
	@Override
	protected Coordinate raisePositionToAboveTile(int xPos, int yPos, Coordinate position) {
		return position; // You can never collide with empty space
	}
	
}
