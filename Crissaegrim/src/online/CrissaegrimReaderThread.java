package online;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import datapacket.DataPacket;

public class CrissaegrimReaderThread extends Thread {
	private Socket valmanwaySocket = null;
    
    public CrissaegrimReaderThread(Socket socket) {
    	super("CrissaegrimReaderThread");
    	this.valmanwaySocket = socket;
    }

    public void run() {
    	
    	ObjectInputStream socketIn = null;
    	try {
    	
    		socketIn = new ObjectInputStream(valmanwaySocket.getInputStream());
    		
    		boolean listening = true;
    		
    		while (listening) {
    			DataPacket incomingPacket = (DataPacket)socketIn.readObject();
    			DataPacketProcessor.processDataPacket(incomingPacket);
    		}
    		
    	} catch (IOException | ClassNotFoundException e) {
    		System.out.println("Crissaegrim reader thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    	
    	try {
			socketIn.close();
			valmanwaySocket.close();
		} catch (IOException e) { e.printStackTrace(); }
		
    }
}
