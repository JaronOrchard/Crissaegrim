package valmanway;

import java.awt.Color;

import thunderbrand.TextBlock;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.ReceivePlayerIdPacket;
import datapacket.SendChatMessagePacket;
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
			case DataPacketTypes.SEND_CHAT_MESSAGE_PACKET:
				processChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock().getMessage(), valmanwayUserData);
				break;
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
	
	private static void processChatMessage(String message, ValmanwayUserData valmanwayUserData) {
		if (message.charAt(0) == '/') { // System command
			String lowercaseMessage = message.toLowerCase();
			
			if (lowercaseMessage.startsWith("/help")) {
				valmanwayUserData.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(
						"Commands: /me, /setname", Color.GRAY)));
//			} else if (lowercaseMessage.startsWith("/me")) {
//				if (lowercaseMessage.equals("/me")) {
//					Crissaegrim.addSystemMessage("Usage: /me [action]");
//				} else {
//					parentChatBox.addChatMessage(Crissaegrim.getPlayer().getName() + " " + message.substring(4), currentColor);
//				}
//			} else if (lowercaseMessage.startsWith("/setname")) {
//				if (lowercaseMessage.equals("/setname")) {
//					Crissaegrim.addSystemMessage("Usage: /setname [name]");
//				} else {
//					Crissaegrim.getPlayer().setName(message.substring(9).trim());
//					Crissaegrim.addSystemMessage("Your name has been changed to \"" + Crissaegrim.getPlayer().getName() + "\".");
//				}
			} else {
				valmanwayUserData.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(
						"Unrecognized command: " + message, Color.GRAY)));
				valmanwayUserData.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(
						"Type \"/help\" to see the list of commands.", Color.GRAY)));
			}
		
		} else { // Normal message
			//parentChatBox.addChatMessage(currentMessage, currentColor);
		}
		
	}
		
}
