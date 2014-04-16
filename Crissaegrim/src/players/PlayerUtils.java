package players;

import geometry.Coordinate;
import geometry.Line;
import geometry.Rect;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
	
	public static Rect getPlayerBodyRect(Coordinate position) {
		return new Rect(
				new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight()),
				new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight() + Player.getPlayerBodyHeight()));
	}
	
	public static List<Line> getPlayerFeetLines(Coordinate position, boolean includeHorizontalFeetLine) {
		List<Line> feetLines = new ArrayList<Line>();
		feetLines.add(new Line(
				new Coordinate(position.getX(), position.getY()),
				new Coordinate(position.getX(), position.getY() + Player.getPlayerFeetHeight())));
		if (includeHorizontalFeetLine) {
			feetLines.add(new Line(
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY())));
			feetLines.add(new Line(
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() - (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight())));
			feetLines.add(new Line(
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY()),
					new Coordinate(position.getX() + (Player.getPlayerBodyWidth() / 2), position.getY() + Player.getPlayerFeetHeight())));
		}
		return feetLines;
	}
	
}
