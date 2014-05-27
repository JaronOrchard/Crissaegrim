package chatbox;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

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
		glColor3d(1.0, 1.0, 1.0);
		for (int i = 0; i < chatMessages.size(); i++) {
			glBindTexture(GL_TEXTURE_2D, chatMessages.get(i).getTextureId());
			if (!parentChatBox.isTypingMode()) {
				alpha = chatMessages.get(i).getAlpha();
				glColor4d(1.0, 1.0, 1.0, alpha);
			}
			glPushMatrix();
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(5, y);
					glTexCoord2d(1, 1);
					glVertex2d(5 + chatMessages.get(i).getWidth(), y);
					glTexCoord2d(1, 0);
					glVertex2d(5 + chatMessages.get(i).getWidth(), y + 20);
					glTexCoord2d(0, 0);
					glVertex2d(5, y + 20);
				glEnd();
			glPopMatrix();
			y += 20;
		}
		glColor3d(1.0, 1.0, 1.0);
	}
	
}
