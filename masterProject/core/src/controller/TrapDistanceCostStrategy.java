package controller;

import utilities.Coordinate;

public class TrapDistanceCostStrategy extends CompositeCostStrategy {
	private static final int BIG_NUMBER = 10000;	
	
	@Override
	public float travelCost(Coordinate fromCoord, Node toNode){
		TrapCostStrategy trapCoster = new TrapCostStrategy();
		DistanceCostStrategy distanceCoster = new DistanceCostStrategy();
		
		float trapCost = trapCoster.travelCost(fromCoord, toNode);
		float distanceCost = distanceCoster.travelCost(fromCoord, toNode);
		
		return distanceCost/BIG_NUMBER + trapCost;
		
	}
	
	@Override
	public float travelCost(Node fromNode, Node toNode) {
		// TODO Auto-generated method stub
		
		TrapCostStrategy trapCoster = new TrapCostStrategy();
		DistanceCostStrategy distanceCoster = new DistanceCostStrategy();
		
		float trapCost = trapCoster.travelCost(fromNode, toNode);
		float distanceCost = distanceCoster.travelCost(fromNode, toNode);
		
		return distanceCost/BIG_NUMBER + trapCost;
	}

	@Override
	public float travelCost(Coordinate from, Node toNode) {
		return this.travelCost(new Node(from, false), toNode);
	}

}
