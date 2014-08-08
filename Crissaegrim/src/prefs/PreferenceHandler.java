package prefs;

import java.util.prefs.Preferences;

public class PreferenceHandler {
	
	private Preferences prefs;
	
	public PreferenceHandler() {
		prefs = Preferences.userRoot().node("Crissaegrim");
	}
	
	public String getLastUsername() { return prefs.get("last_username", null); }
	public int getInventorySolais() { return prefs.getInt("inventory_solais", 0); }
	public String getInventoryItemsJson() { return prefs.get("inventory_items", null); }
	
	public void setLastUsername(String username) { prefs.put("last_username", username); }
	public void setInventorySolais(int solais) { prefs.putInt("inventory_solais", solais); }
	public void setInventoryItemsJson(String itemsJson) { prefs.put("inventory_items", itemsJson); }
	
}
