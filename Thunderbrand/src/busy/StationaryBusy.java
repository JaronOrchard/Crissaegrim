package busy;

import geometry.Coordinate;

/**
 * A type of {@link Busy} that expires after the Entity has moved.
 */
public abstract class StationaryBusy extends Busy {
	
	private final Coordinate initialPosition;
	
	public StationaryBusy(Coordinate position, ImmobilizingType immobilizingType, AttackableType attackableType) {
		super(immobilizingType, attackableType);
		initialPosition = new Coordinate(position);
	}
	
	/**
	 * @return {@code true} if the Entity has moved, {@code false} otherwise
	 */
	public boolean hasMoved(Coordinate position) {
		return !initialPosition.matchesCoordinate(position);
	}
	
}
