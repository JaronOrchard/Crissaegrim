package doodads;

import java.util.Arrays;
import java.util.List;

import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;
import textures.Textures;

public class Signpost extends Doodad {
	private static final long serialVersionUID = 1L;
	
	private static double SIGNPOST_RADIUS = 0.5;
	private static double SIGNPOST_HEIGHT = 2;
	
	private final List<String> messages;
	public List<String> getMessages() { return messages; }
	
	public Signpost(int id, Coordinate bottomCenter, List<String> message_list) {
		super(id, DoodadActions.READ_SIGNPOST, new Rect(
				new Coordinate(bottomCenter.getX() - SIGNPOST_RADIUS, bottomCenter.getY()),
				new Coordinate(bottomCenter.getX() + SIGNPOST_RADIUS, bottomCenter.getY() + SIGNPOST_HEIGHT)));
		messages = message_list;
	}
	
	public Signpost(int id, Coordinate bottomCenter, String message) { this(id, bottomCenter, Arrays.asList(message)); }
	
	@Override
	public boolean isActionable() { return true; }
	
	@Override
	public String getActionIcon() { return "F"; }
	
	@Override
	public void draw() {
		Rect bounds = getBounds();
		GLDrawer.useTexture(Textures.SIGNPOST);
		GLDrawer.drawQuad(bounds);
	}

	@Override
	public void drawDebugMode() {
		Rect bounds = getBounds();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0, 1, 1);
		GLDrawer.drawOutline(bounds);
	}
	
}
