package datapacket;

import java.util.Map;

import doodads.Doodad;

public class BoardDoodadsPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final Map<Integer, Doodad> doodads;
	
	public String getBoardName() { return boardName; }
	public Map<Integer, Doodad> getDoodads() { return doodads; }
	
	public BoardDoodadsPacket(String bName, Map<Integer, Doodad> doodadMap) {
		super(DataPacketTypes.BOARD_DOODADS_PACKET);
		boardName = bName;
		doodads = doodadMap;
	}
	
}
