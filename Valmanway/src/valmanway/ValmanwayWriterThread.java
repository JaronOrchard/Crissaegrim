package valmanway;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import datapacket.SendEntityStatusesPacket;
import entities.EntityStatus;
import thunderbrand.Thunderbrand;

public class ValmanwayWriterThread extends Thread {
	private final Socket crissaegrimSocket;
	private final ValmanwayUserData valmanwayUserData;
	
	private long lastPlayerStatusSendTime = 0;
	private static long PLAYER_STATUS_SEND_INTERVAL = 50;
	
	public ValmanwayWriterThread(Socket socket, ValmanwayUserData userData) {
		super("ValmanwayWriterThread");
		this.crissaegrimSocket = socket;
		this.valmanwayUserData = userData;
	}

	public void run() {
		
		ObjectOutputStream socketOut = null;
		try {
			socketOut = new ObjectOutputStream(crissaegrimSocket.getOutputStream());
			socketOut.flush();
			
			ValmanwayDataPacketProcessor.sendRegularMessage(valmanwayUserData.getPlayerName() + " has logged in.", Color.YELLOW);
			
			while (valmanwayUserData.connectionStable) {
				if (valmanwayUserData.outgoingDataPacketsExist()) {
					socketOut.reset();
					socketOut.writeObject(valmanwayUserData.popOutgoingDataPacket());
				}
				
				// Every 50 ms, send new data:
				//  - A list of all other player statuses
				//  - New chat messages
				if (Thunderbrand.getTime() - lastPlayerStatusSendTime > PLAYER_STATUS_SEND_INTERVAL) {
					lastPlayerStatusSendTime = Thunderbrand.getTime();
					
					String boardName = valmanwayUserData.getCurrentBoardName();
					if (!boardName.isEmpty()) {
						// Send players:
						Map<Integer, EntityStatus> playerStatuses = new HashMap<Integer, EntityStatus>(Valmanway.getSharedData().getPlayerStatusMap().get(boardName));
						playerStatuses.remove(valmanwayUserData.getPlayerId());
						valmanwayUserData.addOutgoingDataPacket(new SendEntityStatusesPacket(playerStatuses, false, boardName));
						
						// Send NPCs:
						valmanwayUserData.addOutgoingDataPacket(new SendEntityStatusesPacket(
								Valmanway.getSharedData().getNpcStatusMap().get(boardName), true, boardName));
					}
					
					valmanwayUserData.sendNewDataPackets();
				}
			}
			
			ValmanwayDataPacketProcessor.sendRegularMessage(valmanwayUserData.getPlayerName() + " has logged out.", Color.YELLOW);
		} catch (IOException e) {
			System.out.println(valmanwayUserData.getPlayerName() + " has left");
		}
		
		try {
			socketOut.close();
			crissaegrimSocket.close();
		} catch (IOException e) { /*e.printStackTrace();*/ }
		
		valmanwayUserData.connectionStable = false;
		Valmanway.getSharedData().dropPlayer(valmanwayUserData.getPlayerId(), valmanwayUserData.getCurrentBoardName());
	}
}
