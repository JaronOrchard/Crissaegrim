package board.tiles;

import java.util.Arrays;
import java.util.List;


public class TileUtils {
	
	public static int getTileTypeInt(Tile tile) {
		if (tile instanceof TileBlank)				return 1;
		if (tile instanceof Tile1L)					return 2;
		if (tile instanceof Tile1LI)				return 3;
		if (tile instanceof Tile1R)					return 4;
		if (tile instanceof Tile1RI)				return 5;
		if (tile instanceof Tile2L)					return 6;
		if (tile instanceof Tile2LH)				return 7;
		if (tile instanceof Tile2LHI)				return 8;
		if (tile instanceof Tile2LI)				return 9;
		if (tile instanceof Tile2R)					return 10;
		if (tile instanceof Tile2RH)				return 11;
		if (tile instanceof Tile2RHI)				return 12;
		if (tile instanceof Tile2RI)				return 13;
		if (tile instanceof TileFull)				return 14;
		if (tile instanceof TileHalfVertically)		return 15;
		if (tile instanceof TileHalfVerticallyI)	return 16;
		if (tile instanceof TilePassTop)			return 17;
		if (tile instanceof TilePassLeft)			return 18;
		if (tile instanceof TilePassRight)			return 19;
		if (tile instanceof TilePassBottom)			return 20;
		if (tile instanceof TilePass1L)				return 21;
		if (tile instanceof TilePass1R)				return 22;
		if (tile instanceof TilePass2L)				return 23;
		if (tile instanceof TilePass2LH)			return 24;
		if (tile instanceof TilePass2R)				return 25;
		if (tile instanceof TilePass2RH)			return 26;
		if (tile instanceof TilePassHalfVertically)	return 27;
		return 0;
	}
	
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
		if (tileType == 18)	return new TilePassLeft();
		if (tileType == 19) return new TilePassRight();
		if (tileType == 20) return new TilePassBottom();
		if (tileType == 21) return new TilePass1L();
		if (tileType == 22) return new TilePass1R();
		if (tileType == 23) return new TilePass2L();
		if (tileType == 24) return new TilePass2LH();
		if (tileType == 25) return new TilePass2R();
		if (tileType == 26) return new TilePass2RH();
		if (tileType == 27) return new TilePassHalfVertically();
		return new TileBlank();		
	}
		
	private static List<Tile> mapmakerSelectableTiles = Arrays.asList(
			new TileBlank(),
			new TileFull(),
			new Tile1L(),
			new Tile1LI(),
			new Tile1R(),
			new Tile1RI(),
			new Tile2L(),
			new Tile2LH(),
			new Tile2LHI(),
			new Tile2LI(),
			new Tile2R(),
			new Tile2RH(),
			new Tile2RHI(),
			new Tile2RI(),
			new TileHalfVertically(),
			new TileHalfVerticallyI(),
			new TilePassTop(),
			new TilePassLeft(),
			new TilePassRight(),
			new TilePassBottom(),
			new TilePass1L(),
			new TilePass1R(),
			new TilePass2L(),
			new TilePass2LH(),
			new TilePass2R(),
			new TilePass2RH(),
			new TilePassHalfVertically()
			);
	
	public static List<Tile> getMapmakerSelectableTiles() { return mapmakerSelectableTiles; }
	
}
