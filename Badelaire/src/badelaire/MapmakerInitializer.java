package badelaire;

import static org.lwjgl.opengl.GL11.*;
import geometry.Coordinate;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public final class MapmakerInitializer {
	
	public static void initializeDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Badelaire.getWindowWidth(), Badelaire.getWindowHeight()));
			Display.create();
			Display.setTitle("Badelaire");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void initializeOpenGLFor2D() {
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0, 0, Badelaire.getWindowWidth(), Badelaire.getWindowHeight());
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		
		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	public static void initializeNewFrameForScene(Coordinate center) {
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(center.getX() - Badelaire.getWindowWidthRadiusInTiles(),
				center.getX() + Badelaire.getWindowWidthRadiusInTiles(),
				center.getY() - Badelaire.getWindowHeightRadiusInTiles(),
				center.getY() + Badelaire.getWindowHeightRadiusInTiles(),
				1,
				-1);
		//glOrtho(xMin, xMax, yMin, yMax, 1, -1);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	public static void initializeNewFrameForWindow() {
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0,
				Badelaire.getWindowWidth(),
				0,
				Badelaire.getWindowHeight(),
				1,
				-1);
		//glOrtho(xMin, xMax, yMin, yMax, 1, -1);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
}
