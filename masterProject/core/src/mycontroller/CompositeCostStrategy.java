package mycontroller;

import utilities.Coordinate;

public abstract class CompositeCostStrategy implements CostStrategy {

	@Override
	public abstract float travelCost(Node fromNode, Node toNode, float carAngle);
	
	@Override
	public abstract float travelCost(Coordinate from, Node toNode, float carAngle);
	
	

}
