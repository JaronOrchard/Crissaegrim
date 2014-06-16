package online;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import crissaegrim.Crissaegrim;
import datapacket.DataPacket;

public class CrissaegrimReaderThread extends Thread {
	private final Socket valmanwaySocket;
	
	public CrissaegrimReaderThread(Socket socket) {
		super("CrissaegrimReaderThread");
		this.valmanwaySocket = socket;
	}

	public void run() {
		
		ObjectInputStream socketIn = null;
		try {
			socketIn = new ObjectInputStream(valmanwaySocket.getInputStream());
			
			while (Crissaegrim.connectionStable) {
				DataPacket incomingPacket = (DataPacket)socketIn.readObject();
				CrissaegrimDataPacketProcessor.processDataPacket(incomingPacket);
			}
			
		} catch (IOException | ClassNotFoundException e) {
			/* This is expected behavior when the window is closed. */
		}
		
		try {
			socketIn.close();
			valmanwaySocket.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		Crissaegrim.connectionStable = false;
	}
}
