package datapacket;

import java.util.Map;

import entities.EntityStatus;

public class SendEntityStatusesPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final Map<Integer, EntityStatus> entityStatuses;
	private final boolean statusesAreNPCs;
	private final String boardName;
	
	public Map<Integer, EntityStatus> getEntityStatuses() { return entityStatuses; }
	public boolean getStatusesAreNPCs() { return statusesAreNPCs; }
	public String getBoardName() { return boardName; }
	
	public SendEntityStatusesPacket(Map<Integer, EntityStatus> es, boolean forNPCs, String bName) {
		super(DataPacketTypes.SEND_ENTITY_STATUSES_PACKET);
		entityStatuses = es;
		statusesAreNPCs = forNPCs;
		boardName = bName;
	}
	
}
