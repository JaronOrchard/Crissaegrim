package online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import players.Player;
import thunderbrand.Thunderbrand;
import crissaegrim.Crissaegrim;
import datapacket.SendPlayerStatusPacket;

public class ValmanwayConnection {
	
	private static int CONNECTION_TIMEOUT_MILLIS = 1000; // Milliseconds to attempted connection timeout
	
	private List<String> VALMANWAY_HOSTNAMES = new ArrayList<String>();
	private int VALMANWAY_PORT;
	
	private Socket valmanwaySocket = null;
	
	private long lastPlayerStatusSendTime = 0;
	private static long PLAYER_STATUS_SEND_INTERVAL = 50;
	
	public ValmanwayConnection() {
		if (Thunderbrand.isLinuxBuild()) {
			VALMANWAY_HOSTNAMES.add("199.204.23.93"); // VPS
		} else {
			VALMANWAY_HOSTNAMES.add("garnet");		// Laptop computer
			VALMANWAY_HOSTNAMES.add("96.35.6.105");	// Home desktop computer
		}
		VALMANWAY_PORT = 22112;
	}
	
	public void connectToValmonwayServer() throws IOException {
		boolean gotOnline = false;
		for (String hostname : VALMANWAY_HOSTNAMES) {
			try {
				valmanwaySocket = new Socket();
				valmanwaySocket.connect(new InetSocketAddress(hostname, VALMANWAY_PORT), CONNECTION_TIMEOUT_MILLIS);
				new CrissaegrimWriterThread(valmanwaySocket).start();
				new CrissaegrimReaderThread(valmanwaySocket).start();
				gotOnline = true;
			} catch (UnknownHostException e) {
				Crissaegrim.addSystemMessage("Don't know about host: " + hostname);
			} catch (IOException e) {
				Crissaegrim.addSystemMessage("Couldn't get I/O for the connection to: " + hostname);
			}
			if (gotOnline) {
				break; // Connected, so don't try more hostnames
			}
		}
		if (!gotOnline) {
			Crissaegrim.connectionStable = false; // Failed to connect to all servers; mark as offline
		}
	}
	
	public void sendPlayerStatus() {
		if (Thunderbrand.getTime() - lastPlayerStatusSendTime > PLAYER_STATUS_SEND_INTERVAL) {
			lastPlayerStatusSendTime = Thunderbrand.getTime();
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
