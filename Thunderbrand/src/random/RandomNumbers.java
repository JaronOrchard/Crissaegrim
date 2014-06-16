package random;

import java.util.Random;

public class RandomNumbers {
	
	private Random random;
	public RandomNumbers() { random = new Random(); }
	
	public double getDouble() { return random.nextDouble(); }
	public double getDoubleInRange(double lowerBound, double upperBound) { return (random.nextDouble() * (upperBound - lowerBound)) + lowerBound; }
	public int getIntInRange(int lowerBound, int upperBound) { return random.nextInt(upperBound - lowerBound + 1) + lowerBound; }
	public boolean getBoolean() { return random.nextBoolean(); }
	
}
