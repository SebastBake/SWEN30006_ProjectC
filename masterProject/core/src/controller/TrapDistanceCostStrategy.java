package controller;

import utilities.Coordinate;

/**
 * cost strategy which prioritises less traps, then less distance
 */
public class TrapDistanceCostStrategy extends CompositeCostStrategy {
	private static final int SHIFTY = 1000000; // used to shift distance backwards by several decimal points
	
	@Override
	public float travelCost(Coordinate fromCoord, Node toNode, float carAngle){
		
		TrapCostStrategy trapCoster 		= new TrapCostStrategy();
		DistanceCostStrategy distanceCoster = new DistanceCostStrategy();
		
		float trapCost = trapCoster.travelCost(fromCoord, toNode, carAngle);
		float distanceCost = distanceCoster.travelCost(fromCoord, toNode, carAngle);
		
		return distanceCost/SHIFTY + trapCost;
		
	}
	
	@Override
	public float travelCost(Node fromNode, Node toNode, float carAngle) {
		
		TrapCostStrategy trapCoster 		= new TrapCostStrategy();
		DistanceCostStrategy distanceCoster = new DistanceCostStrategy();
		
		float trapCost = trapCoster.travelCost(fromNode, toNode, carAngle);
		float distanceCost = distanceCoster.travelCost(fromNode, toNode, carAngle);
		
		return distanceCost/SHIFTY + trapCost;
	}

}
