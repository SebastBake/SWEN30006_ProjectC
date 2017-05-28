package controller;

import java.util.ArrayList;

import tiles.MapTile;
import tiles.UtilityTile;
import utilities.*;
import world.World;

public class Node {
	
	private Coordinate coordinate;
	private boolean unexplored;
	private boolean exitTile;
	private Float travelCost;
	private ArrayList<Edge> edges;
	private Node previous;
	private boolean starter;
	
	
	/**
	 * Initialize the Node, assigning travel cost
	 * @param coordinate
	 * @param unexplored
	 */
	public Node(Coordinate coordinate, boolean unexplored){
		
		this.coordinate = coordinate;
		this.unexplored = unexplored;
		edges = new ArrayList<Edge>();
		travelCost = Float.MAX_VALUE;
		starter = false;
		
		MapTile tile = World.lookUp(coordinate.x, coordinate.y);
		if(tile.getName().equals("Utility")){
			if(((UtilityTile)tile).isExit()){
				this.exitTile = true;
			}
		}
	}

	/**
	 * starter boolean is used for path finding  in Graph.findBestDestination(...)
	 * @return whether the node is a starter node
	 */
	public boolean isStarter() {
		return starter;
	}

	/**
	 * starter boolean is used for path finding in Graph.findBestDestination(...)
	 * @param whether the node is a starter node
	 */
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	public Coordinate getCoordinate(){
		return coordinate;
	}
	
	/**
	 * is used for path finding in Graph.findBestDestination(...)
	 * @return whether the node is over an exit tile
	 */
	public boolean isExitTile(){
		return exitTile;
	}
	
	/**
	 * register an edge with the node
	 * @param edge to register
	 */
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
	
	public void setPreviousNode(Node n) {
		previous = n;
	}
	
	/**
	 * Get if the node is unexplored
	 * boolean is used in Graph
	 * @return whether the node is unexplored or not
	 */
	public boolean isUnexplored(){
		return unexplored;
	}
	
	/**
	 * Set the node as unexplored
	 * @param unexplored
	 */
	public void setUnexplored(boolean unexplored){
		this.unexplored = unexplored;
	}
	
	@Override
	public String toString() {
		return "Node x=" + coordinate.x + ", y=" +coordinate.y + ", unexplored=" + unexplored + ", exit" + exitTile;
	}

}
