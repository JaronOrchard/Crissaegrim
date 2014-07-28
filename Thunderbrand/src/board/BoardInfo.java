package board;

import java.util.Map;

import geometry.Coordinate;
import textures.Textures;
import thunderbrand.Thunderbrand;
import doodads.Doodad;
import doodads.Door;
import doodads.MineableRock;

public class BoardInfo {
	
	public static void addDoodadsToBoard(Board board) { addDoodadsToBoard(board.getName(), board.getDoodads()); }
	public static void addDoodadsToBoard(String boardName, Map<Integer, Doodad> doodadMap) {
		int id;
		if (boardName.equals("dawning")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10052.5, 10013), "tower_of_preludes", new Coordinate(10050.5, 10016)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10214.5, 10004), "dawning_interior", new Coordinate(10043.5, 10019)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10232.5, 10004), "dawning_interior", new Coordinate(10061.5, 10019)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10251.5, 10003), "dawning_interior", new Coordinate(10139.5, 10027)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10258.5, 10009), "dawning_interior", new Coordinate(10139.5, 10033)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10283.5, 9999), "sotn_clock_tower", new Coordinate(10177.5, 10033)));
		} else if (boardName.equals("dawning_interior")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10043.5, 10019), "dawning", new Coordinate(10214.5, 10004)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10061.5, 10019), "dawning", new Coordinate(10232.5, 10004)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10139.5, 10027), "dawning", new Coordinate(10251.5, 10003)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10139.5, 10033), "dawning", new Coordinate(10258.5, 10009)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10036.5, 10014), "dawning", new Coordinate(10207.5, 9999)));
		} else if (boardName.equals("tower_of_preludes")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10050.5, 10016), "dawning", new Coordinate(10052.5, 10013)));
		} else if (boardName.equals("sotn_clock_tower")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(9860.5, 10096), "dawning", new Coordinate(10280.5, 9992)));
		} else if (boardName.equals("morriston")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10271, 9954), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10268, 9954), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10264, 9954), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10249, 9959), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10235, 9970), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10252, 9950), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10246, 9945), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10232, 9947), MineableRock.OreType.RHICHITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10252, 9959), MineableRock.OreType.VALENITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10241, 9946), MineableRock.OreType.VALENITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10238, 9946), MineableRock.OreType.VALENITE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10244, 9958), MineableRock.OreType.SANDELUGE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10231, 9970), MineableRock.OreType.SANDELUGE));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new MineableRock(id, new Coordinate(10258, 9940), MineableRock.OreType.UNKNOWN));
		}
	}
	
	public static int getBackgroundTextureIdForBoard(String boardName) {
		if (boardName.equals("dawning"))				return Textures.BACKGROUND_SOTN;
		else if (boardName.equals("dawning_interior"))	return Textures.NONE;
		else if (boardName.equals("tower_of_preludes"))	return Textures.NONE;
		else if (boardName.equals("sotn_clock_tower"))	return Textures.BACKGROUND_SOTN;
		else											return Textures.NONE;
	}
	
}
