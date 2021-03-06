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
import entities.EntityStatus;

public class ValmanwayConnection {
	
	private int CONNECTION_TIMEOUT_MILLIS = 4000; // Milliseconds to attempted connection timeout
	public int getConnectionTimeoutMillis() { return CONNECTION_TIMEOUT_MILLIS; }
	
	private List<String> VALMANWAY_HOSTNAMES = new ArrayList<String>();
	
	private Socket valmanwaySocket = null;
	
	private long lastPlayerStatusSendTime = 0;
	private static long PLAYER_STATUS_SEND_INTERVAL = 50;
	
	public ValmanwayConnection() {
		if (Thunderbrand.isLinuxBuild()) {
			VALMANWAY_HOSTNAMES.add("199.204.23.93"); // Peridot (VPS)
		} else {
			VALMANWAY_HOSTNAMES.add("garnet");	      // Garnet (Laptop)
			VALMANWAY_HOSTNAMES.add("96.35.6.105");	  // Diamond (Desktop)
		}
	}
	
	public void connectToValmonwayServer() throws IOException {
		boolean gotOnline = false;
		for (String hostname : VALMANWAY_HOSTNAMES) {
			try {
				valmanwaySocket = new Socket();
				valmanwaySocket.connect(new InetSocketAddress(hostname, Thunderbrand.getValmanwayServerPort()), CONNECTION_TIMEOUT_MILLIS);
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
			Crissaegrim.addOutgoingDataPacket(new SendPlayerStatusPacket(new EntityStatus(
					player.getName(),
					Crissaegrim.getCurrentBoard().getName(),
					player.getPosition().getX(),
					player.getPosition().getY(),
					player.getCurrentTexture(),
					player.getFacingRight(),
					player.getHealthBar().getAmtHealth(),
					player.getTextureHalfWidth(),
					player.getTextureHeight())));
		}
	}
	
	public void closeConnections() throws IOException {
		if (valmanwaySocket != null) { valmanwaySocket.close(); }
	}

}
