package datapacket;

import java.util.Map;

import entities.EntityStatus;

public class SendAllPlayerStatusesPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final Map<Integer, EntityStatus> playerStatuses;
	public Map<Integer, EntityStatus> getPlayerStatuses() { return playerStatuses; }
	
	public SendAllPlayerStatusesPacket(Map<Integer, EntityStatus> ps) {
		super(DataPacketTypes.SEND_ALL_PLAYER_STATUSES_PACKET);
		playerStatuses = ps;
	}
	
}
