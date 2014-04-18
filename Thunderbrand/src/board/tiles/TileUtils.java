package board.tiles;


public class TileUtils {
	
	public static Tile getTileType(int tileType) {
		if (tileType <= 1)	return new TileBlank();
		if (tileType == 2)	return new Tile1L();
		if (tileType == 3)	return new Tile1LI();
		if (tileType == 4)	return new Tile1R();
		if (tileType == 5)	return new Tile1RI();
		if (tileType == 6)	return new Tile2L();
		if (tileType == 7)	return new Tile2LH();
		if (tileType == 8)	return new Tile2LHI();
		if (tileType == 9)	return new Tile2LI();
		if (tileType == 10)	return new Tile2R();
		if (tileType == 11)	return new Tile2RH();
		if (tileType == 12)	return new Tile2RHI();
		if (tileType == 13)	return new Tile2RI();
		if (tileType == 14)	return new TileFull();
		if (tileType == 15)	return new TileHalfVertically();
		if (tileType == 16)	return new TileHalfVerticallyI();
		if (tileType == 17)	return new TilePassTop();
		return new TileBlank();		
	}
	
}
