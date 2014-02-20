package busy;

import java.util.Date;

public abstract class Busy {
	
	private long startTime;
	private long duration;
	private long endTime;
	
	public Busy(long dur) {
		startTime = new Date().getTime();
		duration = dur;
		endTime = startTime + duration;
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
