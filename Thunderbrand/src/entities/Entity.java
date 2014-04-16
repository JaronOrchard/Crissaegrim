package entities;

import java.util.List;

import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;

/**
 * An Entity is a living object, like a Player or Monster.
 */
public abstract class Entity {
	
	// Default movement values to Player's
	protected double gravityAcceleration = -0.0045;
	protected double tileCollisionPadding = gravityAcceleration / -2;
	protected double gravityTerminalVelocity = -0.400;
	protected double horizontalAirPenaltyFactor = 0.3;
	protected double horizontalAirTaperOffFactor = 0.88;
	protected double horizontalAirTaperOffLimit = 0.01;
	
	public double getGravityAcceleration() { return gravityAcceleration; }
	public double getTileCollisionPadding() { return tileCollisionPadding; }
	public double getGravityTerminalVelocity() { return gravityTerminalVelocity; }
	public double getHorizontalAirPenaltyFactor() { return horizontalAirPenaltyFactor; }
	public double getHorizontalAirTaperOffFactor() { return horizontalAirTaperOffFactor; }
	public double getHorizontalAirTaperOffLimit() { return horizontalAirTaperOffLimit; }
	
	public abstract Rect getEntityBoundingRect(Coordinate position);
	public abstract List<Line> getEntityFeetLines(Coordinate position, boolean includeHorizontalFeetLine);
	
	public Entity() { }
	
}
