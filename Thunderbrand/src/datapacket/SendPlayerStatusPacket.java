package datapacket;

import entities.EntityStatus;

public class SendPlayerStatusPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final EntityStatus playerStatus;
	public EntityStatus getPlayerStatus() { return playerStatus; }
	
	public SendPlayerStatusPacket(String boardName, double xPos, double yPos, int currentTexture, boolean facingRight) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		playerStatus = new EntityStatus(boardName, xPos, yPos, currentTexture, facingRight);
	}
	
	public SendPlayerStatusPacket(EntityStatus ps) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		playerStatus = ps;
	}
	
}
