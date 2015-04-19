package hybridsim.location;

import hybridsim.HybridSim;

import java.util.List;


public class Statistics {
	
	protected Location location;
	
	protected final int ONE_HOUR_INTERVAL_MILLS = 1000 * 60 * 60;
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
	 * Method that computes the end time of the intersection between the two given clients intervals and calculates the future
	 * interval so that it returns the number of contacts between the two clients
	 * @param client1
	 * @param client2
	 * @param client1Interval
	 * @param client2Interval
	 * @param increase - the increase
	 * @return the number of contacts in the future interval
	 */
	private int checkInterval(Client client1, Client client2, Pair<Long, Long> client1Interval, Pair<Long, Long> client2Interval, int increase) {
		long endContactTime = client2Interval.getSecond();
		if(endContactTime > client1Interval.getSecond()) {
			endContactTime = client1Interval.getSecond();
		} 
		int contactTimes = 0;
		
		
		 
		Pair<Long, Long> futureInterval = new Pair<Long,Long>(
				endContactTime, 
				endContactTime+increase);
		
		List<Pair<Long, Long>> futureIntervalIntersections  = client1.intersectedInterval(futureInterval);
		for (Pair<Long, Long> futureIntersection : futureIntervalIntersections) {
			contactTimes += client2.intersectedInterval(futureIntersection).size();
		}
		
		return contactTimes;
		
	}
	
	/**
	 * This method will compute the social tie for a location between two clients.
	 * @param client1Id
	 * @param client2Id
	 * @return the calculated social tie
	 */
	public double computeSocialTie(int client1Id, int client2Id) {
		
		double socialTie = 0.0001;
		if (this.location.getClientById(client1Id) == null || this.location.getClientById(client2Id) == null) {
			HybridSim.debug("Statistics","Both clients do not exist "+client1Id+" - "+client2Id+" in location: "+this.location.getId());
			return 0;
		}
		
		Client client1 = this.location.getClientById(client1Id);
		Client client2 = this.location.getClientById(client2Id);
		
		/**
		 * If the social tie was already computed for the given clients
		 * get the stored value
		 * Increases performance
		 */
		if (client1.getSocialTieForClient(client2Id) != 0) {
			return client1.getSocialTieForClient(client2Id);
		}
		
		List<Pair<Long, Long>> intervalsC1 = client1.getIntervals();
		int totalNrOfContactsHours = 0;
		int totalNrOfContacts1Day = 0;
		
		for (int i=0; i<intervalsC1.size(); i++) {
			List<Pair<Long,Long>> foundContacts = client2.intersectedInterval(intervalsC1.get(i)); 
			if (!foundContacts.isEmpty()) {	
				// -1 because it counts the initial contact as well
				int nrOfContactsHours = this.checkInterval(client1, client2, intervalsC1.get(i), foundContacts.get(0), 4 * ONE_HOUR_INTERVAL_MILLS);
				int	nrOfContacts1Day = this.checkInterval(client1, client2, intervalsC1.get(i), foundContacts.get(0), ONE_DAY_INTERVAL_MILLS);
				
				/**
				 * TODO: maybe change the logic and remove hard-codings
				 * Computes the weight of the 4hour contacts vs the 24 hour contacts
				 */
				if (nrOfContacts1Day > 0 && nrOfContactsHours > 0 ) {
					if (nrOfContactsHours / 4 > nrOfContacts1Day / 24) {
						totalNrOfContactsHours++;
					} else {
						totalNrOfContacts1Day++;
					}
				}
			}
		}
		
		if (this.location.getPopularity() <= POPULARITY_THRESHOLD) {
			socialTie = 0.08;
			if (totalNrOfContactsHours > totalNrOfContacts1Day) {
				socialTie = 0.15;
			}
		}
		
		client1.addSocialTie(client2Id, socialTie);
		client2.addSocialTie(client1Id, socialTie);
		
		//HybridSim.debug("Statistics","Social tie for "+client1Id+" and "+client2Id+" was created in location "+this.location.getId()+" with value of "+socialTie+"\n");
		
		return socialTie;
	}
	
	public void buildSocialTieCache(List<Client> clientIds) {
		if (!HybridSim.BUILD_SOCIAL_TIE_CACHE) return;
		
		for(int i=0; i<clientIds.size()-1; i++) {
			for (int j=i+1; j<clientIds.size(); j++) {
				this.computeSocialTie(clientIds.get(i).getId(), clientIds.get(j).getId());
			}
		}
	}
	

}
