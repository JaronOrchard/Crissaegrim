package valmanway;

import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.ReceivePlayerIdPacket;

// Valmanway side

public final class DataPacketProcessor {
	
	public static void processDataPacket(DataPacket packet) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.REQUEST_PLAYER_ID_PACKET:
				System.out.println("Received player id request packet");
				Valmanway.getSharedData().addOutgoingDataPacket(new ReceivePlayerIdPacket(Valmanway.getSharedData().getNextPlayerId()));
				break;
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
		
}
