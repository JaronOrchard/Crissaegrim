package online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import crissaegrim.Crissaegrim;

public class ValmanwayConnection {
	
	private List<String> VALMANWAY_HOSTNAMES = new ArrayList<String>();
	private int VALMANWAY_PORT;
	private static int CONNECTION_TIMEOUT_MILLIS = 1000;
	
	private Socket valmanwaySocket = null;
	
	private boolean online = false;
	public boolean getOnline() { return online; }
	
	public ValmanwayConnection() {
		VALMANWAY_HOSTNAMES.add("garnet");		// Laptop computer
		VALMANWAY_HOSTNAMES.add("96.35.6.105");	// Home desktop computer
		VALMANWAY_PORT = 22112;
	}
	
	public void connectToValmonwayServer() throws IOException {
		online = false;
		for (String hostname : VALMANWAY_HOSTNAMES) {
			try {
				valmanwaySocket = new Socket();
				valmanwaySocket.connect(new InetSocketAddress(hostname, VALMANWAY_PORT), CONNECTION_TIMEOUT_MILLIS);
				new CrissaegrimWriterThread(valmanwaySocket).start();
				new CrissaegrimReaderThread(valmanwaySocket).start();
				online = true;
			} catch (UnknownHostException e) {
				Crissaegrim.addSystemMessage("Don't know about host: " + hostname);
			} catch (IOException e) {
				Crissaegrim.addSystemMessage("Couldn't get I/O for the connection to: " + hostname);
			}
			if (online) { break; }
		}
	}
	
//	public boolean incomingDataExists() throws IOException {
//		if (online) {
//			return valmanwaySocket.getInputStream().available() != 0;
//		}
//		return false;
//	}
//	
//	public String getIncomingData() throws IOException {
//		return inputFromServer.readLine();
//	}
	
//	public void sendPlayerStatus() {
////		Player player = Crissaegrim.getPlayer();
////		StringBuilder out = new StringBuilder();
////		out.append("02|");
////		02|player_id|board_name|x_pos|y_pos|texture|facing_right
////		
//		//outputToServer.println(data);
//	}
	
	public void closeConnections() throws IOException {
		if (valmanwaySocket != null) { valmanwaySocket.close(); }
	}

}
