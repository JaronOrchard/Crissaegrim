package crissaegrim;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import online.ValmanwayConnection;
import board.Board;
import player.PlayerStatus;
import players.Player;
import chatbox.ChatBox;
import datapacket.DataPacket;

public class Crissaegrim {
	
	private static volatile GameRunner gameRunner = new GameRunner();
	private static volatile Player player = new Player();
	private static volatile Map<Integer, PlayerStatus> ghosts = new HashMap<Integer, PlayerStatus>();
	private static volatile Board board = null;
	private static volatile ChatBox chatBox = new ChatBox();
	private static volatile ValmanwayConnection valmanwayConnection = new ValmanwayConnection();
	private static volatile List<DataPacket> outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());;
	public static volatile boolean connectionStable = true;
	
	public static GameRunner getGameRunner() { return gameRunner; }
	public static Player getPlayer() { return player; }
	public static Map<Integer, PlayerStatus> getGhosts() { return ghosts; }
	public static void setGhosts(Map<Integer, PlayerStatus> g) { synchronized(Crissaegrim.getGhosts()) { ghosts = g; } }
	public static Board getBoard() { return board; }
	public static void setBoard(Board b) { board = b; }
	public static ChatBox getChatBox() { return chatBox; }
	public static ValmanwayConnection getValmanwayConnection() { return valmanwayConnection; }
	
	public static void addOutgoingDataPacket(DataPacket dp) { outgoingDataPackets.add(dp); }
	public static boolean outgoingDataPacketsExist() { return !outgoingDataPackets.isEmpty(); }
	public static DataPacket popOutgoingDataPacket() { return outgoingDataPackets.remove(0); }
	
	private static boolean debugModeEnabled = false;
	public static boolean getDebugMode() { return debugModeEnabled; }
	public static void toggleDebugMode() {
		debugModeEnabled = !debugModeEnabled;
		addSystemMessage("Debug mode is now " + (debugModeEnabled ? "ON" : "OFF"));
	}
	
	public static void addErrorMessage(String message) { chatBox.addChatMessage(message, Color.RED); }
	public static void addSystemMessage(String message) { chatBox.addChatMessage(message, Color.GRAY); }
	public static void addSystemMessageIfDebug(String message) { if (debugModeEnabled) chatBox.addChatMessage(message, Color.GRAY); }
	
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = 768;
	private static final int CHUNK_SIDE_SIZE = 100;
	
	public static int getWindowWidth() { return WINDOW_WIDTH; }
	public static int getWindowHeight() { return WINDOW_HEIGHT; }
	public static int getChunkSideSize() { return CHUNK_SIDE_SIZE; }
	
	private static int pixelsPerTile = 32; // For best results, use multiples of 16
	private static double windowWidthRadiusInTiles = ((double)WINDOW_WIDTH / 2.0 / (double)pixelsPerTile);
	private static double windowHeightRadiusInTiles = ((double)WINDOW_HEIGHT / 2.0 / (double)pixelsPerTile);
	public static double getWindowWidthRadiusInTiles() { return windowWidthRadiusInTiles; }
	public static double getWindowHeightRadiusInTiles() { return windowHeightRadiusInTiles; }
	
	public static void toggleZoom() {
		pixelsPerTile = (pixelsPerTile % 32) + 16;
		windowWidthRadiusInTiles = ((double)WINDOW_WIDTH / 2.0 / (double)pixelsPerTile);
		windowHeightRadiusInTiles = ((double)WINDOW_HEIGHT / 2.0 / (double)pixelsPerTile);
	}
	
	public static void main(String[] argv) throws IOException {
		try {
			getGameRunner().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
