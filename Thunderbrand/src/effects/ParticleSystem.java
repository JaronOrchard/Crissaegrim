package effects;

import java.awt.Color;

import geometry.Coordinate;
import gldrawer.GLDrawer;

public class ParticleSystem {
	
	private int numParticles;
	private Particle[] particles;
	private String boardName;
	
	public String getBoardName() { return boardName; }
	
	public ParticleSystem(int numParticles, Coordinate systemCenter, String boardName, Color systemColor) {
		this.numParticles = numParticles;
		particles = new Particle[numParticles];
		this.boardName = boardName;
		for (int i = 0; i < numParticles; i++) {
			particles[i] = new Particle(systemCenter, systemColor);
		}
	}
	
	public void draw() {
		GLDrawer.disableTextures();
		for (int i = 0; i < numParticles; i++) {
			if (particles[i] != null) {
				particles[i].draw();
			}
		}
	}
	
	/**
	 * @return {@code true} if the entire ParticleSystem has reached end-of-life, {@code false} otherwise
	 */
	public boolean update() {
		boolean endOfLife = true;
		for (int i = 0; i < numParticles; i++) {
			if (particles[i] != null) {
				if (particles[i].update()) {
					particles[i] = null;
				} else {
					endOfLife = false;
				}
			}
		}
		return endOfLife;
	}
	
}
