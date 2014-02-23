package datapacket;

import java.util.Map;
import player.PlayerStatus;

public class SendAllPlayerStatusesPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final Map<Integer, PlayerStatus> playerStatuses;
	public Map<Integer, PlayerStatus> getPlayerStatuses() { return playerStatuses; }
	
	public SendAllPlayerStatusesPacket(Map<Integer, PlayerStatus> ps) {
		super(DataPacketTypes.SEND_ALL_PLAYER_STATUSES_PACKET);
		playerStatuses = ps;
	}
	
}
