package datapacket;

public class GotHitByAttackPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final int attackerId;
	public int getAttackerId() { return attackerId; }
	
	public GotHitByAttackPacket(int attackerId) {
		super(DataPacketTypes.GOT_HIT_BY_ATTACK_PACKET);
		this.attackerId = attackerId;
	}
	
}
