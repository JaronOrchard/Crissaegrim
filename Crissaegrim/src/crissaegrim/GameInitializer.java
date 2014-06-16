package crissaegrim;

import static org.lwjgl.opengl.GL11.*;
import geometry.Coordinate;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public final class GameInitializer {
	
	private static final String CRISSAEGRIM_WINDOW_TITLE = "Project Crissaegrim";
	
	public static void initializeDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Crissaegrim.getWindowWidth(), Crissaegrim.getWindowHeight()));
			Display.create();
			Display.setTitle(CRISSAEGRIM_WINDOW_TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void initializeOpenGLFor2D() {
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0, 0, Crissaegrim.getWindowWidth(), Crissaegrim.getWindowHeight());
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		
		glEnable(GL11.GL_TEXTURE_2D);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	public static void initializeNewFrameForScene() {
		Coordinate playerPosition = Crissaegrim.getPlayer().getPosition();
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(playerPosition.getX() - Crissaegrim.getWindowWidthRadiusInTiles(),
				playerPosition.getX() + Crissaegrim.getWindowWidthRadiusInTiles(),
				playerPosition.getY() - Crissaegrim.getWindowHeightRadiusInTiles(),
				playerPosition.getY() + Crissaegrim.getWindowHeightRadiusInTiles(),
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
				Crissaegrim.getWindowWidth(),
				0,
				Crissaegrim.getWindowHeight(),
				1,
				-1);
		//glOrtho(xMin, xMax, yMin, yMax, 1, -1);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	public static void setWindowTitle(String title) {
		Display.setTitle(CRISSAEGRIM_WINDOW_TITLE + " | " + title);
	}
	
}
