package mycontroller;
import utilities.Coordinate;

public interface CostStrategy {
	
	// We were forced to add carAngle into our cost functions to force the car to prioritise nodes in front of it
	// Also added a travelCost function using coordinates, just for the costing the car coordinate to the node
	
	/**
	 * Used inside getBestDestination() (Dijstra) to determine the cost of visiting a node
	 * @param fromNode starting node
	 * @param toNode node to visit
	 * @param carAngle is the angle of the car
	 * @return the cost of visiting toNode from fromNode
	 */
	public float travelCost(Node fromNode, Node toNode, float carAngle);
	
	/**
	 * Used inside getBestDestination() (Dijstra) to determine the cost of visiting a node
	 * @param from starting coordinate
	 * @param toNode node to visit
	 * @param carAngle carAngle is the angle of the car
	 * @return the cost of visiting toNode from the from coordinate
	 */
	public float travelCost(Coordinate from, Node toNode, float carAngle);
	
}
