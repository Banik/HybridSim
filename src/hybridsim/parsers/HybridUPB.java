package hybridsim.parsers;

import hybridsim.location.Location;
import hybridsim.location.World;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import mobemu.parsers.UPB;
import mobemu.parsers.UPB.UpbTrace;

interface ILocationFile {

	/**
	 * File constants
	 */
	final String LOCATION_TRACE_FILE = "traces" + File.separator + "upb-hyccups2012" + File.separator+ "wifis.csv";
	final int FILE_COLUMN_SEEN_DATE = 1;
	final int FILE_COLUMN_BSSID = 2;
	final int FILE_COLUMN_CLIENT_ID = 5;
	
}

public class HybridUPB extends UPB implements ILocationFile {
	
	protected World world = null;
	
	public HybridUPB(UpbTrace subtrace) {
		super(subtrace);
		this.world = new World();
		this.parseLocations();
	}
	
	@Override
	protected void parseUpb2012(String contacts, String social, String interests) {
		super.parseUpb2012(contacts, social, interests);
	}
	
	
	protected void parseLocations() {
		// parse proximity file.
        try {
            String line;
            FileInputStream fstream = new FileInputStream(LOCATION_TRACE_FILE);
            try (DataInputStream in = new DataInputStream(fstream)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                int nrLinesRead = 0;
                while ((line = br.readLine()) != null) {
                	
                	if (nrLinesRead == 0) {
                		nrLinesRead++;
                		continue; // jump over the header
                	}

                    String[] tokens;
                    String delimiter = ",";
                    tokens = line.split(delimiter);

                    String seenDate = tokens[FILE_COLUMN_SEEN_DATE];
                    String locationName = tokens[FILE_COLUMN_BSSID];
                    int clientId = Integer.parseInt(tokens[FILE_COLUMN_CLIENT_ID]) - 1;
                    
                    this.populateWorld(locationName, clientId, seenDate);
                    nrLinesRead ++;
                }
                
                this.world.syncData();
                this.world.debug();
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("HybridUPB Parser exception: " + e.getMessage());
        }
	}
	
	protected void populateWorld(String locationName, int clientId, String seenDate) {
		Location location = null;
		if (!this.world.isLocationSet(locationName)) {
			this.world.addLocation(locationName);
		}
		
		location = this.world.getLocation(locationName);
        location.addClient(clientId, seenDate);
	}

}
