package datapacket;

import entities.EntityStatus;

public class SendPlayerStatusPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final EntityStatus playerStatus;
	public EntityStatus getPlayerStatus() { return playerStatus; }
	
	public SendPlayerStatusPacket(EntityStatus ps) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		playerStatus = ps;
	}
	
}
