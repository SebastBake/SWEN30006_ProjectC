package controller;
import utilities.Coordinate;

public interface CostStrategy {
	
	public float travelCost(Node fromNode, Node toNode, float carAngle);
	public float travelCost(Coordinate from, Node toNode, float carAngle);
	
}
