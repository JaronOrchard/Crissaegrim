package valmanway;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import thunderbrand.TextBlock;
import datapacket.ChunkPacket;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.DoneSendingChunksPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.ReceivePlayerIdPacket;
import datapacket.ReceivePlayerNamePacket;
import datapacket.RequestEntireBoardPacket;
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
			case DataPacketTypes.REQUEST_ENTIRE_BOARD_PACKET:
				sendEntireBoard( ((RequestEntireBoardPacket)(packet)).getBoardName(), valmanwayUserData);
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
	
	private static void sendEntireBoard(String boardName, ValmanwayUserData vud) {
		Map<String, DataPacket> chunkPackets = new HashMap<String, DataPacket>();
		int chunkSizeSide = 100;
		
		File chunksDir = new File("C:/CrissaegrimChunks/" + boardName);
		for (File f : chunksDir.listFiles()) {
			if (!f.isDirectory() && f.getName().startsWith(boardName + "@")) {
				int chunkXOrigin = Integer.parseInt(f.getName().substring(f.getName().indexOf('@') + 1, f.getName().lastIndexOf('_')));
				int chunkYOrigin = Integer.parseInt(f.getName().substring(f.getName().lastIndexOf('_') + 1));
				byte[] bytes = new byte[70000];
				
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(f));
					int numEntities = Integer.parseInt(br.readLine());
					for (int i = 0; i < numEntities; i++) {
						br.readLine(); // Skip entities
					}
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					char[] tileChar = new char[7];
					for (int i = 0; i < chunkSizeSide; i++) {
						for (int j = 0; j < chunkSizeSide; j++) {
							br.read(tileChar, 0, 7);
							for (int k = 0; k < 7; k++) {
								outStream.write(tileChar[k]);
							}
						}
					}
					bytes = outStream.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to load chunk " + chunkXOrigin + "_" + chunkYOrigin + "!");
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				chunkPackets.put(chunkXOrigin + "_" + chunkYOrigin, new ChunkPacket(boardName, chunkXOrigin, chunkYOrigin, bytes));
				
				// Create 8 neighboring NonexistentChunks if there's not already data there:
				for (int xOrig = chunkXOrigin - chunkSizeSide; xOrig <= chunkXOrigin + chunkSizeSide; xOrig += chunkSizeSide) {
					for (int yOrig = chunkYOrigin - chunkSizeSide; yOrig <= chunkYOrigin + chunkSizeSide; yOrig += chunkSizeSide) {
						if (!chunkPackets.containsKey(xOrig + "_" + yOrig)) {
							chunkPackets.put(xOrig + "_" + yOrig, new NonexistentChunkPacket(boardName, xOrig, yOrig));
						}
					}
				}
			}
		}
		for (DataPacket chunkPacket : chunkPackets.values()) {
			vud.addOutgoingDataPacket(chunkPacket);
		}
		
		vud.addOutgoingDataPacket(new DoneSendingChunksPacket());
	}
		
}
