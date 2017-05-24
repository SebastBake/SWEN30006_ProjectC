package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.*;
import utilities.Coordinate;

public class Graph {
	
	private HashMap<Coordinate, Node> nodeMap;
	private CostStrategy costStrategy;
	
	public void updateGraph(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	public void getPathList(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private Node generateBestDestination(){
		
	}
	
	private boolean feasibleVisit(Node fromNode, Node toNode){
		
	}
	
	private ArrayList<Node> getGraphNodesInView(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private void updateExploredNodes(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private void updateUnExploredNodes(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private void updateEdges(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private ArrayList<MapTile> lineOfSight(Coordinate c1, Coordinate c2){
		
	}
	
	/**
	 * Remove the given node from the HashMap
	 * @param node
	 */
	private void removeNode(Node n){
		nodeMap.remove(n.getCoordinate());
	}
	
	/**
	 * Add the given node to the HashMap
	 * @param node
	 */
	private void addNode(Node n){
		nodeMap.put(n.getCoordinate(), n);
	}

}
