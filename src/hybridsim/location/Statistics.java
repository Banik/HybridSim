package hybridsim.location;

import hybridsim.HybridSim;

import java.util.List;


public class Statistics {
	
	protected Location location;
	
	protected final int TWO_HOUR_INTERVAL_MILLS = 1000 * 60 * 60 * 2;
	protected final int ONE_DAY_INTERVAL_MILLS = 1000 * 60 * 60 * 24;
	protected final int POPULARITY_THRESHOLD = 30;
	

	/**
	 * Constructor for the statistics object
	 * This object is used to record the statistics of each location, 
	 * being dependent on the Location object
	 * @param location
	 */
	public Statistics(Location location) {
		this.location = location;
	}
	
	/**
	 * Computes a future interval and checks against the two clients so 
	 * that we could see if they have a regular meeting in the current location
	 * 
	 * @param Client client1
	 * @param Client client2
	 * @param Pair initialInterval
	 * @param increase - the amount in milliseconds in which the initial interval will be increased
	 * @return boolean
	 */
	private boolean checkInterval(Client client1, Client client2, Pair<Long, Long> initialInterval, int increase) {
		
		Pair<Long, Long> futureInterval = new Pair<Long,Long>(
				initialInterval.getFirst()+increase, 
				initialInterval.getSecond()+increase);
		
		return client1.intersectedInterval(futureInterval) && client2.intersectedInterval(futureInterval);
		
	}
	
	public double computeSocialTie(int client1Id, int client2Id) {
		
		double socialTie = 0.0001;
		if (this.location.getClientById(client1Id) == null || this.location.getClientById(client2Id) == null) {
			this.debug("Both clients do not exist "+client1Id+" - "+client2Id+" in location: "+this.location.getId());
			return 0;
		}
		
		Client client1 = this.location.getClientById(client1Id);
		Client client2 = this.location.getClientById(client2Id);
		
		/**
		 * If the social tie was already computed for the given clients
		 * get the store value
		 * Increases performance
		 */
		if (client1.getSocialTieForClient(client2Id) != 0) {
			return client1.getSocialTieForClient(client2Id);
		}
		
		List<Pair<Long, Long>> intervalsC1 = client1.getIntervals();
		int nrOfContacts2Hours = 0;
		int nrOfContacts1Day = 0;
		
		for (int i=0; i<intervalsC1.size(); i++) {
			if (client2.intersectedInterval(intervalsC1.get(i))) {
				if (this.checkInterval(client1, client2, intervalsC1.get(i), TWO_HOUR_INTERVAL_MILLS)) {
					nrOfContacts2Hours++;
				}
				if (this.checkInterval(client1, client2, intervalsC1.get(i), ONE_DAY_INTERVAL_MILLS)) {
					nrOfContacts1Day++;
				}
			}
		}
		
		if (this.location.getPopularity() <= POPULARITY_THRESHOLD) {
			if (nrOfContacts2Hours > nrOfContacts1Day) {
				socialTie = 0.15;
			}
			socialTie = 0.08;
		}
		
		client1.addSocialTie(client2Id, socialTie);
		client2.addSocialTie(client1Id, socialTie);
		
		this.debug("Social tie for "+client1Id+" and "+client2Id+" was created in location "+this.location.getId()+" with value of "+socialTie);
		
		return socialTie;
	}
	
	public void debug(String string) {
		if (HybridSim.DEBUG) {
			System.out.println("Statistics: "+string+"\n");
		}
	}
	
	public void buildSocialTieCache(List<Client> clientIds) {
		if (!HybridSim.BUILD_SOCIAL_TIE_CACHE) return;
		
		for(int i=0; i<clientIds.size()-1; i++) {
			for (int j=i+1; j<clientIds.size(); j++) {
				this.computeSocialTie(clientIds.get(i).getId(), clientIds.get(j).getId());
			}
		}
	}
	
	public void dump() {
		//TODO: create a dump of the social ties
	}
	

}
