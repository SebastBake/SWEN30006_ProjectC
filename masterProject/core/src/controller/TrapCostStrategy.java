package controller;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

/**
 * calculate cost based on the number of trap nodes traversed
 */
public class TrapCostStrategy implements CostStrategy {

	
	// Follows a line between fromCoord and toNode, increases cost for every trap found
	@Override
	public float travelCost(Coordinate fromCoord, Node toNode, float carAngle){
		float cost = 0;
		int x = fromCoord.x;
		int y = fromCoord.y;

		MapTile temp;
		
		double opposite = toNode.getCoordinate().x - x;
		double adjacent = toNode.getCoordinate().y - y;
		double gradient = Math.atan(opposite/adjacent);
		
		int distance = (int)Math.sqrt(Math.pow(opposite, 2) + Math.pow(adjacent, 2));
		for(int i = 0; i<distance; i++){
			temp = World.lookUp(x + i * Math.cos(gradient), y + i * Math.sin(gradient));
			if(temp.getName().equals("Trap")) cost += 1;
		}
		return cost;
	}
	
	@Override
	public float travelCost(Node fromNode, Node toNode, float carAngle) {
		Coordinate coord = fromNode.getCoordinate();
		return travelCost(coord, toNode, carAngle);
		
	}

}
