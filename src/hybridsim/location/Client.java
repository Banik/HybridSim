package hybridsim.location;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TimeStampComparator implements Comparator<Long> {
    @Override
    public int compare(Long a, Long b) {
        if (a > b) { return 1;}
        if (a == b) { return 0;}
        
        return -1;
    }
}

public class Client {
	final long TIME_GAP_SECONDS = 3600;
	private int clientId;
	private List<Long> rawTime;
	private List<Pair<Long,Long>> intervals;
	private Pair<Long,Long> currentInterval;
	private Location myLocation;
	private Map<Integer, Double> socialTies;
	
	
	
	public Client(int clientId, Location location) {
		this.clientId = clientId;
		this.rawTime = new ArrayList<Long>();
		this.intervals = new ArrayList<Pair<Long,Long>>();
		this.currentInterval = new Pair<Long,Long>();
		this.myLocation = location;
		this.socialTies = new HashMap<Integer, Double>();
	}
	
	/**
	 * Method that ads an interval in which the client has been seen in a location
	 * @param interval
	 */
	public void addInterval() {
		this.intervals.add(this.currentInterval);
		this.currentInterval = new Pair<Long, Long>();
	}
	
	public void addRawTime(long timeSeen) {
		this.rawTime.add(timeSeen);
	}
	
	public void createIntervals() {
		this.currentInterval = new Pair<Long,Long>();
		this.rawTime.sort(new TimeStampComparator());
		
		this.currentInterval.setFirst(this.rawTime.get(0));
		for (int i=1 ; i<this.rawTime.size() ; i++) {
			long maxIntervalMills = this.rawTime.get(i-1) + TIME_GAP_SECONDS*1000;
			if (maxIntervalMills > this.rawTime.get(i)) {
				this.currentInterval.setSecond(this.rawTime.get(i));
				continue;
			} else {
				this.currentInterval.setSecond(maxIntervalMills);
				if (this.currentInterval.getFirst() != this.rawTime.get(i-1)) {
					this.currentInterval.setSecond(this.rawTime.get(i-1));
				}
				
				this.addInterval();
				this.currentInterval.setFirst(this.rawTime.get(i));
			}
		}
		
		/**
		 * Ads the last interval
		 */
		if (this.currentInterval.isFull()) {
			this.addInterval();
		}

		if (this.currentInterval.getFirst() != null && this.currentInterval.getSecond() == null) {
			this.currentInterval.setSecond(this.currentInterval.getFirst() + TIME_GAP_SECONDS*1000);
			this.addInterval();
		}
	}
	
	public List<Pair<Long,Long>> getIntervals() {
		return this.intervals;
	}
	
	private boolean isIntersection(Pair<Long,Long> interval1, Pair<Long,Long> interval2) {
		
		if (interval1.getSecond() > interval2.getFirst() ) {
			return true;
		}
		
		return false;
	}
	
	public boolean intersectedInterval(Pair<Long, Long> interval) {
		for (int i=0; i<this.intervals.size(); i++) {
			if (this.isIntersection(interval, this.intervals.get(i))) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/** Social tie functions. 
	 * These functions will help store the social ties between the current client and another within a location;
	 * **/
	
	/**
	 * Adds a new social tie for the current client
	 * @param clientId
	 * @param socialTie
	 */
	public void addSocialTie(int clientId, double socialTie) {
		
		if (this.socialTies.containsKey(clientId) && this.socialTies.get(clientId) > socialTie) {
			return;
		}
		
		this.socialTies.put(clientId, socialTie);
	}
	
	/**
	 * Retrieves a social tie of the current client for a given clientId
	 * @param clientId
	 * @return
	 */
	public double getSocialTieForClient(int clientId) {
		if (this.socialTies.containsKey(clientId)) {
			return this.socialTies.get(clientId);
		}
		
		return 0;
	}
	
	/**
	 * Increases the social tie of a client with a given increase number
	 * @param clientId
	 * @param increase
	 * @return the new socialTie value after the increase was made
	 */
	public double increaseSocialTie(int clientId, double increase) {
		if (this.socialTies.containsKey(clientId)) {
			this.socialTies.put(clientId, this.socialTies.get(clientId)+increase);
		}
		
		return this.socialTies.get(clientId);
	}
	
	/***************/
	
	public void dump() {
		System.out.println("Client: "+this.clientId+", intervals:\n");
		for (int i = 0; i < this.intervals.size(); i++ ) {
			System.out.println(i+". "+this.intervals.get(i).getFirst()+" - "+this.intervals.get(i).getSecond()+";\n");
		}
	}
}
