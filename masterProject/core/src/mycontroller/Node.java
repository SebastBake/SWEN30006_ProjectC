package mycontroller;

import java.util.ArrayList;

import tiles.MapTile;
import tiles.UtilityTile;
import utilities.*;

public class Node {
	
	private Coordinate coordinate;
	private MapTile tile;
	private boolean unexplored;
	private boolean exitTile;
	private float travelCost;
	private ArrayList<Edge> edges;
	private Node previous;
	
	/**
	 * Initialize the Node, assigning travel cost
	 * trap tile 20, wall tile 200, other tiles 1
	 * @param coordinate
	 * @param tile
	 * @param unexplored
	 */
	public Node(Coordinate coordinate, MapTile tile, boolean unexplored){
		this.coordinate = coordinate;
		this.tile = tile;
		this.unexplored = unexplored;
		if(tile.getName().equals("Utility")){
			if(((UtilityTile)tile).isExit()){
				this.exitTile = true;
			}
		} else {
			this.exitTile = false;
			if(tile.getName().equals("Trap")){
				travelCost = 20;
			}else if(tile.getName().equals("Wall")){
				travelCost = 200;
			}else{
				travelCost = 1;
			}
		}
	}

	public Coordinate getCoordinate(){
		return coordinate;
	}

	public MapTile getMapTile(){
		return tile;
	}
	
	public boolean isExitTile(){
		return exitTile;
	}
	
	public void registerEdge(Edge edge){
		edges.add(edge);
	}
	
	public ArrayList<Edge> getEdges(){
		return edges;
	}
	
	public float getCost(){
		return travelCost;
	}
	
	public void setCost(float cost){
		this.travelCost = cost;
	}
	
	public Node getPreviousNode(){
		return previous;
	}
	
	/**
	 * Change made
	 * Design flaw
	 * Get if the node is unexplored
	 * @return whether the node is unexplored or not
	 */
	public boolean isUnexplored(){
		return unexplored;
	}
	
	/**
	 * Change made
	 * Design flaw
	 * Set the node as unexplored or not
	 * @param unexplored
	 */
	public void setUnexplored(boolean unexplored){
		this.unexplored = unexplored;
	}

	

}
