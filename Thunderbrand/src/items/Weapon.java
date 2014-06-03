package items;

public class Weapon extends Item {
	private static final long serialVersionUID = 1L;
	
	private final int attackPower;
	
	public int getAttackPower() { return attackPower; }
	
	public Weapon(String weaponName, int weaponTexture) {
		super(weaponName, weaponTexture);
		
		// Should be able to distinguish types of weapons, swords, etc.
		attackPower = 1;
		
	}
	
}
