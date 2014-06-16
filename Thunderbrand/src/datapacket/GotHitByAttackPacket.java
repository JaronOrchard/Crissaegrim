package datapacket;

public class GotHitByAttackPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String attackerName;
	private final int damage;
	private final boolean bounceBack;
	private final boolean hitFromRightSide; // Used when determining bounce direction
	public String getAttackerName() { return attackerName; }
	public int getDamage() { return damage; }
	public boolean getBounceBack() { return bounceBack; }
	public boolean getHitFromRightSide() { return hitFromRightSide; }
	
	public GotHitByAttackPacket(String attackerName, int damage, boolean bounceBack, boolean hitFromRightSide) {
		super(DataPacketTypes.GOT_HIT_BY_ATTACK_PACKET);
		this.attackerName = attackerName;
		this.damage = damage;
		this.bounceBack = bounceBack;
		this.hitFromRightSide = hitFromRightSide;
	}
	
}
