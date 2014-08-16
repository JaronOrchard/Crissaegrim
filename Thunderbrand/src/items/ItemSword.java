package items;

import geometry.Coordinate;
import geometry.Rect;
import textures.Textures;

public class ItemSword extends Item {
	private static final long serialVersionUID = 1L;
	
	private String swordType;
	private int attackPower;
	private double height;
	private double length;
	
	public String getSwordType() { return swordType; }
	public int getAttackPower() { return attackPower; }
	public double getHeight() { return height; }
	public double getLength() { return length; }
	
	public ItemSword(String type) {
		super(type + " Sword", getTextureForSwordType(type));
		setUpAttributes(type);
		swordType = type;
	}
	
	private static int getTextureForSwordType(String type) {
		if (type.equals("Rhichite")) return Textures.ITEM_RHICHITE_SWORD;
		else if (type.equals("Tameike")) return Textures.ITEM_TAMEIKE_SWORD;
		return Textures.ITEM_UNKNOWN;
	}
	
	private void setUpAttributes(String type) {
		if (type.equals("Rhichite")) {
			attackPower = 1;
			height = 0.15;
			length = 1.0;
		} else if (type.equals("Tameike")) {
			attackPower = 2;
			height = 0.15;
			length = 1.0;
		} else {
			setUpAttributes("Rhichite");
		}
	}
	
	/**
	 * Given the lower inside corner of the sword swing rect, uses the sword's dimensions
	 * to calculate the {@link Rect} of the entire sword swing area.
	 * @param lowerInsideCorner The lower inside corner of the sword swing
	 * @param facingRight {@code true} if the sword is being swung to the right, {@code false} otherwise
	 * @return A {@link Rect} representing the sword swing area
	 */
	public Rect getSwordSwingRect(Coordinate lowerInsideCorner, boolean facingRight) {
		if (facingRight) {
			return new Rect(
					lowerInsideCorner,
					new Coordinate(lowerInsideCorner.getX() + length, lowerInsideCorner.getY() + height));
		} else {
			return new Rect(
					new Coordinate(lowerInsideCorner.getX() - length, lowerInsideCorner.getY()),
					new Coordinate(lowerInsideCorner.getX(), lowerInsideCorner.getY() + height));
		}
	}
	
}
