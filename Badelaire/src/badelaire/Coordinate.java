package badelaire;

public class Coordinate {
	
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
	
}
