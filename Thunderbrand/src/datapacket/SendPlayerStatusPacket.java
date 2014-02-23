package datapacket;

import player.PlayerStatus;

public class SendPlayerStatusPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final PlayerStatus playerStatus;
	public PlayerStatus getPlayerStatus() { return playerStatus; }
	
	public SendPlayerStatusPacket(String boardName, double xPos, double yPos, int currentTexture, boolean facingRight) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		playerStatus = new PlayerStatus(boardName, xPos, yPos, currentTexture, facingRight);
	}
	
	public SendPlayerStatusPacket(PlayerStatus ps) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		playerStatus = ps;
	}
	
}
