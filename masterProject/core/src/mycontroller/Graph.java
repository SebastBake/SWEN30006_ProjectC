package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import tiles.*;
import utilities.Coordinate;
import world.World;

public class Graph {
	
	private HashMap<Coordinate, Node> nodeMap;
	private CostStrategy costStrategy;
	
	public void updateGraph(Coordinate currentPosition, HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		updateUnexploredNodes(currentPosition, currentView, previousViews);
		updateExploredNodes(currentView, previousViews);
		updateEdges(currentView, previousViews);
		
	}
	
	public void getPathList(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		
	}
	
	private Node generateBestDestination(){
		return null;
		
	}
	
	private boolean feasibleVisit(Node fromNode, Node toNode){
		return false;
		
	}
	
	private ArrayList<Node> getGraphNodesInView(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		return null;
		
	}
	
	/**
	 * Remove nodes which were previously unexplored but are now in the view
	 * Add viewed nodes into the graph (only useful nodes which have an unobstructed path to are added)
	 * Useful nodes: Outside corner of a wall or trap, adjacent to a trap or an exit tile
	 * @param currentView
	 * @param previousViews (not used)
	 */
	private void updateExploredNodes(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		Coordinate[] coordinatesInView = (Coordinate[]) currentView.keySet().toArray();
		for(int i = 0; i < coordinatesInView.length; i++){
			Node currentNode = new Node(coordinatesInView[i], World.lookUp(coordinatesInView[i].x, coordinatesInView[i].y), false);
			if(nodeMap.containsKey(coordinatesInView[i])){
				removeNode(currentNode);
			}
			if(isUseful(currentNode)){
				addNode(currentNode);
			}
		}
		
	}
	
	/**
	 * Change made: Add currentPosition as a parameter
	 * Add unexplored nodes into the graph
	 * Added nodes are just outside the view, have coordinates not recorded in the previous views, and have an unobstructed path to them
	 * @param currentPositon 
	 * @param currentView
	 * @param previousViews
	 */
	private void updateUnexploredNodes(Coordinate currentPosition, HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		boolean isReachable = true;
		// check EAST
		for(int i = 0; i <  7; i++){
			if(!World.lookUp(currentPosition.x - 4, currentPosition.y - 3 + i).getName().equals("Empty") && !previousViews.containsKey(new Coordinate(currentPosition.x - 4, currentPosition.y - 3 + i))){
				isReachable = true;
				for(int j = 0 ; j < 3; j++){
					for(int k = 0; k < 7; k++){
						if(k == 6){
							if(World.lookUp(currentPosition.x - j, currentPosition.y - 3 + k).getName().equals("Wall")){
								isReachable = false;
							}
						}
						if(!World.lookUp(currentPosition.x - j, currentPosition.y - 3 + k).getName().equals("Wall")){
							break;
						}
					}
				}
				if(isReachable){
					addNode(new Node(new Coordinate(currentPosition.x - 4, currentPosition.y - 3 + i), World.lookUp(currentPosition.x - 4, currentPosition.y - 3 + i), true));
				}	
			} 
		}
		// check NORTH
		for(int i = 0; i <  7; i++){
			if(!World.lookUp(currentPosition.x - 3 + i, currentPosition.y + 4).getName().equals("Empty") && !previousViews.containsKey(new Coordinate(currentPosition.x - 3 + i, currentPosition.y + 4))){
				isReachable = true;
				for(int j = 0 ; j < 3; j++){
					for(int k = 0; k < 7; k++){
						if(k == 6){
							if(World.lookUp(currentPosition.x - 3 + k, currentPosition.y + j).getName().equals("Wall")){
								isReachable = false;
							}
						}
						if(!World.lookUp(currentPosition.x - 3 + k, currentPosition.y + j).getName().equals("Wall")){
							break;
						}
					}
				}
				if(isReachable){
					addNode(new Node(new Coordinate(currentPosition.x - 3 + i, currentPosition.y + 4), World.lookUp(currentPosition.x - 3 + i, currentPosition.y + 4), true));
				}	
			}
		}
		// check WEST
		for(int i = 0; i <  7; i++){
			if(!World.lookUp(currentPosition.x + 4, currentPosition.y + 3 - i).getName().equals("Empty") && !previousViews.containsKey(new Coordinate(currentPosition.x + 4, currentPosition.y + 3 - i))){
				isReachable = true;
				for(int j = 0 ; j < 3; j++){
					for(int k = 0; k < 7; k++){
						if(k == 6){
							if(World.lookUp(currentPosition.x + j, currentPosition.y + 3 - k).getName().equals("Wall")){
								isReachable = false;
							}
						}
						if(!World.lookUp(currentPosition.x + j , currentPosition.y + 3 - k).getName().equals("Wall")){
							break;
						}
					}
				}
				if(isReachable){
					addNode(new Node(new Coordinate(currentPosition.x + 4, currentPosition.y + 3 - i), World.lookUp(currentPosition.x + 4, currentPosition.y + 3 - i), true));
				}	
			}
		}
		// check SOUTH
		for(int i = 0; i <  7; i++){
			if(!World.lookUp(currentPosition.x - 3 + i, currentPosition.y - 4).getName().equals("Empty") && !previousViews.containsKey(new Coordinate(currentPosition.x - 3 + i, currentPosition.y - 4))){
				isReachable = true;
				for(int j = 0 ; j < 3; j++){
					for(int k = 0; k < 7; k++){
						if(k == 6){
							if(World.lookUp(currentPosition.x - 3 + j, currentPosition.y - 4 + k).getName().equals("Wall")){
								isReachable = false;
							}
						}
						if(!World.lookUp(currentPosition.x -3 + j , currentPosition.y - 4 + k).getName().equals("Wall")){
							break;
						}
					}
				}
				if(isReachable){
					addNode(new Node(new Coordinate(currentPosition.x - 3 + i, currentPosition.y - 4), World.lookUp(currentPosition.x - 3 + i, currentPosition.y - 4), true));
				}	
			}
		}	
	}
	
	/**
	 * Draws edges between every pair of nodes in the view which have a clear path between them (use lineOfSight()).
	 * Don't draw edges already in the graph.
	 * @param currentView
	 * @param previousViews
	 */
	private void updateEdges(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
	
	}
	
	/**
	 * Used in updateEdges() draw the line
	 * @param c1
	 * @param c2
	 * @return an ArrayList of MapTile representing all the MapTile that the line has
	 */
	private ArrayList<MapTile> lineOfSight(Coordinate c1, Coordinate c2){
		return null;
		
	}
	
	/**
	 * Remove the given node (if exists) from the HashMap
	 * @param node
	 */
	private void removeNode(Node n){
		if(nodeMap.containsValue(n)){
			nodeMap.remove(n.getCoordinate());
		}	
	}
	
	/**
	 * Add the given node to the HashMap
	 * @param node
	 */
	private void addNode(Node n){
		nodeMap.putIfAbsent(n.getCoordinate(), n);
	}
	
	/**
	 * Change made: decide if a given node is a useful node
	 * Useful nodes: Outside corner of a wall or trap, adjacent to a trap or an exit tile
	 */
	private boolean isUseful(Node n){
		Coordinate c = n.getCoordinate();
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				if(i == 1 && j == 1){
					continue;
				}
				if(World.lookUp(c.x - 1 + i, c.y - 1 + j).getName().equals("Wall") || World.lookUp(c.x - 1 + i, c.y - 1 + j).getName().equals("Trap") || n.isExitTile()){
					return true;
				}
			}
		}
		return false;
	}

}
