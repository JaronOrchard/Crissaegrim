package doodads;

import items.Item;
import items.Items;
import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;
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
	
	public long getOreRespawnTime() { return oreRespawnTime; }
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
		if (type == OreType.RHICHITE)					{ return 0.8; } // Rhichite: 80%
		else if (type == OreType.VALENITE)				{ return 0.4; } // Valenite: 40%
		else if (type == OreType.SANDELUGE)				{ return 0.5; } // Sandeluge: 50%
		return 1;
	}
	
	@Override
	public boolean isActionable() { return hasOre; }
	
	@Override
	public String getActionIcon() { return "LEFT_CLICK"; }
	
	@Override
	public void draw() {
		Rect bounds = getBounds();
		GLDrawer.useTexture(hasOre ? rockTexture : Textures.DEPLETED_ROCK);
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
