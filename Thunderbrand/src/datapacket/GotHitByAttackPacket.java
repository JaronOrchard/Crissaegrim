package datapacket;

public class GotHitByAttackPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int attackerId;
	private final int damage;
	public int getAttackerId() { return attackerId; }
	public int getDamage() { return damage; }
	
	public GotHitByAttackPacket(int attackerId, int damage) {
		super(DataPacketTypes.GOT_HIT_BY_ATTACK_PACKET);
		this.attackerId = attackerId;
		this.damage = damage;
	}
	
}
