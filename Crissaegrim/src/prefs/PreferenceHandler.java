package prefs;

import java.util.prefs.Preferences;

public class PreferenceHandler {
	
	private Preferences prefs;
	
	public PreferenceHandler() {
		prefs = Preferences.userRoot().node("Crissaegrim");
	}
	
	public void setLastUsername(String username) { prefs.put("last_username", username); }
	public String getLastUsername() { return prefs.get("last_username", null); }
	
}
