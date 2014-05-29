package npc;

import java.util.Map;

import geometry.Coordinate;
import board.Board;
import textures.Textures;

public class NPCPhanto extends NPC {
	
	public NPCPhanto(int npc_id, Coordinate startingPosition, String boardName, Map<String, Board> boardMap) {
		super(npc_id, startingPosition, boardName, boardMap);
		textureHalfWidth = 1;
		textureHeight = 2;
		
		// TODO: Make a required function that sets all the movement parameters up
		
	}
	
	@Override
	public int getCurrentTexture() {
		if (isBusy()) return busy.getTexture();
		return Textures.NPC_PHANTO;
	}
	
	@Override
	public int getStunnedTexture() { return Textures.NPC_PHANTO; }
	
	@Override
	public void update() {
		
	}
	
}
