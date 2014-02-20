package crissaegrim;

public class Line {
	private final Coordinate point1;
	private final Coordinate point2;
	
	public Coordinate getPoint1() { return point1; }
	public Coordinate getPoint2() { return point2; }
	
	public Line(Coordinate p1, Coordinate p2) {
		point1 = new Coordinate(p1);
		point2 = new Coordinate(p2);
	}
	
	public Line(double x1, double y1, double x2, double y2) {
		point1 = new Coordinate(x1, y1);
		point2 = new Coordinate(x2, y2);
	}
	
}
