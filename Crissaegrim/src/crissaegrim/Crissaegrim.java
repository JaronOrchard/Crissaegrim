package crissaegrim;

import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import online.ValmanwayConnection;
import board.Board;
import players.Player;
import chatbox.ChatBox;
import datapacket.DataPacket;
import entities.EntityStatus;

public class Crissaegrim {
	
	private static final int CLIENT_VERSION = 20140521;
	public static int getClientVersion() { return CLIENT_VERSION; }
	
	private static volatile GameRunner gameRunner = new GameRunner();
	private static volatile Player player = null;
	private static volatile Map<Integer, EntityStatus> ghosts = new HashMap<Integer, EntityStatus>();
	private static volatile ChatBox chatBox = new ChatBox();
	private static volatile ValmanwayConnection valmanwayConnection = new ValmanwayConnection();
	private static volatile List<DataPacket> outgoingDataPackets = Collections.synchronizedList(new ArrayList<DataPacket>());
	public static volatile boolean connectionStable = true;
	public static volatile boolean currentlyLoading = false;
	
	public static GameRunner getGameRunner() { return gameRunner; }
	public static Player getPlayer() { return player; }
	public static void initializePlayer(Map<String, Board> boardMap) { player = new Player(boardMap); }
	public static Map<Integer, EntityStatus> getGhosts() { return ghosts; }
	public static void setGhosts(Map<Integer, EntityStatus> g) { synchronized(Crissaegrim.getGhosts()) { ghosts = g; } }
	public static Board getBoard() { return player.getCurrentBoard(); }
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
	
	private static int windowWidth = 1024;
	private static int windowHeight = 768;
	
	public static int getWindowWidth() { return windowWidth; }
	public static int getWindowHeight() { return windowHeight; }
	
	private static int pixelsPerTile = 32; // For best results, use multiples of 16
	private static double windowWidthRadiusInTiles = ((double)windowWidth / 2.0 / (double)pixelsPerTile);
	private static double windowHeightRadiusInTiles = ((double)windowHeight / 2.0 / (double)pixelsPerTile);
	public static double getWindowWidthRadiusInTiles() { return windowWidthRadiusInTiles; }
	public static double getWindowHeightRadiusInTiles() { return windowHeightRadiusInTiles; }
	
	public static void toggleZoom() {
		pixelsPerTile = (pixelsPerTile % 32) + 16;
		windowWidthRadiusInTiles = ((double)windowWidth / 2.0 / (double)pixelsPerTile);
		windowHeightRadiusInTiles = ((double)windowHeight / 2.0 / (double)pixelsPerTile);
	}
	
	public static void toggleWindowSize() {
		// Toggles between 1024x768 and 800x640
		if (windowWidth == 1024) {
			windowWidth = 800;
			windowHeight = 640;
		} else {
			windowWidth = 1024;
			windowHeight = 768;
		}
		windowWidthRadiusInTiles = ((double)windowWidth / 2.0 / (double)pixelsPerTile);
		windowHeightRadiusInTiles = ((double)windowHeight / 2.0 / (double)pixelsPerTile);
		glViewport(0, 0, Crissaegrim.getWindowWidth(), Crissaegrim.getWindowHeight());
		try {
			Display.setDisplayMode(new DisplayMode(Crissaegrim.getWindowWidth(), Crissaegrim.getWindowHeight()));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argv) throws IOException {
		try {
			getGameRunner().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
