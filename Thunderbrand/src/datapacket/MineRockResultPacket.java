package datapacket;

public class MineRockResultPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final boolean succeeded;
	public boolean getSucceeded() { return succeeded; }
	
	public MineRockResultPacket(boolean succeeded) {
		super(DataPacketTypes.MINE_ROCK_RESULT_PACKET);
		this.succeeded = succeeded;
	}
	
}
