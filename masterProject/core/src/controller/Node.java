package controller;

import java.util.ArrayList;

import tiles.MapTile;
import tiles.UtilityTile;
import utilities.*;
import world.World;

public class Node {
	
	private Coordinate coordinate;
	private MapTile tile;
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
		this.tile = World.lookUp(coordinate.x, coordinate.y);
		this.unexplored = unexplored;
		if(tile.getName().equals("Utility")){
			if(((UtilityTile)tile).isExit()){
				this.exitTile = true;
			}
		}
		travelCost = Float.MAX_VALUE;
		starter = false;
		edges = new ArrayList<Edge>();
	}

	/**
	 * @return the starter
	 */
	public boolean isStarter() {
		return starter;
	}

	/**
	 * @param starter the starter to set
	 */
	public void setStarter(boolean starter) {
		this.starter = starter;
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
	
	@Override
	public String toString() {
		return "Node x=" + coordinate.x + ", y=" +coordinate.y + ", unexplored=" + unexplored + ", exit" + exitTile;
	}

	

}
