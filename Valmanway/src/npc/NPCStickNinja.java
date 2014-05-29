package npc;

import java.util.Map;

import geometry.Coordinate;
import board.Board;
import textures.Textures;
import thunderbrand.Thunderbrand;
import entities.EntityMovementHelper;

public class NPCStickNinja extends NPC {
	
	private final int npc_type;
	
	public NPCStickNinja(int npc_id, Coordinate startingPosition, String boardName, Map<String, Board> boardMap, int type) {
		super(npc_id, startingPosition, boardName, boardMap);
		npc_type = type;
		
		// TODO: Make a required function that sets all the movement parameters up
		
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_STICK_NINJA;
	}
	
	@Override
	public int getStunnedTexture() { return Textures.NPC_STICK_NINJA_STUNNED; }
	
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
	
}
