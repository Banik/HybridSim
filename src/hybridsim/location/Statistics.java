package hybridsim.location;


public class Statistics {
	
	protected Location location;

	/**
	 * Constructor for the statistics object
	 * This object is used to record the statistics of each location, 
	 * being dependent on the Location object
	 * @param location
	 */
	public Statistics(Location location) {
		this.location = location;
	}
	
	public void debug() {
		return;
	}
	

}
