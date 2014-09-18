package board;

import java.util.Arrays;
import java.util.Map;

import geometry.Coordinate;
import textures.Textures;
import thunderbrand.Thunderbrand;
import doodads.Doodad;
import doodads.Door;
import doodads.Furnace;
import doodads.MineableRock;
import doodads.Signpost;

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
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10283.5, 9999), "barrett_station", new Coordinate(10040.5, 10051)));
		} else if (boardName.equals("dawning_interior")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10043.5, 10019), "dawning", new Coordinate(10214.5, 10004)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10061.5, 10019), "dawning", new Coordinate(10232.5, 10004)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10139.5, 10027), "dawning", new Coordinate(10251.5, 10003)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10139.5, 10033), "dawning", new Coordinate(10258.5, 10009)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10036.5, 10014), "dawning", new Coordinate(10207.5, 9999)));
		} else if (boardName.equals("tower_of_preludes")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10050.5, 10016), "dawning", new Coordinate(10052.5, 10013)));
		} else if (boardName.equals("sotn_clock_tower")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(9860.5, 10096), "barrett_station", new Coordinate(10050.5, 10045)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10177.5, 10033), "barrett_station", new Coordinate(10050.5, 10051)));
		} else if (boardName.equals("morriston")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10022.5, 10035), "barrett_station", new Coordinate(10060.5, 10051)));
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
			// Needs to move into Morriston proper:
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Furnace(id, new Coordinate(10290, 9954)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10293.5, 9955), Arrays.asList(
					"Feel free to smelt ores in this furance.",
					"...You can't actually do anything with the bars yet, though.")));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10115.5, 9991), "(This one's for you, Market Man!)"));
			
		} else if (boardName.equals("barrett_station")) {
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10040.5, 10051), "dawning", new Coordinate(10283.5, 9999)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10050.5, 10051), "sotn_clock_tower", new Coordinate(10177.5, 10033)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Door(id, new Coordinate(10060.5, 10051), "morriston", new Coordinate(10022.5, 10035)));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10048.5, 10056), Arrays.asList(
					"Welcome to Crissaegrim!",
					"This is Barrett Station, the Hub.",
					"Feel free to explore the areas",
					"beyond the three doors below!")));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10042.5, 10051), Arrays.asList(
					"Dawning",
					"Tower of Preludes (far west)",
					"(The first area ever!)")));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10052.5, 10051), Arrays.asList(
					"SotN Clock Tower",
					"(Kill Phantos, get fireworks!)")));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10058.5, 10051), Arrays.asList(
					"Morriston Mine",
					"(Explore a mine and get ores!)")));
			doodadMap.put(id = Thunderbrand.getNextDoodadId(), new Signpost(id, new Coordinate(10048.5, 10045), Arrays.asList(
					"Congrats on reaching the end of the",
					"clock tower!  Here's a tip:",
					"Press B to get some nice items!")));
		}
	}
	
	public static int getBackgroundTextureIdForBoard(String boardName) {
		if (boardName.equals("dawning"))				return Textures.BACKGROUND_SOTN;
		else if (boardName.equals("dawning_interior"))	return Textures.NONE;
		else if (boardName.equals("tower_of_preludes"))	return Textures.NONE;
		else if (boardName.equals("sotn_clock_tower"))	return Textures.BACKGROUND_SOTN;
		else if (boardName.equals("barrett_station"))	return Textures.NONE;
		else											return Textures.NONE;
	}
	
}
