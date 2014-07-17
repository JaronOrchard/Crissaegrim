package chatbox;

import java.awt.Color;
import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;
import static org.lwjgl.opengl.GL11.*;

import textures.TextureLoader;

public class ChatMessage {
	private final static long MILLIS_AT_FULL_ALPHA = 10000;
	private final static long MILLIS_TO_FADE_OUT = 1500;
	
	private final int textureId;
	private final int width;
	private final long creationTime;
	
	public int getTextureId() { return textureId; }
	public int getWidth() { return width; }
	
	public ChatMessage(String message, Color color) {
		Pair<Integer, Integer> idAndWidth = TextureLoader.createTextTexture(message, color);
		textureId = idAndWidth.getLeft();
		width = idAndWidth.getRight();
		creationTime = new Date().getTime();
	}
	
	public void delete() {
		glDeleteTextures(textureId);
	}
	
	public double getAlpha() {
		if (new Date().getTime() - creationTime < MILLIS_AT_FULL_ALPHA) {
			return 1;
		}
		return Math.max(0, (double)(MILLIS_TO_FADE_OUT - (new Date().getTime() - creationTime - MILLIS_AT_FULL_ALPHA)) / (double)MILLIS_TO_FADE_OUT);
	}
	
}
