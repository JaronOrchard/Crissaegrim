package textblock;

import java.awt.Color;
import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import textures.TextureLoader;

public class TextTexture {
	
	private final int textureId;
	private final int width;
	private final long creationTime;
	
	public int getTextureId() { return textureId; }
	public int getWidth() { return width; }
	public long getCreationTime() { return creationTime; }
	
	public TextTexture(TextBlock tb) {
		Pair<Integer, Integer> idAndWidth = TextureLoader.createTextTexture(tb.getMessage(), tb.getColor());
		textureId = idAndWidth.getLeft();
		width = idAndWidth.getRight();
		creationTime = new Date().getTime();
	}
	
	public TextTexture(String message, Color color) {
		Pair<Integer, Integer> idAndWidth = TextureLoader.createTextTexture(message, color);
		textureId = idAndWidth.getLeft();
		width = idAndWidth.getRight();
		creationTime = new Date().getTime();
	}
	
	public void delete() {
		GL11.glDeleteTextures(textureId);
	}
		
}
