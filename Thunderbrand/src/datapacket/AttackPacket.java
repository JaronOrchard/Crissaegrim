package datapacket;

import attack.Attack;

public class AttackPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final Attack attack;
	public Attack getAttack() { return attack; }
	
	public AttackPacket(Attack attack) {
		super(DataPacketTypes.ATTACK_PACKET);
		this.attack = attack;
	}
	
}
