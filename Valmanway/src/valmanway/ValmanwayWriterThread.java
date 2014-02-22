package valmanway;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ValmanwayWriterThread extends Thread {
	private final Socket crissaegrimSocket;
    private final ValmanwayUserData valmanwayUserData;
	
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
    		}
    		
    		socketOut.close();
    		crissaegrimSocket.close();
    		
    	} catch (IOException e) {
    		System.out.println("Valmanway writer thread ended - " + e.getMessage());
			e.printStackTrace();
		}
    	System.out.println("writer thread end");
    	
    	valmanwayUserData.connectionStable = false;
    }
}
