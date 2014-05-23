package chatbox;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import thunderbrand.TextBlock;
import crissaegrim.Crissaegrim;
import datapacket.SendChatMessagePacket;

public class ChatTypingArea {
	
	private static final int MAXIMUM_MESSAGE_LENGTH = 250;
	
	private Map<String, ChatMessage> messageEntryTextures;
	private boolean typingModeEnabled;
	private String currentMessage;
	private Color currentColor;
	
	public ChatTypingArea() {
		messageEntryTextures = new HashMap<String, ChatMessage>();
		typingModeEnabled = false;
		resetState();
	}
	
	private int getMinimumMessageLength() {
		return Crissaegrim.getPlayer().getName().length() + 3;
	}
	
	private void resetState() {
		for (ChatMessage chatMessage : messageEntryTextures.values()) {
			chatMessage.delete();
		}
		messageEntryTextures.clear();
		currentMessage = "<" + (Crissaegrim.getPlayer() == null ? "NULL" : Crissaegrim.getPlayer().getName()) + "> ";
		currentColor = Color.WHITE;
	}
	
	public void draw() {
		if (typingModeEnabled) {
			int texture;
			String hashMapKey = currentColor.getRGB() + currentMessage;
			if (messageEntryTextures.containsKey(hashMapKey)) {
				texture = messageEntryTextures.get(hashMapKey).getTexture();
			} else {
				ChatMessage tempMsg = new ChatMessage(currentMessage, currentColor);
				messageEntryTextures.put(hashMapKey, tempMsg);
				texture = tempMsg.getTexture();
			}
			glBindTexture(GL_TEXTURE_2D, texture);
			glPushMatrix();
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(5, 5);
					glTexCoord2d(1, 1);
					glVertex2d(5 + 1024, 5);
					glTexCoord2d(1, 0);
					glVertex2d(5 + 1024, 25);
					glTexCoord2d(0, 0);
					glVertex2d(5, 25);
				glEnd();
			glPopMatrix();
		}
	}
	
	public void enableTypingMode() {
		resetState();
		typingModeEnabled = true;
	}
	
	/**
	 * @return {@code true} if the player is currently in typing mode and entering a line of text, {@code false} otherwise
	 */
	public boolean isTypingMode() {
		return typingModeEnabled;
	}
	
	public void getKeyboardInput() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) { // Key was pressed (not released)
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					typingModeEnabled = false;
					return; // Don't process any more keys!
				} else if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
					if (currentMessage.length() > getMinimumMessageLength()) {
						currentMessage = currentMessage.substring(0, currentMessage.length() - 1);
					} else if (currentColor != Color.WHITE) {
						resetState();
					}
				} else if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (!currentMessage.substring(getMinimumMessageLength()).trim().isEmpty()) {
						String message = currentMessage.substring(getMinimumMessageLength()).trim();
						Crissaegrim.addOutgoingDataPacket(new SendChatMessagePacket(new TextBlock(message, currentColor)));
					}
					typingModeEnabled = false;
					resetState();
					return; // Don't process any more keys!
				} else if (Keyboard.getEventCharacter() != 0 && currentMessage.length() < MAXIMUM_MESSAGE_LENGTH) {
					currentMessage += Keyboard.getEventCharacter();
					if (Keyboard.getEventCharacter() == '@') {
						checkForRecolor();
					}
				}
			}
		}
		while (Mouse.next()) {
			// Accept and then ignore all mouse clicks while in typing mode
		}
	}
	
	/**
	 * Apply text coloring effects based off of RuneScape's classic scheme
	 */
	private void checkForRecolor() {
		int minimumMessageLength = getMinimumMessageLength();
		if (currentMessage.length() == minimumMessageLength + 5 && currentMessage.charAt(minimumMessageLength) == '@') {
			Color colorChange = null;
			String colorRequest = currentMessage.substring(minimumMessageLength + 1, minimumMessageLength + 4);
			
			if (colorRequest.equalsIgnoreCase("bla")) colorChange = Color.BLACK;
			else if (colorRequest.equalsIgnoreCase("blu")) colorChange = Color.BLUE;
			else if (colorRequest.equalsIgnoreCase("cya")) colorChange = Color.CYAN;
			else if (colorRequest.equalsIgnoreCase("dre")) colorChange = new Color(201, 0, 0);
			else if (colorRequest.equalsIgnoreCase("gr1")) colorChange = new Color(216, 255, 0);
			else if (colorRequest.equalsIgnoreCase("gr2")) colorChange = new Color(144, 255, 0);
			else if (colorRequest.equalsIgnoreCase("gr3")) colorChange = Color.GREEN;
			else if (colorRequest.equalsIgnoreCase("gre")) colorChange = Color.GREEN;
			else if (colorRequest.equalsIgnoreCase("mag")) colorChange = Color.MAGENTA;
			else if (colorRequest.equalsIgnoreCase("or1")) colorChange = new Color(255, 185, 0);
			else if (colorRequest.equalsIgnoreCase("or2")) colorChange = new Color(255, 126, 0);
			else if (colorRequest.equalsIgnoreCase("or3")) colorChange = new Color(255, 50, 0);
			else if (colorRequest.equalsIgnoreCase("ora")) colorChange = new Color(255, 156, 69);
			else if (colorRequest.equalsIgnoreCase("lre")) colorChange = new Color(255, 156, 69);
			else if (colorRequest.equalsIgnoreCase("red")) colorChange = Color.RED;
			else if (colorRequest.equalsIgnoreCase("whi")) colorChange = Color.WHITE;
			else if (colorRequest.equalsIgnoreCase("yel")) colorChange = Color.YELLOW;
			
			if (colorChange != null) {
				currentColor = colorChange;
				currentMessage = "<" + Crissaegrim.getPlayer().getName() + "> ";
			}
		}
	}
	
}
