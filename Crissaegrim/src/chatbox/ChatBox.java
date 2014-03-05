package chatbox;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;
import thunderbrand.TextBlock;

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
	
	public void addChatMessage(TextBlock tb) {
		chatHistory.addChatMessage(tb.getMessage(), tb.getColor());
	}
	
	public void draw() {
		chatHistory.draw();
		if (isTypingMode()) {
			drawSeparatingLine();
		}
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
	
	private void drawSeparatingLine() {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINE_STRIP);
			glColor4d(1, 1, 1, 1);
			glVertex2d(5, 31);
			glVertex2d(250, 31);
			glColor4d(1, 1, 1, 0);
			glVertex2d(400, 31);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glColor4d(1, 1, 1, 1);
	}
	
}
