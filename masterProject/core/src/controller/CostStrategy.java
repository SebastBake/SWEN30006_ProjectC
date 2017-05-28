package controller;
import utilities.Coordinate;

public interface CostStrategy {
	
	public float travelCost(Node fromNode, Node toNode);
	public float travelCost(Coordinate from, Node toNode);
	
}
