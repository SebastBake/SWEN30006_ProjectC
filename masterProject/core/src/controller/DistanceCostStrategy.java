package controller;

import utilities.Coordinate;

public class DistanceCostStrategy implements CostStrategy {

	@Override
	public float travelCost(Node fromNode, Node toNode, float carAngle) {

		Coordinate node1 = fromNode.getCoordinate();
		Coordinate node2 = toNode.getCoordinate();
		
		float xDist = Math.max(node1.x, node2.x) - Math.min(node1.x, node2.x);
		float yDist = Math.max(node1.y, node2.y) - Math.min(node1.y, node2.y);
		
		double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		return (float)distance + fromNode.getCost();

	}
	
	@Override
	public float travelCost(Coordinate from, Node toNode, float carAngle) {
		
		float angle = (float) Math.abs(
									Math.abs(carAngle) - Math.abs(Math.atan2(
													from.y-toNode.getCoordinate().y, 
													from.x-toNode.getCoordinate().x)));
		Coordinate node2 = toNode.getCoordinate();
		
		float xDist = Math.max(from.x, node2.x) - Math.min(from.x, node2.x);
		float yDist = Math.max(from.y, node2.y) - Math.min(from.y, node2.y);
		
		double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		return (float)distance*angle;
	}

}
