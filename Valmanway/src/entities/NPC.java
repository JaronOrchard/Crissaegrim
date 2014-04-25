package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;
import board.Board;
import textures.Textures;
import thunderbrand.Thunderbrand;
import entities.Entity;

public class NPC extends Entity {
	
	private static double NPC_FEET_HEIGHT = 0.425;
	private static double NPC_BODY_HEIGHT = 2.4;
	private static double NPC_BODY_WIDTH = 0.6;
	private static double NPC_SWORD_ALTITUDE = 1.74;
	private static double NPC_SWORD_HEIGHT = 0.15;
	private static double NPC_SWORD_LENGTH = 1.0;
	
	public static double getNPCFeetHeight() { return NPC_FEET_HEIGHT; }
	public static double getNPCBodyHeight() { return NPC_BODY_HEIGHT; }
	public static double getNPCBodyWidth() { return NPC_BODY_WIDTH; }
	public static double getNPCSwordAltitude() { return NPC_SWORD_ALTITUDE; }
	public static double getNPCSwordHeight() { return NPC_SWORD_HEIGHT; }
	public static double getNPCSwordLength() { return NPC_SWORD_LENGTH; }
	
	private final int npc_type;
	
	public NPC(int npc_id, Coordinate startingPosition, String boardName, int type, Map<String, Board> boardMap) {
		super(boardMap);
		id = npc_id;
		currentBoardName = boardName;
		position = startingPosition;
		npc_type = type;
		
		// TODO: Make a required function that sets all the movement parameters up
		
	}

	@Override
	public Rect getEntityBoundingRect(Coordinate position) {
		return new Rect(
				new Coordinate(position.getX() - (getNPCBodyWidth() / 2), position.getY() + getNPCFeetHeight()),
				new Coordinate(position.getX() + (getNPCBodyWidth() / 2), position.getY() + getNPCFeetHeight() + getNPCBodyHeight()));
	}
	
	@Override
	public List<Line> getEntityFeetLines(Coordinate position, boolean includeHorizontalFeetLine) {
		List<Line> feetLines = new ArrayList<Line>();
		feetLines.add(new Line(
				new Coordinate(position.getX(), position.getY()),
				new Coordinate(position.getX(), position.getY() + getNPCFeetHeight())));
		if (includeHorizontalFeetLine) {
			feetLines.add(new Line(
					new Coordinate(position.getX() - (getNPCBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (getNPCBodyWidth() / 2), position.getY())));
			feetLines.add(new Line(
					new Coordinate(position.getX() - (getNPCBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() - (getNPCBodyWidth() / 2), position.getY() + getNPCFeetHeight())));
			feetLines.add(new Line(
					new Coordinate(position.getX() + (getNPCBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (getNPCBodyWidth() / 2), position.getY() + getNPCFeetHeight())));
		}
		return feetLines;
	}
	
	// Currently a hack to get NPCs initially working.
	// Preferably each type of NPC would be its own subclass or something.
	long tempTime = Thunderbrand.getTime();
	Coordinate posHalfSecondAgo = new Coordinate(0,0);
	boolean goLeft = false;
	boolean jump = true;
	@Override
	public void update() {
		long currentTime = Thunderbrand.getTime();
		EntityMovementHelper emh = getMovementHelper();
		if (npc_type == 1) {
			if (goLeft) {
				emh.requestLeftMovement();
			} else {
				emh.requestRightMovement();
			}
			if (jump) {
				emh.requestJumpMovement();
			}
			jump = true;
		}
		emh.moveEntity();
		if (currentTime - tempTime > 500) {
			Coordinate currentPosition = getPosition();
			if (posHalfSecondAgo.getX() == currentPosition.getX() && posHalfSecondAgo.getY() == currentPosition.getY()) {
				goLeft = !goLeft;
				jump = false;
			} else {
				posHalfSecondAgo.setAll(currentPosition.getX(), currentPosition.getY());
			}
			tempTime = currentTime;
		}
	}

	@Override
	public int getCurrentTexture() {
		return Textures.NPC;
	}
	
}
