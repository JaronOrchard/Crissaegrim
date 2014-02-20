package textures;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import crissaegrim.Crissaegrim;
import static org.lwjgl.opengl.GL11.*;

/**
 * Loads textures I guess?  Taken in part from http://stackoverflow.com/questions/10801016/lwjgl-textures-and-strings
 */
public class TextureLoader {
	private static final int BYTES_PER_PIXEL = 4; // 3 for RGB, 4 for RGBA
	private static int NEXT_TEXT_TEXTURE_ID = 100000;
	private static Font TEXT_FONT = new Font("Arial", Font.PLAIN, 18);
	private static final Object LOCK_1 = new Object() {};
	
	public static void loadTexture(int textureId, BufferedImage image) {
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); // 4 for RGBA, 3 for RGB
		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));	// Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF));	// Green component
				buffer.put((byte) (pixel & 0xFF));			// Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF));	// Alpha component. Only for RGBA
			}
		}
		
		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS
		
		// You now have a ByteBuffer filled with the color data of each pixel.
		// Now just create a texture ID and bind it. Then you can load it using 
		// whatever OpenGL method you want, for example:
		
		glBindTexture(GL_TEXTURE_2D, textureId); // Bind texture ID
		
		// Setup wrap mode
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		// Setup texture scaling filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		// Send texel data to OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
	
	public static BufferedImage loadImage(InputStream textureStream) {
		try {
			//return ImageIO.read(DefenseStep.class.getResource(loc));
			//return ImageIO.read(textureFile);
			return ImageIO.read(textureStream);
		} catch (IOException e) {
			Crissaegrim.addErrorMessage("Problem creating texture from " + (textureStream == null ? "NULL" : textureStream.toString()) + ":");
			e.printStackTrace();
		}
		return null;
	}
	
	public static int createTextTexture(String message, Color color) {
		int textureId;
		synchronized(LOCK_1) {
			textureId = NEXT_TEXT_TEXTURE_ID++;
		}
		BufferedImage textImg = new BufferedImage(Crissaegrim.getWindowWidth(), 20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphTex = textImg.createGraphics();
		graphTex.setColor(color);
		Rectangle2D stringBounds = graphTex.getFontMetrics().getStringBounds(message, graphTex);
		graphTex.setFont(TEXT_FONT);
		graphTex.drawString(message, 0, (float)stringBounds.getHeight());
		loadTexture(textureId, textImg);
		return textureId;
	}
	
}
