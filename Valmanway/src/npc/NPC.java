package npc;

import java.util.Map;

import board.Board;
import entities.Entity;
import geometry.Coordinate;

public abstract class NPC extends Entity {

	public NPC(int npc_id, int maxHealth, Coordinate startingPosition, String boardName, Map<String, Board> boardMap) {
		super(maxHealth, boardMap);
		id = npc_id;
		currentBoardName = boardName;
		position = startingPosition;
	}
	
	public abstract int getAttackPower();
	
}
