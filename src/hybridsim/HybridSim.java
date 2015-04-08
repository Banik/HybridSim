package hybridsim;

import hybridsim.location.Location;
import hybridsim.location.World;

import java.util.List;

import mobemu.algorithms.SPRINT;
import mobemu.node.Context;
import mobemu.node.Node;

public class HybridSim extends SPRINT {
	
	public final static boolean DEBUG = true;
	
	
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
		
		if (DEBUG) {
			System.out.println("Hybrid Sim: Node id "+id+" has visited "+this.myLocations.size()+" locations:\n");
			for(Location loc : this.myLocations) {
				System.out.println("Location: "+loc.getId()+" -- "+loc.getName()+";\n");
			}
			System.out.println("---------------\n\n");
		}
		
		// TODO Auto-generated constructor stub
	}

	
	

}
