package hybridsim.location;

import hybridsim.HybridSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
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
	
	public List<Location> getLocationsForClientId(int id) {
		List<Location> clientLocations = new ArrayList<Location>();
		for (Location loc : this.locations.values()) {
			if (loc.getClientById(id) != null) {
				clientLocations.add(loc);
			}
		}
		
		return clientLocations;
	}
	
	public void dump() {
		if (!HybridSim.DEBUG) return;
		
		for (Location location : this.locations.values()) {
			location.dump();
		}
	}

}
