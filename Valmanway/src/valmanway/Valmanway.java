package valmanway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Valmanway {
	
	private static int VALMANWAY_SERVER_PORT = 22112;
	private static volatile ValmanwaySharedData sharedData = new ValmanwaySharedData();
	public static ValmanwaySharedData getSharedData() { return sharedData; }
	
	public static void main(String[] argv) throws IOException {
		ServerSocket serverSocket = null;
        boolean listening = true;
        
        System.out.println("Starting Valmanway server on port " + VALMANWAY_SERVER_PORT + "...");

        try {
            serverSocket = new ServerSocket(VALMANWAY_SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + VALMANWAY_SERVER_PORT + ".");
            System.exit(-1);
        }

        while (listening) {
        	Socket acceptedSocket = serverSocket.accept();
        	ValmanwayUserData valmanwayUserData = new ValmanwayUserData(
        			Valmanway.getSharedData().getNextPlayerId(), Valmanway.getSharedData().getMostRecentChatMessageIndex());
        	new ValmanwayWriterThread(acceptedSocket, valmanwayUserData).start();
        	new ValmanwayReaderThread(acceptedSocket, valmanwayUserData).start();
        }

        serverSocket.close();
	}
	
}
