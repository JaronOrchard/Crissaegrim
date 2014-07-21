package geometry;

import java.util.Arrays;
import java.util.List;

public final class LineUtils {
	
	/**
	 * Detects if two lines intersect.  Taken from http://gamedev.stackexchange.com/questions/26004/how-to-detect-2d-line-on-line-collision
	 * <br/><br/>
	 * Here be dragons
	 * @param line1 The first line
	 * @param line2 The second line
	 * @return {@code true} if the lines intersect, {@code false} otherwise
	 */
	public static boolean linesIntersect(Line line1, Line line2) {
		//float denominator = ((b.X - a.X) * (d.Y - c.Y)) - ((b.Y - a.Y) * (d.X - c.X));
		//float numerator1 = ((a.Y - c.Y) * (d.X - c.X)) - ((a.X - c.X) * (d.Y - c.Y));
		//float numerator2 = ((a.Y - c.Y) * (b.X - a.X)) - ((a.X - c.X) * (b.Y - a.Y));
		double denominator = ((line1.getPoint2().getX() - line1.getPoint1().getX()) * (line2.getPoint2().getY() - line2.getPoint1().getY())) -
				((line1.getPoint2().getY() - line1.getPoint1().getY()) * (line2.getPoint2().getX() - line2.getPoint1().getX()));
		double numerator1 = ((line1.getPoint1().getY() - line2.getPoint1().getY()) * (line2.getPoint2().getX() - line2.getPoint1().getX())) -
				((line1.getPoint1().getX() - line2.getPoint1().getX()) * (line2.getPoint2().getY() - line2.getPoint1().getY()));
		double numerator2 = ((line1.getPoint1().getY() - line2.getPoint1().getY()) * (line1.getPoint2().getX() - line1.getPoint1().getX())) -
				((line1.getPoint1().getX() - line2.getPoint1().getX()) * (line1.getPoint2().getY() - line1.getPoint1().getY()));
		
//		// Detect coincident lines
//		if (denominator == 0) {
//			return (numerator1 == 0 && numerator2 == 0);
//		}
		// The above section returns true if lines are coincident but do not overlap.  We'll include a new test for this case:
//		if (denominator == 0 && numerator1 == 0 && numerator2 == 0) {
//			return coincidentLinesOverlap(line1, line2); // This isn't working either for some reason
//		}
		// Right now we're just going to throw out parallel/coincident lines until either we get a better solution or it becomes pressing.
		if (denominator == 0) {
			return false;
		}
		
		double r = numerator1 / denominator;
		double s = numerator2 / denominator;
		
		return (r >= 0 && r <= 1 && s >= 0 && s <= 1);
	}
	
//	private static boolean ccw(double x1, double y1, double x2, double y2, double x3, double y3) {
//		return (y3 - y1) * (x2 - x1) > (y2 - y1) * (x3 - x1);
//	}
//	
//	private static boolean coincidentLinesOverlap(Line line1, Line line2) {
//		return (ccw(line1.getPoint1().getX(), line1.getPoint1().getY(), line2.getPoint1().getX(), line2.getPoint1().getY(), line2.getPoint2().getX(), line2.getPoint2().getY()) !=
//				ccw(line1.getPoint2().getX(), line1.getPoint2().getY(), line2.getPoint1().getX(), line2.getPoint1().getY(), line2.getPoint2().getX(), line2.getPoint2().getY()) &&
//				ccw(line1.getPoint1().getX(), line1.getPoint1().getY(), line1.getPoint2().getX(), line1.getPoint2().getY(), line2.getPoint1().getX(), line2.getPoint1().getY()) !=
//				ccw(line1.getPoint1().getX(), line1.getPoint1().getY(), line1.getPoint2().getX(), line1.getPoint2().getY(), line2.getPoint2().getX(), line2.getPoint2().getY()));
//		//ccw(seg1.x1, seg1.y1, seg2.x1, seg2.y1, seg2.x2, seg2.y2) != ccw(seg1.x2, seg1.y2, seg2.x1, seg2.y1, seg2.x2, seg2.y2) &&
//		//ccw(seg1.x1, seg1.y1, seg1.x2, seg1.y2, seg2.x1, seg2.y1) != ccw(seg1.x1, seg1.y1, seg1.x2, seg1.y2, seg2.x2, seg2.y2);
//		
//		// This should be working but it's not, what is even the deal
//		// http://jsperf.com/coincident-line-segment-intersection/3
//	}
	
	/**
	 * Given a {@link Line} and a set of lines, see if any of the lines from the set intersect the single line.
	 * @param line1 The single line
	 * @param lines2 The set of lines
	 * @return {@code true} if any lines in {@code lines2} intersect {@code line1}, {@code false} otherwise
	 */
	public static boolean lineSetsIntersect(Line line1, List<Line> lines2) {
		return lineSetsIntersect(Arrays.asList(line1), lines2);
	}
	
	/**
	 * Given two lists of lines, see if any of the lines from the first list intersect any from the second list.
	 * @param lines1 The first list of lines
	 * @param lines2 The second list of lines
	 * @return {@code true} if any lines in {@code lines1} intersect any lines in {@code lines2}, {@code false} otherwise
	 */
	public static boolean lineSetsIntersect(List<Line> lines1, List<Line> lines2) {
		for (Line line1 : lines1) {
			for (Line line2 : lines2) {
				if (linesIntersect(line1, line2)) { return true; }
			}
		}
		return false;
	}
	
	/**
	 * @param line The {@link Line} to check
	 * @return {@code true} if the Line's second point is higher than the first, {@code false} otherwise 
	 */
	public static boolean isMovingUp(Line line) { return line.getPoint2().getY() > line.getPoint1().getY(); }
	
	/**
	 * @param line The {@link Line} to check
	 * @return {@code true} if the Line's second point is lower than the first, {@code false} otherwise 
	 */
	public static boolean isMovingDown(Line line) { return line.getPoint2().getY() < line.getPoint1().getY(); }
	
	/**
	 * @param line The {@link Line} to check
	 * @return {@code true} if the Line's second point is to the left of the first, {@code false} otherwise 
	 */
	public static boolean isMovingLeft(Line line) { return line.getPoint2().getX() < line.getPoint1().getX(); }
	
	/**
	 * @param line The {@link Line} to check
	 * @return {@code true} if the Line's second point is to the right of the first, {@code false} otherwise 
	 */
	public static boolean isMovingRight(Line line) { return line.getPoint2().getX() > line.getPoint1().getX(); }
	
}
