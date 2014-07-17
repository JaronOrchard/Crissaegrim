package gldrawer;

import static org.lwjgl.opengl.GL11.*;
import geometry.Rect;

/**
 * GLDrawer is responsible for many helper methods that draw objects using OpenGL.
 */
public final class GLDrawer {
	
	/**
	 * Disables OpenGL texturing.
	 */
	public static void disableTextures() {
		glDisable(GL_TEXTURE_2D);
	}
	
	/**
	 * Enables OpenGL texturing and binds the given texture id.
	 * @param textureId The texture id of the texture to use
	 */
	public static void useTexture(int textureId) {
		clearColor();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	/**
	 * Sets OpenGL's current line width
	 * @param width The width of the lines to draw from now on
	 */
	public static void setLineWidth(float width) {
		glLineWidth(width);
	}
	
	/**
	 * Clears GL_COLOR_BUFFER_BIT and GL_DEPTH_BUFFER_BIT.
	 */
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColor() { setColor(1.0, 1.0, 1.0, 1.0); }
	public static void setColor(double red, double green, double blue) { setColor(red, green, blue, 1.0); }
	
	/**
	 * Sets the current OpenGL color.
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public static void setColor(double red, double green, double blue, double alpha) {
		glColor4d(red, green, blue, alpha);
	}
	
	/**
	 * Uses GL_QUADS to draw a rectangle or a texture.
	 * @param rect A {@link Rect} representing the boundaries of the quad
	 */
	public static void drawQuad(Rect rect) { drawQuad(rect.getLeft(), rect.getRight(), rect.getBottom(), rect.getTop()); }
	
	/**
	 * Uses GL_QUADS to draw a rectangle or a texture.
	 * @param left The left edge of the quad
	 * @param right The right edge of the quad
	 * @param bottom The bottom edge of the quad
	 * @param top The top edge of the quad
	 */
	public static void drawQuad(double left, double right, double bottom, double top) { draw(left, right, bottom, top, GL_QUADS); }
	
	/**
	 * Uses GL_LINE_LOOP to draw a rectangular outline.
	 * @param rect A {@link Rect} representing the boundaries of the rectangle
	 */
	public static void drawOutline(Rect rect) { drawOutline(rect.getLeft(), rect.getRight(), rect.getBottom(), rect.getTop()); }
	
	/**
	 * Uses GL_LINE_LOOP to draw a rectangular outline.
	 * @param left The left edge of the rectangle
	 * @param right The right edge of the rectangle
	 * @param bottom The bottom edge of the rectangle
	 * @param top The top edge of the rectangle
	 */
	public static void drawOutline(double left, double right, double bottom, double top) { draw(left, right, bottom, top, GL_LINE_LOOP); }
	
	private static void draw(double left, double right, double bottom, double top, int glDrawType) {
		boolean texturesEnabled = glIsEnabled(GL_TEXTURE_2D);
		glPushMatrix();
			if (texturesEnabled) { glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE); }
			glBegin(glDrawType);
				if (texturesEnabled) { glTexCoord2d(0, 1); }
				glVertex2d(left, bottom);
				if (texturesEnabled) { glTexCoord2d(1, 1); }
				glVertex2d(right, bottom);
				if (texturesEnabled) { glTexCoord2d(1, 0); }
				glVertex2d(right, top);
				if (texturesEnabled) { glTexCoord2d(0, 0); }
				glVertex2d(left, top);
			glEnd();
		glPopMatrix();
	}
	
	public static void drawLine(double x1, double y1, double x2, double y2) {
		glBegin(GL_LINES);
			glVertex2d(x1, y1);
			glVertex2d(x2, y2);
		glEnd();
	}
	
}
