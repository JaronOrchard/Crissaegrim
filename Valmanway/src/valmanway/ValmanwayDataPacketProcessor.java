package valmanway;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import npc.NPC;

import org.apache.commons.lang3.StringUtils;

import actions.MineRockAction;
import attack.Attack;
import textblock.TextBlock;
import thunderbrand.Constants;
import thunderbrand.Thunderbrand;
import datapacket.AttackPacket;
import datapacket.BoardDoodadsPacket;
import datapacket.ChunkPacket;
import datapacket.ClientIsOutdatedPacket;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.DoneSendingChunksPacket;
import datapacket.GotHitByAttackPacket;
import datapacket.IncomingChunkCountPacket;
import datapacket.MineRockRequestPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.ParticleSystemPacket;
import datapacket.ReceiveItemsPacket;
import datapacket.ReceivePlayerIdPacket;
import datapacket.ReceivePlayerNamePacket;
import datapacket.PlayerIsChangingBoardsPacket;
import datapacket.RequestPlayerIdPacket;
import datapacket.RequestSpecificChunkPacket;
import datapacket.SendChatMessagePacket;
import datapacket.SendPlayerStatusPacket;
import datapacket.SendSystemMessagePacket;
import entities.EntityStatus;
import geometry.Coordinate;
import geometry.Rect;
import geometry.RectUtils;

public final class ValmanwayDataPacketProcessor {
	
	private static final int LIST_PLAYERS_LINE_MAX_CHARS = 100;
	
