package busy;

import java.util.Date;

public abstract class Busy {
	
	protected enum ImmobilizingType { COMPLETELY_IMMOBILIZED, CANNOT_WALK, NOT_IMMOBILIZED }
	public boolean getCompletelyImmobilized() {
		return (immobilizingType == ImmobilizingType.COMPLETELY_IMMOBILIZED);
	}
	public boolean getCannotWalk() {
		return (immobilizingType == ImmobilizingType.COMPLETELY_IMMOBILIZED ||
				immobilizingType == ImmobilizingType.CANNOT_WALK);
	}
	
	protected enum AttackableType { CAN_BE_ATTACKED, IMMUNE_TO_ATTACKS }
	public boolean isImmuneToAttacks() {
		return (attackableType == AttackableType.IMMUNE_TO_ATTACKS);
	}
	
	protected final long startTime;
	private final ImmobilizingType immobilizingType;
	private final AttackableType attackableType;
	
	public Busy(ImmobilizingType immobilizingType, AttackableType attackableType) {
		startTime = new Date().getTime();
		this.immobilizingType = immobilizingType;
		this.attackableType = attackableType;
	}
	
	/**
	 * @return The recommended texture given the type of Busy and the time elapsed within it
	 */
	public abstract int getTexture();
	
	/**
	 * @return The number of milliseconds that have passed since the Busy has started
	 */
	protected long getMillisElapsed() { return new Date().getTime() - startTime; }
	
}
