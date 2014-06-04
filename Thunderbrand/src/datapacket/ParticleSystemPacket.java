package datapacket;

import geometry.Coordinate;

import java.awt.Color;

public class ParticleSystemPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int numParticles;
	private final Coordinate systemCenter;
	private final String boardName;
	private final Color systemColor;
	public int getNumParticles() { return numParticles; }
	public Coordinate getSystemCenter() { return systemCenter; }
	public String getBoardName() { return boardName; }
	public Color getSystemColor() { return systemColor; }
	
	public ParticleSystemPacket(int numParticles, Coordinate systemCenter, String boardName, Color systemColor) {
		super(DataPacketTypes.PARTICLE_SYSTEM_PACKET);
		this.numParticles = numParticles;
		this.systemCenter = systemCenter;
		this.boardName = boardName;
		this.systemColor = systemColor;
	}
	
}
