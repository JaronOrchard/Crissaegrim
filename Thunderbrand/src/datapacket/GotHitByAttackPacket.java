package datapacket;

public class GotHitByAttackPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String attackerName;
	private final int damage;
	public String getAttackerName() { return attackerName; }
	public int getDamage() { return damage; }
	
	public GotHitByAttackPacket(String attackerName, int damage) {
		super(DataPacketTypes.GOT_HIT_BY_ATTACK_PACKET);
		this.attackerName = attackerName;
		this.damage = damage;
	}
	
}
