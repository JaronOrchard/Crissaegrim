package chatbox;

import java.awt.Color;

public class ChatBox {
	
	private ChatHistory chatHistory;
	private ChatTypingArea chatTypingArea;
	
	public ChatBox() {
		chatHistory = new ChatHistory(this);
		chatTypingArea = new ChatTypingArea(this);
	}
	
	public void addChatMessage(String message, Color color) {
		chatHistory.addChatMessage(message, color);
	}
	
	public void draw() {
		chatHistory.draw();
		chatTypingArea.draw();
	}
	
	public void enableTypingMode() {
		chatTypingArea.enableTypingMode();
	}
	
	/**
	 * @return {@code true} if the player is currently in typing mode and entering a line of text, {@code false} otherwise
	 */
	public boolean isTypingMode() {
		return chatTypingArea.isTypingMode();
	}
	
	public void getKeyboardInput() {
		chatTypingArea.getKeyboardInput();
	}
	
}
