package controller;

public class TrapDistanceCostStrategy extends CompositeCostStrategy {
	private static final int BIG_NUMBER = 10000;	
	
	@Override
	public float travelCost(Node fromNode, Node toNode) {
		// TODO Auto-generated method stub
		
		TrapCostStrategy trapCoster;
		DistanceCostStrategy distanceCoster;
		
		float trapCost = trapCoster.travelCost(fromNode, toNode);
		float distanceCost = distanceCoster.travelCost(fromNode, toNode);
		
		return distanceCost/BIG_NUMBER + trapCost;
	}

}
