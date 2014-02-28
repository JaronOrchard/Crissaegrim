package thunderbrand;

import java.awt.Color;
import java.io.Serializable;

public class TextBlock implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String message;
	private final Color color;
	
	public String getMessage() { return message; }
	public Color getColor() { return color; }
	
	public TextBlock(String m, Color c) {
		message = m;
		color = c;
	}
	
}
