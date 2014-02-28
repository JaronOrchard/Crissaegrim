package valmanway;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import datapacket.SendAllPlayerStatusesPacket;
import player.PlayerStatus;
import thunderbrand.Thunderbrand;

public class ValmanwayWriterThread extends Thread {
	private final Socket crissaegrimSocket;
    private final ValmanwayUserData valmanwayUserData;
	
    private long lastPlayerStatusSendTime = 0;
	private static long PLAYER_STATUS_SEND_INTERVAL = 100;
	
    public ValmanwayWriterThread(Socket socket, ValmanwayUserData userData) {
    	super("ValmanwayWriterThread");
    	this.crissaegrimSocket = socket;
    	this.valmanwayUserData = userData;
    }

    public void run() {
    	
    	try {
    		ObjectOutputStream socketOut = new ObjectOutputStream(crissaegrimSocket.getOutputStream());
    		socketOut.flush();
    		
    		while (valmanwayUserData.connectionStable) {
    			if (valmanwayUserData.outgoingDataPacketsExist()) {
    				socketOut.reset();
    				socketOut.writeObject(valmanwayUserData.popOutgoingDataPacket());
    			}
    			
    			// Every 100 ms, send a list of all other players
    			if (Thunderbrand.getTime() - lastPlayerStatusSendTime > PLAYER_STATUS_SEND_INTERVAL) {
    				lastPlayerStatusSendTime = Thunderbrand.getTime();
					Map<Integer, PlayerStatus> ps = Valmanway.getSharedData().getPlayerStatuses();
					ps.remove(valmanwayUserData.getPlayerId());
					valmanwayUserData.addOutgoingDataPacket(new SendAllPlayerStatusesPacket(ps));
    			}
    		}
    		
    		socketOut.close();
    		crissaegrimSocket.close();
    		
    	} catch (IOException e) {
    		System.out.println("Valmanway writer thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    	
    	valmanwayUserData.connectionStable = false;
    	Valmanway.getSharedData().dropPlayer(valmanwayUserData.getPlayerId());
    }
}
