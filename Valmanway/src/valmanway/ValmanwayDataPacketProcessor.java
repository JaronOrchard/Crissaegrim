package valmanway;

import java.awt.Color;

import thunderbrand.TextBlock;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.ReceivePlayerIdPacket;
import datapacket.ReceivePlayerNamePacket;
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
				processChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock(), valmanwayUserData);
				break;
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
	
	private static void processChatMessage(TextBlock textBlock, ValmanwayUserData vud) {
		String message = textBlock.getMessage();
		if (message.charAt(0) == '/') { // System command
			String lowercaseMessage = message.toLowerCase();
			
			if (lowercaseMessage.startsWith("/help")) {
				sendSystemMessage("Commands: /me, /setname", vud);
			} else if (lowercaseMessage.startsWith("/me")) {
				if (lowercaseMessage.equals("/me")) {
					sendSystemMessage("Usage: /me [action]", vud);
				} else {
					sendRegularMessage(vud.getPlayerName() + " " + message.substring(4), textBlock.getColor());
				}
			} else if (lowercaseMessage.startsWith("/setname")) {
				if (lowercaseMessage.equals("/setname")) {
					sendSystemMessage("Usage: /setname [name]", vud);
				} else {
					String newName = message.substring(9).trim();
					vud.setPlayerName(newName);
					vud.addOutgoingDataPacket(new ReceivePlayerNamePacket(newName));
					sendSystemMessage("Your name has been changed to \"" + newName + "\".", vud);
				}
			} else {
				sendSystemMessage("Unrecognized command: " + message, vud);
				sendSystemMessage("Type \"/help\" to see the list of commands.", vud);
			}
		} else { // Normal message
			sendRegularMessage("<" + vud.getPlayerName() + "> " + message, textBlock.getColor());
		}
		
	}
	
	private static void sendSystemMessage(String message, ValmanwayUserData vud) {
		vud.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(message, Color.GRAY)));
	}
	
	private static void sendRegularMessage(String message, Color color) {
		Valmanway.getSharedData().addChatMessage(new TextBlock(message, color));
	}
		
}
