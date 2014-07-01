package busy;

import java.util.Date;

/**
 * A type of {@link Busy} that expires after a certain amount of time has passed.
 */
public abstract class TimedBusy extends Busy {
	
	private final long duration;
	private final long endTime;
	
	public TimedBusy(long dur, ImmobilizingType immobilizingType, AttackableType attackableType) {
		super(immobilizingType, attackableType);
		duration = dur;
		endTime = startTime + duration;
	}
	
	/**
	 * @return {@code true} if the Busy has expired, {@code false} otherwise
	 */
	public boolean hasExpired() {
		return new Date().getTime() >= endTime;
	}
	
}
