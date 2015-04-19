package hybridsim;

import hybridsim.location.Location;
import hybridsim.location.World;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import mobemu.algorithms.SPRINT;
import mobemu.node.Context;
import mobemu.node.Node;

public class HybridSim extends SPRINT {
	
	public final static boolean DEBUG = true;
	public final static String DEBUG_FILE = "debug.txt";
	public static BufferedWriter logWriter = null;
	
	
	
	public final static boolean BUILD_SOCIAL_TIE_CACHE = true;
	public final static double SOCIAL_NETWORK_ST_INCREASE = 0.15;
	
	
	protected List<Location> myLocations = null;
	protected World world = null;

	public HybridSim(int id, Context context, boolean[] socialNetwork,
			int dataMemorySize, int exchangeHistorySize, long seed,
			long traceStart, long traceEnd, boolean altruism, Node[] nodes,
			int cacheMemorySize, World world) {
		super(id, context, socialNetwork, dataMemorySize, exchangeHistorySize, seed,
				traceStart, traceEnd, altruism, nodes, cacheMemorySize);
		
		this.world = world;
		this.myLocations = world.getLocationsForClientId(id);
		HybridSim.debug("Hybrid Sim","Node id "+id+" has visited "+this.myLocations.size()+" locations:\n");
		
	}
	
	public static void debug(String className, String message) {
		if (!HybridSim.DEBUG) {
			return;
		}
		
		//Create the logWriter if it's not already created
		if (HybridSim.logWriter == null) {
	        try {
	            File logFile = new File(HybridSim.DEBUG_FILE);
	            HybridSim.logWriter = new BufferedWriter(new FileWriter(logFile));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		try {
			HybridSim.logWriter.write(className+" : "+message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	

}
