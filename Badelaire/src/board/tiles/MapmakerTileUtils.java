package board.tiles;

import java.util.Arrays;
import java.util.List;

public class MapmakerTileUtils {
	
	public static int getTileTypeInt(MapmakerTile tile) {
		if (tile instanceof MapmakerTileBlank)				return 1;
		if (tile instanceof MapmakerTile1L)					return 2;
		if (tile instanceof MapmakerTile1LI)				return 3;
		if (tile instanceof MapmakerTile1R)					return 4;
		if (tile instanceof MapmakerTile1RI)				return 5;
		if (tile instanceof MapmakerTile2L)					return 6;
		if (tile instanceof MapmakerTile2LH)				return 7;
		if (tile instanceof MapmakerTile2LHI)				return 8;
		if (tile instanceof MapmakerTile2LI)				return 9;
		if (tile instanceof MapmakerTile2R)					return 10;
		if (tile instanceof MapmakerTile2RH)				return 11;
		if (tile instanceof MapmakerTile2RHI)				return 12;
		if (tile instanceof MapmakerTile2RI)				return 13;
		if (tile instanceof MapmakerTileFull)				return 14;
		if (tile instanceof MapmakerTileHalfVertically)		return 15;
		if (tile instanceof MapmakerTileHalfVerticallyI)	return 16;
		if (tile instanceof MapmakerTilePassTop)			return 17;
		return 0;
	}
	
	public static MapmakerTile getTileType(int tileType) {
		if (tileType <= 1)	return new MapmakerTileBlank();
		if (tileType == 2)	return new MapmakerTile1L();
		if (tileType == 3)	return new MapmakerTile1LI();
		if (tileType == 4)	return new MapmakerTile1R();
		if (tileType == 5)	return new MapmakerTile1RI();
		if (tileType == 6)	return new MapmakerTile2L();
		if (tileType == 7)	return new MapmakerTile2LH();
		if (tileType == 8)	return new MapmakerTile2LHI();
		if (tileType == 9)	return new MapmakerTile2LI();
		if (tileType == 10)	return new MapmakerTile2R();
		if (tileType == 11)	return new MapmakerTile2RH();
		if (tileType == 12)	return new MapmakerTile2RHI();
		if (tileType == 13)	return new MapmakerTile2RI();
		if (tileType == 14)	return new MapmakerTileFull();
		if (tileType == 15)	return new MapmakerTileHalfVertically();
		if (tileType == 16)	return new MapmakerTileHalfVerticallyI();
		if (tileType == 17)	return new MapmakerTilePassTop();
		return new MapmakerTileBlank();		
	}
	
	private static List<MapmakerTile> selectableTiles = Arrays.asList(
			new MapmakerTileBlank(),
			new MapmakerTileFull(),
			new MapmakerTile1L(),
			new MapmakerTile1LI(),
			new MapmakerTile1R(),
			new MapmakerTile1RI(),
			new MapmakerTile2L(),
			new MapmakerTile2LH(),
			new MapmakerTile2LHI(),
			new MapmakerTile2LI(),
			new MapmakerTile2R(),
			new MapmakerTile2RH(),
			new MapmakerTile2RHI(),
			new MapmakerTile2RI(),
			new MapmakerTileHalfVertically(),
			new MapmakerTileHalfVerticallyI(),
			new MapmakerTilePassTop()
			);
	
	public static List<MapmakerTile> getSelectableTiles() { return selectableTiles; }
	
}
