package doodads;

import static org.lwjgl.opengl.GL11.*;
import geometry.Coordinate;
import geometry.Rect;

import org.lwjgl.opengl.GL11;

import textures.Textures;

public class Target extends Doodad {
	
	private static double TARGET_RADIUS = 0.5;
	
	public Target(Coordinate center) {
		super(new Rect(
				new Coordinate(center.getX() - TARGET_RADIUS, center.getY() - TARGET_RADIUS),
				new Coordinate(center.getX() + TARGET_RADIUS, center.getY() + TARGET_RADIUS)));
	}

	@Override
	public void draw() {
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D, Textures.TARGET);
			glBegin(GL_QUADS);
				glTexCoord2d(0, 0);
				glVertex2d(bounds.getLeft(), bounds.getTop());
				glTexCoord2d(1, 0);
				glVertex2d(bounds.getRight(), bounds.getTop());
				glTexCoord2d(1, 1);
				glVertex2d(bounds.getRight(), bounds.getBottom());
				glTexCoord2d(0, 1);
				glVertex2d(bounds.getLeft(), bounds.getBottom());
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
