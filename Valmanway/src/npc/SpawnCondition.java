package npc;

import geometry.Coordinate;
import geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import thunderbrand.Thunderbrand;

public class SpawnCondition {
	
	/**
	 * A list of Rects that denote possible areas an Entity can respawn.
	 */
	private List<Rect> spawningAreas;
	
	/**
	 * A list of Double ratios that denote the probability of choosing a certain spawning area Rect for
	 * respawning, based on the area of each Rect in comparison to the total area of all Rects.
	 * This list is populated with values like "25.0, 75.0, 100.0" where the first and last Rect would
	 * have a 25% chance of being chosen, and the middle Rect would have ea 50% chance of being chosen.
	 */
	private List<Double> spawningAreaChanceRatios;
	
	private long timeOfDeath = 0;
	private final long millisToRespawn;
	
	public SpawnCondition(Coordinate singleSpawningPoint, long millisToRespawn) { this(new Rect(singleSpawningPoint), millisToRespawn); }
	public SpawnCondition(Rect singleSpawningArea, long millisToRespawn) { this(Arrays.asList(singleSpawningArea), millisToRespawn); }
	public SpawnCondition(List<Rect> spawningAreas, long millisToRespawn) {
		this.spawningAreas = spawningAreas;
		this.millisToRespawn = millisToRespawn;
		calculateSpawningAreaChanceRatios();
	}
	
	public void setTimeOfDeath() { timeOfDeath = new Date().getTime(); }
	public long getTimeUntilRevived() { return (timeOfDeath + millisToRespawn - new Date().getTime()); }
	public boolean getRevivedYet() { return getTimeUntilRevived() <= 0; }
	
	public Coordinate getNewRespawnPoint() {
		double ratio = Thunderbrand.getRandomNumbers().getDouble();
		for (int i = 0; i < spawningAreaChanceRatios.size(); i++) {
			if (ratio <= spawningAreaChanceRatios.get(i)) {
				return spawningAreas.get(i).getRandomizedPointInRect();
			}
		}
		System.out.println("Error -- Could not figure out which Rect to respawn in!");
		return spawningAreas.get(0).getRandomizedPointInRect();
	}
	
	private void calculateSpawningAreaChanceRatios() {
		spawningAreaChanceRatios = new ArrayList<Double>();
		double totalArea = 0.0;
		for (Rect rect : spawningAreas) {
			totalArea += rect.getArea();
		}
		double latestRatio = 0.0;
		for (int i = 0; i < spawningAreas.size(); i++) {
			if (i == spawningAreas.size() - 1) {
				spawningAreaChanceRatios.add(1.0);
			} else {
				latestRatio += (spawningAreas.get(i).getArea() / totalArea);
				spawningAreaChanceRatios.add(latestRatio);
			}
		}
	}
	
}
