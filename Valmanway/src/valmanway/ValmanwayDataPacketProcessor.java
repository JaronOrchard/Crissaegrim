package valmanway;

import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.ReceivePlayerIdPacket;
import datapacket.SendPlayerStatusPacket;

public final class ValmanwayDataPacketProcessor {
	
	public static void processDataPacket(DataPacket packet, ValmanwayUserData valmanwayUserData) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.REQUEST_PLAYER_ID_PACKET:
				System.out.println("Received player id request packet");
				valmanwayUserData.addOutgoingDataPacket(new ReceivePlayerIdPacket(valmanwayUserData.getPlayerId()));
				break;
			case DataPacketTypes.SEND_PLAYER_STATUS_PACKET:
				Valmanway.getSharedData().updatePlayerStatusMap(
						valmanwayUserData.getPlayerId(),
						((SendPlayerStatusPacket)(packet)).getPlayerStatus());
				break;
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
		
}
