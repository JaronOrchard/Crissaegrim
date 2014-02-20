package datapacket;

import java.io.Serializable;

public abstract class DataPacket implements Serializable {
	private static final long serialVersionUID = 1L;

	private int packetType;
	public int getPacketType() { return packetType; }
	
	public DataPacket(int type) {
		packetType = type;
	}
	
}
