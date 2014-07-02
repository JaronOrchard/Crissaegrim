package doodads;

import static org.lwjgl.opengl.GL11.*;
import items.Item;
import items.Items;
import geometry.Coordinate;
import geometry.Rect;
import textures.Textures;

public class MineableRock extends Doodad {
	private static final long serialVersionUID = 1L;
	
	private static transient double ROCK_RADIUS = 1;
	private static transient double ROCK_HEIGHT = 1;
	
	public enum OreType { RHICHITE, VALENITE, SANDELUGE, UNKNOWN }
	private final OreType oreType;
	private final int rockTexture;
	private final long oreRespawnTime;
	private boolean hasOre;
	
	public void setHasOre(boolean hasOre) { this.hasOre = hasOre; }
	public boolean isDepleted() { return !hasOre; }
	
	public String getOreString() {
		if (oreType == OreType.RHICHITE) { return "Rhichite"; }
		else if (oreType == OreType.VALENITE) { return "Valenite"; }
		else if (oreType == OreType.SANDELUGE) { return "Sandeluge"; }
		return "Unknown";
	}
	
	public Item getOreItem() {
		if (oreType == OreType.RHICHITE) { return Items.rhichiteOre(); }
		else if (oreType == OreType.VALENITE) { return Items.valeniteOre(); }
		else if (oreType == OreType.SANDELUGE) { return Items.sandelugeOre(); }
		return null;
	}
	
	public MineableRock(int id, Coordinate bottomCenter, OreType oreType) {
		super(id, DoodadActions.MINE_ROCK, new Rect(
				new Coordinate(bottomCenter.getX() - ROCK_RADIUS, bottomCenter.getY()),
				new Coordinate(bottomCenter.getX() + ROCK_RADIUS, bottomCenter.getY() + ROCK_HEIGHT)));
		this.oreType = oreType;
		rockTexture = getRockTexture(oreType);
		oreRespawnTime = getOreRespawnTime(oreType);
		hasOre = true;
	}
	
	private static int getRockTexture(OreType type) {
		if (type == OreType.RHICHITE) { return Textures.RHICHITE_ROCK; }
		else if (type == OreType.VALENITE) { return Textures.VALENITE_ROCK; }
		else if (type == OreType.SANDELUGE) { return Textures.SANDELUGE_ROCK; }
		return Textures.DEPLETED_ROCK;
	}
	
	private static long getOreRespawnTime(OreType type) {
		if (type == OreType.RHICHITE)					{ return 2500; } // Rhichite: 2.5 sec
		else if (type == OreType.VALENITE)				{ return 5000; } // Valenite: 5 sec
		else if (type == OreType.SANDELUGE)				{ return 4000; } // Sandeluge: 4 sec
		return 1000;
	}
	
	public double getChanceOfSuccess() { return getChanceOfSuccess(oreType); }
	public static double getChanceOfSuccess(OreType type) {
		if (type == OreType.RHICHITE)					{ return 0.8; }
		else if (type == OreType.VALENITE)				{ return 0.4; }
		else if (type == OreType.SANDELUGE)				{ return 0.5; }
		return 1;
	}
	
	@Override
	public boolean isActionable() { return hasOre; }
	
	@Override
	public void draw() {
		Rect bounds = getBounds();
		glPushMatrix();
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D, (hasOre ? rockTexture : Textures.DEPLETED_ROCK));
			glBegin(GL_QUADS);
				glTexCoord2d(0, 0);
				glVertex2d(bounds.getLeft(), bounds.getTop());
				glTexCoord2d(1, 0);
				glVertex2d(bounds.getRight(), bounds.getTop());
				glTexCoord2d(1, 1);
				glVertex2d(bounds.getRight(), bounds.getBottom());
				glTexCoord2d(0, 1);
				glVertex2d(bounds.getLeft(), bounds.getBottom());
			glEnd();
		glPopMatrix();
	}

	@Override
	public void drawDebugMode() {
		Rect bounds = getBounds();
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINE_LOOP);
			glColor3d(0, 1, 1);
			glVertex2d(bounds.getLeft(), bounds.getBottom());
			glVertex2d(bounds.getRight(), bounds.getBottom());
			glVertex2d(bounds.getRight(), bounds.getTop());
			glVertex2d(bounds.getLeft(), bounds.getTop());
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
}
