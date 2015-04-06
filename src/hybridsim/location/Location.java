package hybridsim.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Location {
	final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	protected int ID;
	protected String name;
	protected Statistics stats;
	protected Map<Integer, Client> clients;
	
	/**
	 * This constructs the Location object used in the place model
	 * @param name
	 */
	public Location(int id, String name) {
		//populate the current object
		this.ID = id;
		this.name = name;
		
		this.clients = new HashMap<Integer, Client>();
		this.stats = new Statistics(this);
	}
	
	public Statistics getStatistics() {
		return this.stats;
	}
	
	public void addClient(int clientId, String seenDate) {

		if (!this.clients.containsKey(clientId)) {
			this.clients.put(clientId, new Client(clientId));
		}
		
		try {
			Date date = new SimpleDateFormat(dateFormat).parse(seenDate);
			this.clients.get(clientId).addRawTime(date.getTime());
		} catch (ParseException e) {
			System.err.println("Location exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public int getPopularity() {
		return this.clients.size();
	}
	
	public void syncData() {
		for (Client client : this.clients.values()) {
			client.createIntervals();
		}
	}
	
	public void debug() {
		System.out.println("Location: "+this.ID+", "+this.name+"\n--\nStats:\n");
		this.stats.debug();
		System.out.println("\n---\nClients:\n");
		for (Client client : this.clients.values()) {
			client.debug();
		}
		System.out.println("\n--------------\n\n");
	}


}
