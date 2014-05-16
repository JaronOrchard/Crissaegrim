package geometry;

/**
 * Utility methods for the {@link Coordinate} class
 */
public final class CoordinateUtils {
	
	/**
	 * @param startingCoordinate The starting {@link Coordinate}
	 * @param endingCoordinate The ending Coordinate (after the Coordinate has moved)
	 * @return {@code true} if the endingCoordinate has moved upward in relation
	 * to the startingCoordinate, {@code false} otherwise
	 */
	public static boolean isMovingUp(Coordinate startingCoordinate, Coordinate endingCoordinate) {
		return startingCoordinate.getY() < endingCoordinate.getY();
	}
	
	/**
	 * @param startingCoordinate The starting {@link Coordinate}
	 * @param endingCoordinate The ending Coordinate (after the Coordinate has moved)
	 * @return {@code true} if the endingCoordinate has moved downward in relation
	 * to the startingCoordinate, {@code false} otherwise
	 */
	public static boolean isMovingDown(Coordinate startingCoordinate, Coordinate endingCoordinate) {
		return startingCoordinate.getY() > endingCoordinate.getY();
	}
	
	/**
	 * @param startingCoordinate The starting {@link Coordinate}
	 * @param endingCoordinate The ending Coordinate (after the Coordinate has moved)
	 * @return {@code true} if the endingCoordinate has moved to the left in relation
	 * to the startingCoordinate, {@code false} otherwise
	 */
	public static boolean isMovingLeft(Coordinate startingCoordinate, Coordinate endingCoordinate) {
		return startingCoordinate.getX() > endingCoordinate.getX();
	}
	
	/**
	 * @param startingCoordinate The starting {@link Coordinate}
	 * @param endingCoordinate The ending Coordinate (after the Coordinate has moved)
	 * @return {@code true} if the endingCoordinate has moved to the right in relation
	 * to the startingCoordinate, {@code false} otherwise
	 */
	public static boolean isMovingRight(Coordinate startingCoordinate, Coordinate endingCoordinate) {
		return startingCoordinate.getX() < endingCoordinate.getX();
	}
	
}
