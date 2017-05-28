package controller;

import utilities.Coordinate;

public class TrapDistanceCostStrategy extends CompositeCostStrategy {
	
	@Override
	public float travelCost(Node fromNode, Node toNode) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public float travelCost(Coordinate from, Node toNode) {
		return 0;
	}

}
