package controller;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

public class TrapCostStrategy implements CostStrategy {

	@Override
	public float travelCost(Node fromNode, Node toNode) {
		float cost = 0;
		int x = fromNode.getCoordinate().x;
		int y = fromNode.getCoordinate().y;
		
		MapTile temp;
		
		double opposite = toNode.getCoordinate().x - x;
		double adjacent = toNode.getCoordinate().y - y;
		double angle = Math.atan(opposite/adjacent);
		
		int distance = (int)Math.sqrt(Math.pow(opposite, 2) + Math.pow(adjacent, 2));
		for(int i = 0; i<distance; i++){
			temp = World.lookUp(x + i * Math.cos(angle), y + i * Math.sin(angle));
			if(temp.getName().equals("Trap")) cost += 1;
		}
		// TODO Auto-generated method stub
		return cost;
	}

	@Override
	public float travelCost(Coordinate from, Node toNode) {
		return this.travelCost(new Node(from, false), toNode);
	}

}
