package vitals;

public class HealthBar {
	
	private int currentHealth;
	private int maxHealth;
	
	public int getCurrentHealth() { return currentHealth; }
	public int getMaxHealth() { return maxHealth; }
	public double getAmtHealth() { return (double)(currentHealth) / (double)(maxHealth); }
	
	public HealthBar(int maxHealth) {
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Takes damage by subtracting {@code damage} from {@code currentHealth}.
	 * @param damage The amount of damage to take
	 * @return {@code true} if HP reaches 0 as a result, {@code false} otherwise
	 */
	public boolean takeDamage(int damage) {
		currentHealth = Math.max(currentHealth - damage, 0);
		return currentHealth == 0;
	}
	
}
