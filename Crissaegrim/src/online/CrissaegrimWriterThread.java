package online;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import crissaegrim.Crissaegrim;

public class CrissaegrimWriterThread extends Thread {
	private Socket valmanwaySocket = null;
    
    public CrissaegrimWriterThread(Socket socket) {
    	super("CrissaegrimWriterThread");
    	this.valmanwaySocket = socket;
    }

    public void run() {
    	
    	ObjectOutputStream socketOut = null;
    	try {
    		socketOut = new ObjectOutputStream(valmanwaySocket.getOutputStream());
    		socketOut.flush();
    	
    		while (Crissaegrim.connectionStable) {
    			if (Crissaegrim.outgoingDataPacketsExist()) {
    				socketOut.reset();
    				socketOut.writeObject(Crissaegrim.popOutgoingDataPacket());
    			}
    		}
    		
    	} catch (IOException e) {
    		System.out.println("Crissaegrim writer thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    	
    	try {
			socketOut.close();
			valmanwaySocket.close();
		} catch (IOException e) { e.printStackTrace(); }
		
    	Crissaegrim.connectionStable = false;
    }
}
