package valmanway;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ValmanwayWriterThread extends Thread {
	private Socket crissaegrimSocket = null;
    
    public ValmanwayWriterThread(Socket socket) {
    	super("ValmanwayWriterThread");
    	this.crissaegrimSocket = socket;
    }

    public void run() {
    	
    	try {
    		ObjectOutputStream socketOut = new ObjectOutputStream(crissaegrimSocket.getOutputStream());
    		socketOut.flush();
    	
    		boolean listening = true;
    		
    		while (listening) {
    			if (Valmanway.getSharedData().outgoingDataPacketsExist()) {
    				socketOut.reset();
    				socketOut.writeObject(Valmanway.getSharedData().popOutgoingDataPacket());
    			}
    		}
    		
    		socketOut.close();
    		crissaegrimSocket.close();
    		
    	} catch (IOException e) {
    		System.out.println("Valmanway writer thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    }
}
