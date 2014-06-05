package geometry;

import java.io.Serializable;

import thunderbrand.Thunderbrand;

public class Rect implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Coordinate bottomLeftCorner;
	private Coordinate topRightCorner;
	
	public Coordinate getBottomLeftCorner() { return bottomLeftCorner; }
	public Coordinate getTopRightCorner() { return topRightCorner; }
	
	public Rect(Coordinate bottomLeftCorner, Coordinate topRightCorner) {
		this.bottomLeftCorner = bottomLeftCorner;
		this.topRightCorner = topRightCorner;
	}
	
	public Rect(Coordinate singlePoint) {
		this.bottomLeftCorner = singlePoint;
		this.topRightCorner = singlePoint;
	}
	
	public double getLeft() { return getBottomLeftCorner().getX(); }
	public double getRight() { return getTopRightCorner().getX(); }
	public double getTop() { return getTopRightCorner().getY(); }
	public double getBottom() { return getBottomLeftCorner().getY(); }
	
	public double getWidth() { return getRight() - getLeft(); }
	public double getHeight() { return getTop() - getBottom(); }
	public double getArea() { return getWidth() * getHeight(); }
	
	public Coordinate getRandomizedPointInRect() {
		return new Coordinate(
				Thunderbrand.getRandomNumbers().getDoubleInRange(getLeft(), getRight()),
				Thunderbrand.getRandomNumbers().getDoubleInRange(getBottom(), getTop()));
	}
}
