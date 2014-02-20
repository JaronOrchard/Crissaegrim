package chatbox;

import java.awt.Color;
import java.util.Date;

import org.lwjgl.opengl.GL11;

import textures.TextureLoader;

public class ChatMessage {
	private final static long MILLIS_AT_FULL_ALPHA = 10000;
	private final static long MILLIS_TO_FADE_OUT = 1500;
	
	private final int texture;
	private final long creationTime;
	
	ChatMessage(String message, Color color) {
		texture = TextureLoader.createTextTexture(message, color);
		creationTime = new Date().getTime();
	}
	
	public int getTexture() { return texture; }
	
	public void delete() {
		GL11.glDeleteTextures(texture);
	}
	
	public double getAlpha() {
		if (new Date().getTime() - creationTime < MILLIS_AT_FULL_ALPHA) {
			return 1;
		}
		return Math.max(0, (double)(MILLIS_TO_FADE_OUT - (new Date().getTime() - creationTime - MILLIS_AT_FULL_ALPHA)) / (double)MILLIS_TO_FADE_OUT);
	}
	
}
