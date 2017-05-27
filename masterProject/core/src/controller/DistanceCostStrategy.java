package controller;

import utilities.Coordinate;

public class DistanceCostStrategy implements CostStrategy {

	@Override
	public float travelCost(Node fromNode, Node toNode) {

		

		Coordinate node1 = fromNode.getCoordinate();
		Coordinate node2 = toNode.getCoordinate();
		
		float xDist = Math.max(node1.x, node2.x) - Math.min(node1.x, node2.x);
		float yDist = Math.max(node1.y, node2.y) - Math.min(node1.y, node2.y);
		
		double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		return (float)distance;

	}

}
