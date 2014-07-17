package chatbox;

import gldrawer.GLDrawer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
	
	private static final int MAX_CHAT_MESSAGES = 8;
	private final ChatBox parentChatBox;
	
	private List<ChatMessage> chatMessages;
	
	public ChatHistory(ChatBox parentCB) {
		parentChatBox = parentCB;
		chatMessages = new ArrayList<ChatMessage>();
	}
	
	private void killChatMessage(int index) {
		chatMessages.get(index).delete();
		chatMessages.remove(index);
	}
	
	public void addChatMessage(String message, Color color) {
		while (chatMessages.size() >= MAX_CHAT_MESSAGES) {
			killChatMessage(MAX_CHAT_MESSAGES - 1);
		}
		chatMessages.add(0, new ChatMessage(message, color));
	}
	
	public void draw() {
		int y = 34;
		double alpha = 1.0;
		for (int i = 0; i < chatMessages.size(); i++) {
			GLDrawer.useTexture(chatMessages.get(i).getTextureId());
			if (!parentChatBox.isTypingMode()) {
				alpha = chatMessages.get(i).getAlpha();
			}
			GLDrawer.setColor(1.0, 1.0, 1.0, alpha);
			GLDrawer.drawQuad(5, 5 + chatMessages.get(i).getWidth(), y, y + 20);
			y += 20;
		}
		GLDrawer.clearColor();
	}
	
}
