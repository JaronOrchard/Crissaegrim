package online;

import items.Item;

import java.awt.Color;
import java.util.List;

import textblock.TextBlock;
import textures.Textures;
import busy.GotHitByAttackStunnedBusy;
import busy.PlayerDiedBusy;
import crissaegrim.Crissaegrim;
import datapacket.ChunkPacket;
import datapacket.DataPacket;
import datapacket.DataPacketTypes;
import datapacket.GotHitByAttackPacket;
import datapacket.IncomingChunkCountPacket;
import datapacket.NonexistentChunkPacket;
import datapacket.ParticleSystemPacket;
import datapacket.ReceiveItemsPacket;
import datapacket.ReceivePlayerIdPacket;
import datapacket.ReceivePlayerNamePacket;
import datapacket.SendEntityStatusesPacket;
import datapacket.SendChatMessagePacket;
import datapacket.SendSystemMessagePacket;
import effects.ParticleSystem;

public final class CrissaegrimDataPacketProcessor {
	
	public static void processDataPacket(DataPacket packet) {
		switch (packet.getPacketType()) {
			
			case DataPacketTypes.RECEIVE_PLAYER_ID_PACKET:
				Crissaegrim.getPlayer().assignPlayerId( ((ReceivePlayerIdPacket)(packet)).getReceivedPlayerId() );
				break;
			case DataPacketTypes.RECEIVE_PLAYER_NAME_PACKET:
				String newName = ((ReceivePlayerNamePacket)(packet)).getReceivedPlayerName();
				Crissaegrim.getPlayer().setName(newName);
				Crissaegrim.getPreferenceHandler().setLastUsername(newName);
				break;
			case DataPacketTypes.SEND_ENTITY_STATUSES_PACKET:
				SendEntityStatusesPacket sesp = (SendEntityStatusesPacket)(packet);
				if (sesp.getStatusesAreNPCs()) {
					Crissaegrim.setNpcGhosts(sesp.getEntityStatuses());
				} else {
					Crissaegrim.setPlayerGhosts(sesp.getEntityStatuses());
				}
				break;
			case DataPacketTypes.SEND_CHAT_MESSAGE_PACKET:
				Crissaegrim.getGameRunner().addWaitingChatMessage( ((SendChatMessagePacket)(packet)).getTextBlock() );
				break;
			case DataPacketTypes.INCOMING_CHUNK_COUNT_PACKET:
				Crissaegrim.numPacketsToReceive = ((IncomingChunkCountPacket)(packet)).getIncomingChunkCount();
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
				GotHitByAttackPacket ghbap = (GotHitByAttackPacket)(packet);
				if (!Crissaegrim.getPlayer().isBusy() || !Crissaegrim.getPlayer().getBusy().isImmuneToAttacks()) {
					if (!Crissaegrim.getPlayer().getHealthBar().takeDamage(ghbap.getDamage())) {
						if (ghbap.getBounceBack()) {
							Crissaegrim.getPlayer().getMovementHelper().bounceBackFromAttack(ghbap.getHitFromRightSide());
						} else {
							Crissaegrim.getPlayer().setBusy(new GotHitByAttackStunnedBusy(Crissaegrim.getPlayer().getStunnedTexture()));
						}
					} else { // The Player died from this attack!
						Crissaegrim.addOutgoingDataPacket(new SendSystemMessagePacket(new TextBlock(
								Crissaegrim.getPlayer().getName() + " has been killed by " + ghbap.getAttackerName() + "!", Color.RED)));
						Crissaegrim.getPlayer().setBusy(new PlayerDiedBusy(Textures.STICK_PLAYER_DEAD));
					}
				}
				break;
			case DataPacketTypes.RECEIVE_ITEMS_PACKET:
				List<Item> items = ((ReceiveItemsPacket)(packet)).getItems();
				for (Item item : items) {
					Crissaegrim.getPlayer().receiveItem(item);
				}
				break;
			case DataPacketTypes.PARTICLE_SYSTEM_PACKET:
				ParticleSystemPacket psp = (ParticleSystemPacket)(packet);
				Crissaegrim.getGameRunner().addParticleSystem(
						new ParticleSystem(psp.getNumParticles(), psp.getSystemCenter(), psp.getBoardName(), psp.getSystemColor()));
				break;
				
				
				
			default:
				System.out.println("UNKNOWN PACKET TYPE: " + packet.getPacketType());
		}
		
	}
	
}
