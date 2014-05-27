package textures;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import textblock.TextBlock;
import textblock.TextTexture;

public class CommonTextures {
	
	private static Map<String, TextTexture> commonTextures = new HashMap<String, TextTexture>();
	
	public CommonTextures() { }
	
	/**
	 * Returns the texture id for the given message, in white.
	 * If the texture does not yet exist, it is created.
	 * @param message The message from which to make a texture
	 * @return The TextTexture for the image
	 */
	public TextTexture getTextTexture(String message) {
		return getTextTexture(message, Color.WHITE);
	}
	
	/**
	 * Returns the texture id for the given TextBlock.
	 * If the texture does not yet exist, it is created.
	 * @param tb The TextBlock from which to make a texture
	 * @return The TextTexture for the image
	 */
	public TextTexture getTextTexture(TextBlock tb) {
		return getTextTexture(tb.getMessage(), tb.getColor());
	}
	
	/**
	 * Returns the texture id for the given message and color.
	 * If the texture does not yet exist, it is created.
	 * @param message The message from which to make a texture
	 * @param color The color of the message
	 * @return The TextTexture for the image
	 */
	public TextTexture getTextTexture(String message, Color color) {
		String hexString = Integer.toHexString(color.getRGB());
		hexString = "00000000".substring(hexString.length()) + hexString;
		String concatenatedKey = message + hexString;
		TextTexture tex = commonTextures.get(concatenatedKey);
		if (tex != null) {
			return tex;
		} else {
			TextTexture newTex = new TextTexture(message, color);
			commonTextures.put(concatenatedKey, newTex);
			return newTex;
		}
	}
	
}
