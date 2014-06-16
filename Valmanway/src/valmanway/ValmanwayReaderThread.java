package valmanway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import datapacket.DataPacket;

public class ValmanwayReaderThread extends Thread {
	private final Socket crissaegrimSocket;
	private final ValmanwayUserData valmanwayUserData;
	
	public ValmanwayReaderThread(Socket socket, ValmanwayUserData userData) {
		super("ValmanwayReaderThread");
		this.crissaegrimSocket = socket;
		this.valmanwayUserData = userData;
	}

	public void run() {
		
		ObjectInputStream socketIn = null;
		try {
		
			socketIn = new ObjectInputStream(crissaegrimSocket.getInputStream());
			
			while (valmanwayUserData.connectionStable) {
				DataPacket incomingPacket = (DataPacket)socketIn.readObject();
				ValmanwayDataPacketProcessor.processDataPacket(incomingPacket, valmanwayUserData);
			}
			
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(valmanwayUserData.getPlayerName() + " has left");
		}
		
		try {
			socketIn.close();
			crissaegrimSocket.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		valmanwayUserData.connectionStable = false;
	}
}
