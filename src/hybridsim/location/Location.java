package hybridsim.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
			this.clients.put(clientId, new Client(clientId, this));
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
		
		this.stats.buildSocialTieCache(new ArrayList<Client>(this.clients.values()));
	}
	
	public Client getClientById(int id) {
		
		if (this.clients.containsKey(id)) {
			return this.clients.get(id);
		}
		
		return null; 
	}
	
	public int getId() {
		return this.ID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void dump() {
		System.out.println("Location: "+this.ID+", "+this.name+"\n--\nStats:\n");
		System.out.println("\n---\nClients:\n");
		for (Client client : this.clients.values()) {
			client.dump();
		}
		System.out.println("\n--------------\n\n");
	}


}
