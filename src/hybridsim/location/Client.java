package hybridsim.location;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
	
	
	public Client(int clientId) {
		this.clientId = clientId;
		this.rawTime = new ArrayList<Long>();
		this.intervals = new ArrayList<Pair<Long,Long>>();
		this.currentInterval = new Pair<Long,Long>();
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
	
	public void debug() {
		System.out.println("Client: "+this.clientId+", intervals:\n");
		for (int i = 0; i < this.intervals.size(); i++ ) {
			System.out.println(i+". "+this.intervals.get(i).getFirst()+" - "+this.intervals.get(i).getSecond()+";\n");
		}
	}
}
