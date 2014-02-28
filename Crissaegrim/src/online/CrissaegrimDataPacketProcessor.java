package online;

import crissaegrim.Crissaegrim;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.ReceivePlayerIdPacket;
import datapacket.SendAllPlayerStatusesPacket;
import datapacket.SendChatMessagePacket;

public final class CrissaegrimDataPacketProcessor {
	
	public static void processDataPacket(DataPacket packet) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.RECEIVE_PLAYER_ID_PACKET:
				Crissaegrim.getPlayer().assignPlayerId( ((ReceivePlayerIdPacket)(packet)).getReceivedPlayerId() );
				break;
			case DataPacketTypes.SEND_ALL_PLAYER_STATUSES_PACKET:
				Crissaegrim.setGhosts( ((SendAllPlayerStatusesPacket)(packet)).getPlayerStatuses() );
				break;
			case DataPacketTypes.SEND_CHAT_MESSAGE_PACKET:
				Crissaegrim.getGameRunner().addWaitingChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock() );
				break;
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
	
}
