package hybridsim;

import mobemu.algorithms.SPRINT;
import mobemu.node.Context;
import mobemu.node.Node;

public class HybridSim extends SPRINT {

	public HybridSim(int id, Context context, boolean[] socialNetwork,
			int dataMemorySize, int exchangeHistorySize, long seed,
			long traceStart, long traceEnd, boolean altruism, Node[] nodes,
			int cacheMemorySize) {
		super(id, context, socialNetwork, dataMemorySize, exchangeHistorySize, seed,
				traceStart, traceEnd, altruism, nodes, cacheMemorySize);
		// TODO Auto-generated constructor stub
	}
	
	

}
