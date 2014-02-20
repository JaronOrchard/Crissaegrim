package datapacket;

import java.io.Serializable;

public abstract class DataPacket implements Serializable {
	private static final long serialVersionUID = 1L;

	public transient static final int REQUEST_PLAYER_ID_PACKET = 1;
	public transient static final int RECEIVE_PLAYER_ID_PACKET = 2;
	
	private int packetType;
	public int getPacketType() { return packetType; }
	
	public DataPacket(int type) {
		packetType = type;
	}
	
}
