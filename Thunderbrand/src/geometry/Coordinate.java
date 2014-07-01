package geometry;

import java.io.Serializable;

public class Coordinate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double x;
	private double y;
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public void setX(double v) { x = v; }
	public void setY(double v) { y = v; }
	public void setAll(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setAll(Coordinate c) {
		x = c.getX();
		y = c.getY();
	}
	
	public void incrementX(double v) { x += v; }
	public void incrementY(double v) { y += v; }
	
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(Coordinate clone) {
		this.x = clone.getX();
		this.y = clone.getY();
	}
	
	/**
	 * @param c The {@link Coordinate} to compare to
	 * @return {@code true} if c's x and y match this Coordinate's x and y, {@code false} otherwise
	 */
	public boolean matchesCoordinate(Coordinate c) {
		return (x == c.getX() && y == c.getY());
	}
	
}
