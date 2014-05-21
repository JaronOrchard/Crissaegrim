package valmanway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import thunderbrand.TextBlock;
import thunderbrand.Thunderbrand;
import world.WorldRunnerThread;

public class Valmanway {
	
	private static volatile ValmanwaySharedData sharedData = new ValmanwaySharedData();
	public static ValmanwaySharedData getSharedData() { return sharedData; }
	
	private static ValmanwayLogger logger = new ValmanwayLogger();
	
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
        
        if (Thunderbrand.isLinuxBuild() || Thunderbrand.getStartWorldRunner()) {
        	new WorldRunnerThread().start();
        }
        
        while (listening) {
        	Socket acceptedSocket = serverSocket.accept();
        	ValmanwayUserData valmanwayUserData = new ValmanwayUserData(
        			Valmanway.getSharedData().getNextPlayerId(), Valmanway.getSharedData().getMostRecentChatMessageIndex());
        	new ValmanwayWriterThread(acceptedSocket, valmanwayUserData).start();
        	new ValmanwayReaderThread(acceptedSocket, valmanwayUserData).start();
        }
        
        logger.close();
        serverSocket.close();
	}
	
	public static void logMessage(TextBlock tb) {
		logger.log(tb);
	}
	
}
