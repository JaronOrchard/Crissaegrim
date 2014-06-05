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
	
	private long startTime;
	private long duration;
	private long endTime;
	private final ImmobilizingType immobilizingType;
	private final AttackableType attackableType;
	
	public Busy(long dur, ImmobilizingType immobilizingType, AttackableType attackableType) {
		startTime = new Date().getTime();
		duration = dur;
		endTime = startTime + duration;
		this.immobilizingType = immobilizingType;
		this.attackableType = attackableType;
	}
	
	/**
	 * @return The recommended texture given the type of Busy and the time elapsed within it
	 */
	public abstract int getTexture();
	
	/**
	 * @return The amount of the Busy that has elapsed, where 0 is none of it, 1 is all of it,
	 * and 0.5 means the Busy is exactly half over
	 */
	protected double getAmountElapsed() {
		return ((double)(new Date().getTime() - startTime)/(double)duration);
	}
	
	/**
	 * @return {@code true} if the Busy has expired, {@code false} otherwise
	 */
	public boolean hasExpired() {
		return new Date().getTime() >= endTime;
	}
	
}
