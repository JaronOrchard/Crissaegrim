package valmanway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datapacket.DataPacket;
import textblock.TextBlock;
import thunderbrand.Thunderbrand;
import world.WorldRunnerThread;

public class Valmanway {
	
	private static final int SERVER_VERSION = 20140918;
	public static int getServerVersion() { return SERVER_VERSION; }
	
	private static volatile ValmanwaySharedData sharedData = new ValmanwaySharedData();
	public static ValmanwaySharedData getSharedData() { return sharedData; }
	
	private static Map<Integer, ValmanwayUserData> users = new HashMap<Integer, ValmanwayUserData>();
	public static void sendPacketToPlayer(int playerId, DataPacket packet) {
		if (users.containsKey(playerId)) {
			users.get(playerId).addOutgoingDataPacket(packet);
		} else {
			System.out.println("Cannot send packet--player " + playerId + " does not exist!");
		}
	}
	
	private static ValmanwayLogger logger = new ValmanwayLogger();
	
	private static int nextNPCId = 1000000;
	public synchronized static int getNextNPCId() { nextNPCId++; return nextNPCId - 1; }
	
	private static List<String> boardNames;
	public static List<String> getBoardNames() {
		if (boardNames == null) {
			boardNames = Arrays.asList(
					"tower_of_preludes", "dawning", "dawning_interior", "sotn_clock_tower", "morriston", "barrett_station"
			);
		}
		return boardNames;
	}
	
	public static void main(String[] argv) throws IOException {
		int valmanwayServerPort = Thunderbrand.getValmanwayServerPort();
		ServerSocket serverSocket = null;
		boolean listening = true;
		
		System.out.println("Starting Valmanway server on port " + valmanwayServerPort + "...");
		logger.log("=== Server started ===");
		
		try {
			serverSocket = new ServerSocket(valmanwayServerPort);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + valmanwayServerPort + ".");
			System.exit(-1);
		}
		
		new WorldRunnerThread().start();
		
		while (listening) {
			Socket acceptedSocket = serverSocket.accept();
			ValmanwayUserData valmanwayUserData = new ValmanwayUserData(
					Valmanway.getSharedData().getNextPlayerId(), Valmanway.getSharedData().getMostRecentDataPacketIndex());
			users.put(valmanwayUserData.getPlayerId(), valmanwayUserData);
			new ValmanwayWriterThread(acceptedSocket, valmanwayUserData).start();
			new ValmanwayReaderThread(acceptedSocket, valmanwayUserData).start();
		}
		
		logger.close();
		serverSocket.close();
	}
	
	public static void logMessage(String message) { logger.log(message); }
	public static void logMessage(TextBlock tb) { logger.log(tb); }
	
}
