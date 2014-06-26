package board;

import geometry.Coordinate;

import textures.Textures;
import thunderbrand.Thunderbrand;
import doodads.Door;

public class BoardInfo {
	
	public static void addDoodadsToBoard(Board board) {
		String boardName = board.getName();
		if (boardName.equals("dawning")) {
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10052.5, 10013), "tower_of_preludes", new Coordinate(10050.5, 10016)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10214.5, 10004), "dawning_interior", new Coordinate(10043.5, 10019)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10232.5, 10004), "dawning_interior", new Coordinate(10061.5, 10019)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10251.5, 10003), "dawning_interior", new Coordinate(10139.5, 10027)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10258.5, 10009), "dawning_interior", new Coordinate(10139.5, 10033)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10283.5, 9999), "sotn_clock_tower", new Coordinate(10177.5, 10033)));
		} else if (boardName.equals("dawning_interior")) {
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10043.5, 10019), "dawning", new Coordinate(10214.5, 10004)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10061.5, 10019), "dawning", new Coordinate(10232.5, 10004)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10139.5, 10027), "dawning", new Coordinate(10251.5, 10003)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10139.5, 10033), "dawning", new Coordinate(10258.5, 10009)));
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10036.5, 10014), "dawning", new Coordinate(10207.5, 9999)));
		} else if (boardName.equals("tower_of_preludes")) {
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(10050.5, 10016), "dawning", new Coordinate(10052.5, 10013)));
		} else if (boardName.equals("sotn_clock_tower")) {
			board.getDoodads().put(Thunderbrand.getNextDoodadId(), new Door(new Coordinate(9860.5, 10096), "dawning", new Coordinate(10280.5, 9992)));
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
