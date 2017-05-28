package controller;

import utilities.Coordinate;

public abstract class CompositeCostStrategy implements CostStrategy {

	@Override
	public abstract float travelCost(Node fromNode, Node toNode);
	
	@Override
	public abstract float travelCost(Coordinate from, Node toNode);
	
	

}
