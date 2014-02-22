package valmanway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import datapacket.DataPacket;

public class ValmanwayReaderThread extends Thread {
	private Socket crissaegrimSocket = null;
    
    public ValmanwayReaderThread(Socket socket) {
    	super("ValmanwayReaderThread");
    	this.crissaegrimSocket = socket;
    }

    public void run() {
    	
    	ObjectInputStream socketIn = null;
    	try {
    	
    		socketIn = new ObjectInputStream(crissaegrimSocket.getInputStream());
    		
    		boolean listening = true;
    		
    		while (listening) {
    			DataPacket incomingPacket = (DataPacket)socketIn.readObject();
    			ValmanwayDataPacketProcessor.processDataPacket(incomingPacket);
    		}
    		
    	} catch (IOException | ClassNotFoundException e) {
    		System.out.println("Valmanway reader thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    	
    	try {
			socketIn.close();
			crissaegrimSocket.close();
		} catch (IOException e) { e.printStackTrace(); }
		
    }
}
