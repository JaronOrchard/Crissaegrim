package online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import players.Player;
import crissaegrim.Crissaegrim;
import datapacket.SendPlayerStatusPacket;

public class ValmanwayConnection {
	
	private List<String> VALMANWAY_HOSTNAMES = new ArrayList<String>();
	private int VALMANWAY_PORT;
	private static int CONNECTION_TIMEOUT_MILLIS = 1000;
	
	private Socket valmanwaySocket = null;
	
	private long lastPlayerStatusSendTime = 0;
	private static long PLAYER_STATUS_SEND_INTERVAL = 100;
	
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
	
	public void sendPlayerStatus() {
		if (Crissaegrim.getTime() - lastPlayerStatusSendTime > PLAYER_STATUS_SEND_INTERVAL) {
			lastPlayerStatusSendTime = Crissaegrim.getTime();
			Player player = Crissaegrim.getPlayer();
			Crissaegrim.addOutgoingDataPacket(new SendPlayerStatusPacket(
					Crissaegrim.getBoard().getName(),
					player.getPosition().getX(),
					player.getPosition().getY(),
					player.getCurrentTexture(),
					player.getFacingRight()));
		}
	}
	
	public void closeConnections() throws IOException {
		if (valmanwaySocket != null) { valmanwaySocket.close(); }
	}

}
