package hybridsim.location;

import java.util.HashMap;
import java.util.Map;

public class World {
	final boolean DEBUG = true;
	protected Map<String, Location> locations = null;
	protected int nrLocations = 0;

	public World() {
		this.locations = new HashMap<String,Location>();
	}
	
	
	public void addLocation(String name) {
		Location location = new Location(this.nrLocations++, name);
		this.locations.put(name, location);
	}
	
	public boolean isLocationSet(String name) {
		return this.locations.containsKey(name);
	}
	
	public Location getLocation(String name) {
		return this.locations.get(name);
	}
	
	public void syncData() {
		for (Location location : this.locations.values()) {
			location.syncData();
		}
	}
	
	public void debug() {
		if (!DEBUG) return;
		
		for (Location location : this.locations.values()) {
			location.debug();
		}
	}

}
