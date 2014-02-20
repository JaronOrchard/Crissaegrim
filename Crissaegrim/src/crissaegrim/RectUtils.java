package crissaegrim;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for the {@link Rect} class
 */
public final class RectUtils {
	
	/**
	 * <i>Important: Assumes both {@link Rect}s are the same size.</i>
	 * @param startingRect The starting Rect
	 * @param endingRect The ending Rect (after the starting Rect has moved)
	 * @return {@code true} if the endingRect has moved upward in relation to the startingRect, {@code false} otherwise
	 */
	public static boolean isMovingUp(Rect startingRect, Rect endingRect) { return startingRect.getTop() < endingRect.getTop(); }
	
	/**
	 * <i>Important: Assumes both {@link Rect}s are the same size.</i>
	 * @param startingRect The starting Rect
	 * @param endingRect The ending Rect (after the starting Rect has moved)
	 * @return {@code true} if the endingRect has moved downward in relation to the startingRect, {@code false} otherwise
	 */
	public static boolean isMovingDown(Rect startingRect, Rect endingRect) { return startingRect.getTop() > endingRect.getTop(); }
	
	/**
	 * <i>Important: Assumes both {@link Rect}s are the same size.</i>
	 * @param startingRect The starting Rect
	 * @param endingRect The ending Rect (after the starting Rect has moved)
	 * @return {@code true} if the endingRect has moved to the left in relation to the startingRect, {@code false} otherwise
	 */
	public static boolean isMovingLeft(Rect startingRect, Rect endingRect) { return startingRect.getLeft() > endingRect.getLeft(); }
	
	/**
	 * <i>Important: Assumes both {@link Rect}s are the same size.</i>
	 * @param startingRect The starting Rect
	 * @param endingRect The ending Rect (after the starting Rect has moved)
	 * @return {@code true} if the endingRect has moved to the right in relation to the startingRect, {@code false} otherwise
	 */
	public static boolean isMovingRight(Rect startingRect, Rect endingRect) { return startingRect.getLeft() < endingRect.getLeft(); }
	
	/**
	 * @param rects The list of Rects to search.
	 * @return The {@link Rect} in the list with the highest {@link Rect#getBottom()} value. 
	 */
	public static Rect getFurthestRectFromBottom(List<Rect> rects) {
		if (rects.isEmpty()) { throw new IllegalArgumentException("List of Rects cannot be empty"); }
		Rect furthestFromBottom = rects.get(0);
		for (int i = 1; i < rects.size(); i++) {
			if (rects.get(i).getBottom() > furthestFromBottom.getBottom()) { furthestFromBottom = rects.get(i); }
		}
		return furthestFromBottom;
	}
	
	/**
	 * @param rects The list of Rects to search.
	 * @return The {@link Rect} in the list with the lowest {@link Rect#getTop()} value. 
	 */
	public static Rect getFurthestRectFromTop(List<Rect> rects) {
		if (rects.isEmpty()) { throw new IllegalArgumentException("List of Rects cannot be empty"); }
		Rect furthestFromTop = rects.get(0);
		for (int i = 1; i < rects.size(); i++) {
			if (rects.get(i).getTop() < furthestFromTop.getTop()) { furthestFromTop = rects.get(i); }
		}
		return furthestFromTop;
	}
	
	/**
	 * @param rects The list of Rects to search.
	 * @return The {@link Rect} in the list with the highest {@link Rect#getLeft()} value. 
	 */
	public static Rect getFurthestRectFromLeft(List<Rect> rects) {
		if (rects.isEmpty()) { throw new IllegalArgumentException("List of Rects cannot be empty"); }
		Rect furthestFromLeft = rects.get(0);
		for (int i = 1; i < rects.size(); i++) {
			if (rects.get(i).getLeft() > furthestFromLeft.getLeft()) { furthestFromLeft = rects.get(i); }
		}
		return furthestFromLeft;
	}
	
	/**
	 * @param rects The list of Rects to search.
	 * @return The {@link Rect} in the list with the lowest {@link Rect#getRight()} value. 
	 */
	public static Rect getFurthestRectFromRight(List<Rect> rects) {
		if (rects.isEmpty()) { throw new IllegalArgumentException("List of Rects cannot be empty"); }
		Rect furthestFromRight = rects.get(0);
		for (int i = 1; i < rects.size(); i++) {
			if (rects.get(i).getRight() < furthestFromRight.getRight()) { furthestFromRight = rects.get(i); }
		}
		return furthestFromRight;
	}
	
	/**
	 * Returns a list of {@link Line}s that make up the {@link Rect}.
	 * @param rect The Rect
	 * @return A list of {@link Line}s
	 */
	public static List<Line> getLinesFromRect(Rect rect) {
		List<Line> lines = new ArrayList<Line>();
		lines.add(new Line(new Coordinate(rect.getLeft(), rect.getBottom()), new Coordinate(rect.getLeft(), rect.getTop())));
		lines.add(new Line(new Coordinate(rect.getRight(), rect.getBottom()), new Coordinate(rect.getRight(), rect.getTop())));
		lines.add(new Line(new Coordinate(rect.getLeft(), rect.getBottom()), new Coordinate(rect.getRight(), rect.getBottom())));
		lines.add(new Line(new Coordinate(rect.getLeft(), rect.getTop()), new Coordinate(rect.getRight(), rect.getTop())));
		return lines;
	}
	
	public static boolean coordinateIsInRect(Coordinate coord, Rect rect) {
		return (!(coord.getX() < rect.getLeft() ||
				coord.getX() > rect.getRight() ||
				coord.getY() < rect.getBottom() ||
				coord.getY() > rect.getTop()));
	}
	
}
