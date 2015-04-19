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
	
	/**
	 * This method will help add 0.15 to the social tie for all the location between
	 * two clients if they are friends on a social network.
	 */
	public void increaseSocialTieWithinSameCommunities(boolean[][] socialNetwork, int nrOfNodes){
		
		for(int clientId1 = 0; clientId1 < nrOfNodes-1 ; clientId1++)
			for (int clientId2 = clientId1 + 1; clientId2 < nrOfNodes; clientId2++) {
				if (socialNetwork[clientId1][clientId2] != true) {
					continue;
				}
				for (Location location : this.locations.values()) {
					Client client1 = location.getClientById(clientId1);
					Client client2 = location.getClientById(clientId2);
					if(client1 != null && client2 != null) {
						double oldST1 = client1.getSocialTieForClient(clientId2);
						double oldST2 = client2.getSocialTieForClient(clientId1);
						client1.addSocialTie(clientId2, oldST1 + HybridSim.SOCIAL_NETWORK_ST_INCREASE);
						client2.addSocialTie(clientId1, oldST2 + HybridSim.SOCIAL_NETWORK_ST_INCREASE);	
					}
					
				}
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
