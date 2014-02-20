package entities;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import textures.Textures;
import crissaegrim.Coordinate;
import crissaegrim.Rect;

public class Door extends Entity {
	
	private static double DOOR_RADIUS = 0.5;
	private static double DOOR_HEIGHT = 3;
	
	private final String destinationBoardName;
	private final Coordinate destinationCoordinate;
	
	public String getDestinationBoardName() { return destinationBoardName; }
	public Coordinate getDestinationCoordinate() { return destinationCoordinate; }
	
	public Door(Coordinate bottomCenter, String destBoardName, Coordinate destCoord) {
		super(new Rect(
				new Coordinate(bottomCenter.getX() - DOOR_RADIUS, bottomCenter.getY()),
				new Coordinate(bottomCenter.getX() + DOOR_RADIUS, bottomCenter.getY() + DOOR_HEIGHT)));
		destinationBoardName = destBoardName;
		destinationCoordinate = destCoord;
	}

	@Override
	public void draw() {
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D, Textures.DOOR_CLOSED);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 0);
				glVertex2d(bounds.getLeft()-1, bounds.getTop()+1);
				glTexCoord2d(1, 0);
				glVertex2d(bounds.getRight()+1, bounds.getTop()+1);
				glTexCoord2d(1, 1);
				glVertex2d(bounds.getRight()+1, bounds.getBottom());
				glTexCoord2d(0, 1);
				glVertex2d(bounds.getLeft()-1, bounds.getBottom());
			glEnd();
		glPopMatrix();
	}

	@Override
	public void drawDebugMode() {
		glDisable(GL11.GL_TEXTURE_2D);
		glBegin(GL_LINE_LOOP);
			glColor3d(0, 1, 1);
			glVertex2d(bounds.getLeft(), bounds.getBottom());
			glVertex2d(bounds.getRight(), bounds.getBottom());
			glVertex2d(bounds.getRight(), bounds.getTop());
			glVertex2d(bounds.getLeft(), bounds.getTop());
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
	}
	
}
