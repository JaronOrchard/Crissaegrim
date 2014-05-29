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
	
}
