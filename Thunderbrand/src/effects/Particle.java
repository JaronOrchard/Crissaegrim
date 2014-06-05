package effects;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import random.RandomNumbers;
import thunderbrand.Thunderbrand;
import geometry.Coordinate;

public class Particle {
	
	private Coordinate position;
	private double red;
	private double green;
	private double blue;
	private final int framesToLive;
	private int framesLeft;
	private double halfWidth;
	private double deltaX;
	private double deltaY;
	
	private static final int MAX_COLOR_CHANGE = 50;
	private static final int MIN_FRAMES = 50;
	private static final int MAX_FRAMES = 150;
	private static final double MIN_WIDTH = 0.05;
	private static final double MAX_WIDTH = 0.2;
	private static final double MAX_INITIAL_VELOCITY = 0.025;
	private static final double GRAVITY_ACCELERATION = -0.0001;
	
	public Particle(Coordinate startingPosition, Color startingColor) {
		RandomNumbers randomNumbers = Thunderbrand.getRandomNumbers();
		position = new Coordinate(startingPosition);
		
		int redChange = randomNumbers.getIntInRange(-MAX_COLOR_CHANGE, MAX_COLOR_CHANGE);
		int greenChange = randomNumbers.getIntInRange(-MAX_COLOR_CHANGE, MAX_COLOR_CHANGE);
		int blueChange = randomNumbers.getIntInRange(-MAX_COLOR_CHANGE, MAX_COLOR_CHANGE);
		red = (double)(Math.max(0, Math.min(255, startingColor.getRed() + redChange))) / 255.0;
		green = (double)(Math.max(0, Math.min(255, startingColor.getGreen() + greenChange))) / 255.0;
		blue = (double)(Math.max(0, Math.min(255, startingColor.getBlue() + blueChange))) / 255.0;
		
		framesToLive = randomNumbers.getIntInRange(MIN_FRAMES, MAX_FRAMES);
		framesLeft = framesToLive;
		halfWidth = randomNumbers.getDoubleInRange(MIN_WIDTH, MAX_WIDTH) / 2;
		
		double angle = randomNumbers.getDoubleInRange(0, 2 * Math.PI);
		double velocity = randomNumbers.getDoubleInRange(0, MAX_INITIAL_VELOCITY);
		deltaX = Math.cos(angle) * velocity;
		deltaY = Math.sin(angle) * velocity;
	}
	
	public void draw() {
		glColor4d(red, green, blue, getAlpha());
		glBegin(GL_QUADS);
			glVertex2d(position.getX() - halfWidth, position.getY() + halfWidth);
			glVertex2d(position.getX() + halfWidth, position.getY() + halfWidth);
			glVertex2d(position.getX() + halfWidth, position.getY() - halfWidth);
			glVertex2d(position.getX() - halfWidth, position.getY() - halfWidth);
		glEnd();
	}
	
	/**
	 * @return {@code true} if this Particle has reached end-of-life, {@code false} otherwise
	 */
	public boolean update() {
		position.incrementX(deltaX);
		deltaY += GRAVITY_ACCELERATION;
		position.incrementY(deltaY);
		framesLeft--;
		return framesLeft <= 0;
	}
	
	private double getAlpha() {
		int twentyPercentLife = framesToLive / 5;
		if (framesLeft < twentyPercentLife) {
			return (double)(framesLeft) / (double)(twentyPercentLife);
		}
		return 1.0;
	}
	
}
