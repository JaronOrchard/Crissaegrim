package entities;

import java.util.Map;

import geometry.Coordinate;
import board.Board;
import textures.Textures;
import thunderbrand.Thunderbrand;
import entities.Entity;

public class NPC extends Entity {
	
	private static double NPC_SWORD_ALTITUDE = 1.74;
	private static double NPC_SWORD_HEIGHT = 0.15;
	private static double NPC_SWORD_LENGTH = 1.0;
	
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
	
	// Currently a hack to get NPCs initially working.
	// Preferably each type of NPC would be its own subclass or something.
	long tempTime = Thunderbrand.getTime();
	Coordinate prevPosition = new Coordinate(0,0);
	boolean goLeft = true;
	boolean jump = true;
	boolean jumpedAlready = false;
	@Override
	public void update() {
		if (isBusy() && busy.hasExpired()) {
			busy = null;
		}
		
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
			emh.moveEntity();
			if (currentTime - tempTime > 500) {
				Coordinate currentPosition = getPosition();
				if (prevPosition.matchesCoordinate(currentPosition)) {
					goLeft = !goLeft;
					jump = false;
				} else {
					prevPosition.setAll(currentPosition);
				}
				tempTime = currentTime;
			}
		} else if (npc_type == 2) {
			if (goLeft) {
				emh.requestLeftMovement();
			} else {
				emh.requestRightMovement();
			}
			if (jump) {
				emh.requestJumpMovement();
			}
			jump = true;
			emh.moveEntity();
			if (currentTime - tempTime > 1500) {
				Coordinate currentPosition = getPosition();
				if (prevPosition.matchesCoordinate(currentPosition)) {
					if (jumpedAlready) {
						goLeft = !goLeft;
						jumpedAlready = false;
					} else {
						jump = false;
						jumpedAlready = true;
					}
				} else {
					prevPosition.setAll(currentPosition);
					jumpedAlready = false;
				}
				tempTime = currentTime;
			}
		}
	}

	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC;
	}
	
}
