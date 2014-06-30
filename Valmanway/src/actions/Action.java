package actions;

import thunderbrand.Thunderbrand;

public abstract class Action {
	
	private long actionTime;
	public long getActionTime() { return actionTime; }
	
	Action(long millis) {
		actionTime = Thunderbrand.getTime() + millis;
	}
	
}
