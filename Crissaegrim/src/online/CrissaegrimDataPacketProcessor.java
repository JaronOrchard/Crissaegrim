package online;

import busy.GotHitByAttackBusy;
import crissaegrim.Crissaegrim;
import datapacket.ChunkPacket;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.IncomingChunkCountPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.ReceivePlayerIdPacket;
import datapacket.ReceivePlayerNamePacket;
import datapacket.SendAllPlayerStatusesPacket;
import datapacket.SendChatMessagePacket;

public final class CrissaegrimDataPacketProcessor {
	
	public static void processDataPacket(DataPacket packet) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.RECEIVE_PLAYER_ID_PACKET:
				Crissaegrim.getPlayer().assignPlayerId( ((ReceivePlayerIdPacket)(packet)).getReceivedPlayerId() );
				break;
			case DataPacketTypes.RECEIVE_PLAYER_NAME_PACKET:
				Crissaegrim.getPlayer().setName( ((ReceivePlayerNamePacket)(packet)).getReceivedPlayerName() );
				break;
			case DataPacketTypes.SEND_ALL_PLAYER_STATUSES_PACKET:
				Crissaegrim.setGhosts( ((SendAllPlayerStatusesPacket)(packet)).getPlayerStatuses() );
				break;
			case DataPacketTypes.SEND_CHAT_MESSAGE_PACKET:
				Crissaegrim.getGameRunner().addWaitingChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock() );
				break;
			case DataPacketTypes.CHUNK_PACKET:
				Crissaegrim.getGameRunner().addChunk( ((ChunkPacket)(packet)) );
				Crissaegrim.numPacketsReceived++;
				break;
			case DataPacketTypes.NONEXISTENT_CHUNK_PACKET:
				Crissaegrim.getGameRunner().addNonexistentChunk( ((NonexistentChunkPacket)(packet)) );
				Crissaegrim.numPacketsReceived++;
				break;
			case DataPacketTypes.DONE_SENDING_CHUNKS_PACKET:
				Crissaegrim.currentlyLoading = false;
				Crissaegrim.getGameRunner().goToDestinationBoard();
				break;
			case DataPacketTypes.CLIENT_IS_OUTDATED_PACKET:
				Crissaegrim.getPlayer().assignPlayerId(-2); // playerId of -2 signifies outdated version
				break;
			case DataPacketTypes.GOT_HIT_BY_ATTACK_PACKET:
				if (!Crissaegrim.getPlayer().isBusy()) {
					Crissaegrim.getPlayer().setBusy(new GotHitByAttackBusy(Crissaegrim.getPlayer().getStunnedTexture()));
				}
				break;
			case DataPacketTypes.INCOMING_CHUNK_COUNT_PACKET:
				Crissaegrim.numPacketsToReceive = ((IncomingChunkCountPacket)(packet)).getIncomingChunkCount();
				break;
			
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
	
}