	public static void processDataPacket(DataPacket packet, ValmanwayUserData valmanwayUserData) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.REQUEST_PLAYER_ID_PACKET:
				if (((RequestPlayerIdPacket)(packet)).getClientVersion() < Valmanway.getServerVersion()) {
					valmanwayUserData.addOutgoingDataPacket(new ClientIsOutdatedPacket());
				} else {
					System.out.println(valmanwayUserData.getPlayerName() + " has joined");
					valmanwayUserData.addOutgoingDataPacket(new ReceivePlayerIdPacket(valmanwayUserData.getPlayerId()));
				}
				break;
			case DataPacketTypes.SEND_PLAYER_STATUS_PACKET:
				SendPlayerStatusPacket spsp = (SendPlayerStatusPacket)(packet);
				Valmanway.getSharedData().updatePlayerStatusMap(
						valmanwayUserData.getPlayerId(), valmanwayUserData.getCurrentBoardName(),
						spsp.getPlayerStatus());
				break;
			case DataPacketTypes.SEND_CHAT_MESSAGE_PACKET:
				processChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock(), valmanwayUserData);
				break;
			case DataPacketTypes.SEND_SYSTEM_MESSAGE_PACKET:
				TextBlock tb = ((SendSystemMessagePacket)(packet)).getTextBlock();
				sendRegularMessage(tb.getMessage(), tb.getColor());
				break;
			case DataPacketTypes.PLAYER_IS_CHANGING_BOARDS_PACKET:
				PlayerIsChangingBoardsPacket picbp = (PlayerIsChangingBoardsPacket)(packet);
				if (!valmanwayUserData.getCurrentBoardName().isEmpty()) {
					Valmanway.getSharedData().removePlayerStatusFromBoard(valmanwayUserData.getPlayerId(), valmanwayUserData.getCurrentBoardName());
				}
				valmanwayUserData.setCurrentBoardName(picbp.getBoardName());
				if (picbp.getSendBoardData()) { sendEntireBoard(picbp.getBoardName(), valmanwayUserData); }
				valmanwayUserData.addOutgoingDataPacket(
						new BoardDoodadsPacket(picbp.getBoardName(), Valmanway.getSharedData().getBoardMap().get(picbp.getBoardName()).getDoodads()));
				valmanwayUserData.addOutgoingDataPacket(new DoneSendingChunksPacket());
				break;
			case DataPacketTypes.REQUEST_SPECIFIC_CHUNK_PACKET:
				sendSpecificChunk( (RequestSpecificChunkPacket)packet, valmanwayUserData);
				break;
			case DataPacketTypes.ATTACK_PACKET:
				processAttackPacket( (AttackPacket)packet, valmanwayUserData);
				break;
			case DataPacketTypes.PARTICLE_SYSTEM_PACKET:
				Valmanway.getSharedData().addDataPacket(packet);
				break;
			case DataPacketTypes.MINE_ROCK_REQUEST_PACKET:
				MineRockRequestPacket mrrp = (MineRockRequestPacket)(packet);
				Valmanway.getSharedData().addActionToQueue(new MineRockAction(
						Constants.MILLIS_TO_MINE_A_ROCK, mrrp.getRockId(), mrrp.getPlayerId(), mrrp.getBoardName(), mrrp.getChanceOfSuccess()));
				break;
				
				
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
				break;
		}
		
	}
	
	private static void processAttackPacket(AttackPacket attackPacket, ValmanwayUserData vud) {
		Attack attack = attackPacket.getAttack();
		// Compare this attack to all NPCs on that board:
		for (NPC npc : Valmanway.getSharedData().getNPCs().get(attack.getBoardName())) {
			if (npc.isAttackable() && npc.isAlive() && !npc.isBusy() &&
					RectUtils.rectsOverlap(npc.getEntityEntireRect(), attack.getBounds())) {
				if (!npc.getHealthBar().takeDamage(attack.getAttackPower())) {
					npc.hitByAttack(RectUtils.firstRectIsOnLeft(npc.getEntityEntireRect(), attack.getBounds()));
				} else { // The NPC died from this attack!
					Valmanway.sendPacketToPlayer(attack.getAttackerId(), new ReceiveItemsPacket(npc.dropItems())); // Give the killer the dropped items
					Valmanway.getSharedData().addDataPacket(new ParticleSystemPacket(
							125,
							new Coordinate(npc.getPosition().getX(), npc.getPosition().getY() + (npc.getEntireHeight() / 2)),
							npc.getCurrentBoardName(),
							npc.getMainColor().brighter()));
					npc.killNPC(); // Set NPC to dead and scheduled to respawn (must be last as it changes position)
					Valmanway.logMessage(Valmanway.getSharedData().getPlayerName(attack.getAttackerId()) + " killed a " + npc.getName() + "!");
				}
			}
		}
		
		// Compare this attack to all Players:
		for (Entry<Integer, EntityStatus> entityStatus : Valmanway.getSharedData().getPlayerStatusMap().get(attack.getBoardName()).entrySet()) {
			if (entityStatus.getKey() != vud.getPlayerId()) {
				Coordinate position = new Coordinate(entityStatus.getValue().getXPos(), entityStatus.getValue().getYPos());
				Rect playerBoundingRect = RectUtils.getPlayerBoundingRect(position);
				if (RectUtils.rectsOverlap(playerBoundingRect, attack.getBounds())) {
					Valmanway.sendPacketToPlayer(entityStatus.getKey(), new GotHitByAttackPacket(
							vud.getPlayerName(), attack.getAttackPower(), true, RectUtils.firstRectIsOnLeft(playerBoundingRect, attack.getBounds())));
				}
			}
		}
	}
	
	private static void processChatMessage(TextBlock textBlock, ValmanwayUserData vud) {
		String message = textBlock.getMessage();
		if (message.charAt(0) == '/') { // System command
			String lowercaseMessage = message.toLowerCase();
			
			if (lowercaseMessage.startsWith("/help")) {					// "/help"
				sendSystemMessage("Commands: /me, /setname, /players", vud);
			} else if (lowercaseMessage.startsWith("/me")) {			// "/me"
				if (lowercaseMessage.equals("/me")) {
					sendSystemMessage("Usage: /me [action]", vud);
				} else {
					sendRegularMessage(vud.getPlayerName() + " " + message.substring(4), textBlock.getColor());
				}
			} else if (lowercaseMessage.startsWith("/setname")) {		// "/setname"
				if (lowercaseMessage.equals("/setname")) {
					sendSystemMessage("Usage: /setname [name]", vud);
				} else {
					String origName = vud.getPlayerName();
					String newName = message.substring(9).trim();
					if (newName.length() > 16) {
						newName = newName.substring(0, 16).trim();
					}
					if (newName.startsWith("Player")) {
						sendSystemMessage("That name is not allowed.", vud);
					} else if (!StringUtils.isAlphanumericSpace(newName)) {
						sendSystemMessage("Names can only contain letters, numbers, and spaces.", vud);
					} else if (Valmanway.getSharedData().isPlayerNameInUse(newName)) {
						sendSystemMessage("Someone else is already using the name \"" + newName + "\".", vud);
					} else {
						vud.setPlayerName(newName);
						vud.addOutgoingDataPacket(new ReceivePlayerNamePacket(newName));
						System.out.println("\"" + origName + "\" is now known as \"" + newName + "\".");
						sendRegularMessage("\"" + origName + "\" is now known as \"" + newName + "\".", Color.YELLOW);
					}
				}
			} else if (lowercaseMessage.startsWith("/players")) {		// "/players"
				List<String> names = Valmanway.getSharedData().getPlayerNamesSorted();
				if (names.size() == 1) {
					sendSystemMessage("There is one player online: " + names.get(0), vud);
				} else {
					StringBuilder sb = new StringBuilder("There are " + names.size() + " players online: ");
					for (int i = 0; i < names.size(); i++) {
						if (sb.length() + names.get(i).length() > LIST_PLAYERS_LINE_MAX_CHARS) {
							sb.append(",");
							sendSystemMessage(sb.toString(), vud);
							sb = new StringBuilder("     " + names.get(i));
						} else {
							if (i != 0) {
								sb.append(", ");
							}
							sb.append(names.get(i));
						}
					}
					sendSystemMessage(sb.toString(), vud);
				}
			} else {													// Invalid /command
				sendSystemMessage("Unrecognized command: " + message, vud);
				sendSystemMessage("Type \"/help\" to see the list of commands.", vud);
			}
		} else {														// Normal message
			sendRegularMessage("<" + vud.getPlayerName() + "> " + message, textBlock.getColor());
		}
		
	}
	
	public static void sendSystemMessage(String message, ValmanwayUserData vud) {
		vud.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(message, Color.YELLOW)));
	}
	
	public static void sendRegularMessage(String message, Color color) {
		Valmanway.getSharedData().addChatMessage(new TextBlock(message, color));
	}
	
	private static void sendEntireBoard(String boardName, ValmanwayUserData vud) {
		Map<String, DataPacket> chunkPackets = new HashMap<String, DataPacket>();
		int chunkSizeSide = Thunderbrand.getChunkSideSize();
		
		File chunksDir = new File((Thunderbrand.isLinuxBuild() ? "./" : "C:/") + "CrissaegrimChunks/" + boardName);
		for (File f : chunksDir.listFiles()) {
			if (!f.isDirectory() && f.getName().startsWith(boardName + "@")) {
				int chunkXOrigin = Integer.parseInt(f.getName().substring(f.getName().indexOf('@') + 1, f.getName().lastIndexOf('_')));
				int chunkYOrigin = Integer.parseInt(f.getName().substring(f.getName().lastIndexOf('_') + 1));
				byte[] chunkData = new byte[70000];
				
				FileInputStream fin = null;
				try {
					fin = new FileInputStream(f);
					byte[] dummyEntitiesHeader = new byte[3];
					fin.read(dummyEntitiesHeader);
					fin.read(chunkData);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to load chunk " + chunkXOrigin + "_" + chunkYOrigin + "!");
				} finally {
					if (fin != null) {
						try {
							fin.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				chunkPackets.put(chunkXOrigin + "_" + chunkYOrigin, new ChunkPacket(boardName, chunkXOrigin, chunkYOrigin, chunkData));
				
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
		
		vud.addOutgoingDataPacket(new IncomingChunkCountPacket(chunkPackets.size()));
		for (DataPacket chunkPacket : chunkPackets.values()) {
			vud.addOutgoingDataPacket(chunkPacket);
		}
	}
	
	private static void sendSpecificChunk(RequestSpecificChunkPacket rscp, ValmanwayUserData vud) {
		File chunksDir = new File((Thunderbrand.isLinuxBuild() ? "./" : "C:/") + "CrissaegrimChunks/" + rscp.getBoardName());
		File f = new File(chunksDir, rscp.getBoardName() + "@" + rscp.getChunkXOrigin() + "_" + rscp.getChunkYOrigin());
		if (!f.exists() || f.isDirectory()) {
			vud.addOutgoingDataPacket(new NonexistentChunkPacket(rscp.getBoardName(), rscp.getChunkXOrigin(), rscp.getChunkYOrigin()));
		} else {
			int chunkSizeSide = Thunderbrand.getChunkSideSize();
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
				System.out.println("Failed to load chunk " + rscp.getChunkXOrigin() + "_" + rscp.getChunkYOrigin() + "!");
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			vud.addOutgoingDataPacket(new ChunkPacket(rscp.getBoardName(), rscp.getChunkXOrigin(), rscp.getChunkYOrigin(), bytes));
		}
		vud.addOutgoingDataPacket(new DoneSendingChunksPacket());
	}
		
}
