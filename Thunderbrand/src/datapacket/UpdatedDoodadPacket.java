package datapacket;

import doodads.Doodad;

public class UpdatedDoodadPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final Doodad doodad;
	
	public String getBoardName() { return boardName; }
	public Doodad getDoodad() { return doodad; }
	
	public UpdatedDoodadPacket(String boardName, Doodad doodad) {
		super(DataPacketTypes.UPDATED_DOODAD_PACKET);
		this.boardName = boardName;
		this.doodad = doodad;
	}
	
}
