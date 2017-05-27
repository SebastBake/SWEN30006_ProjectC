package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tiles.*;
import utilities.Coordinate;
import world.World;

public class Graph {
	
	private static final boolean EXPLORED = true;
	
	private HashMap<Coordinate, Node> nodeMap;
	private CostStrategy costStrategy;
	
	public void updateGraph(Coordinate currentPosition, HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		updateUnexploredNodes(currentPosition, currentView, previousViews);
		updateExploredNodes(currentView, previousViews);
		updateEdges(currentView, previousViews);
	}
	
	public List<Node> getPathList(HashMap<Coordinate, MapTile> currentView, HashMap<Coordinate, MapTile> previousViews){
		Node best = generateBestDestination();
		return null;
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
		for(Coordinate viewedCoordinate : currentView.keySet()){
			
			// remove nodes which were previously unexplored
			if ( nodeMap.containsKey(viewedCoordinate) ) {
				if (nodeMap.get(viewedCoordinate).isUnexplored()) {
					removeNode(viewedCoordinate);
				}
			}
			
			if(isUseful(viewedCoordinate)){
				addNode(viewedCoordinate, EXPLORED);
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
					addNode(new Coordinate(currentPosition.x - 3 + i, currentPosition.y - 4), true);
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
	private ArrayList<MapTile> lineOfSight(Coordinate c1, Coordinate c2) {
		return null;
	}
	
	/**
	 * Remove the given node (if exists) from the HashMap
	 * @param c is the location of the node to delete
	 */
	private void removeNode(Coordinate c){
		int x = (int) Math.round(c.x);
		int y = (int) Math.round(c.y);
		Coordinate loc = new Coordinate(x, y);
		
		Node n = nodeLookup(loc);
		if(n!=null){
			nodeMap.remove(n.getCoordinate());
		}
	}
	
	/**
	 * Add the given node to the HashMap
	 * @param c is the location of the new node
	 * @param unexplored is a boolean - true for unexplored
	 */
	private void addNode(Coordinate c, boolean unexplored){
		int x = (int) Math.round(c.x);
		int y = (int) Math.round(c.y);
		Coordinate loc = new Coordinate(x, y);
		
		nodeMap.putIfAbsent(loc, new Node (loc, unexplored));
	}
	
	/**
	 * @param c is the coordinate to lookup
	 * @return node coordinate is inside the graph
	 */
	private Node nodeLookup(Coordinate c) {
		int x = (int) Math.round(c.x);
		int y = (int) Math.round(c.y);
		Coordinate loc = new Coordinate(x, y);
		
		return nodeMap.get(loc);
	}
	
	/**
	 * Change made: decide if a given coordinate is a useful coordinate for a node
	 * Useful nodes: Outside corner of a wall or trap, adjacent to a trap or an exit tile, also must not be a wall
	 */
	private boolean isUseful(Coordinate center) {
		
		// not useful if there's already a node there
		if (nodeLookup(center) != null) {
			return false;
		}
		
		boolean isWall = World.lookUp(center.x, center.y).getName().equals("Wall");
		boolean isUtility = World.lookUp(center.x, center.y).getName().equals("Utility");
		boolean isTrap = World.lookUp(center.x, center.y).getName().equals("Trap");
		
		// is a wall? -- not useful
		if (isWall) {
			return false;
		}
		
		// is exit? -- useful
		if (isUtility) {
			if ( ( (UtilityTile) World.lookUp(center.x, center.y)).isExit() ) {
				return true;
			}
		}
		
		// adjacent to a trap? -- useful
		if (!isTrap) {
			Coordinate c = new Coordinate(0,0); // side coordinate
			for(int i = -1; i <= 1; i++){
				for(int j = -1; j <= 1; j++){
					
					if(i == 0 && j == 0) { continue; }
					c.x = center.x + i;
					c.y = center.y + j;
					
					if (World.lookUp(center.x, center.y).getName().equals("Trap")) {
						return true;
					}
					
				}
			}
		}
		
		// is a corner of a wall? -- useful
		// variables below are true if they are not walls;
		boolean n	= !World.lookUp(center.x  , center.y+1).getName().equals("Wall");
		boolean ne	= !World.lookUp(center.x+1, center.y+1).getName().equals("Wall");
		boolean e	= !World.lookUp(center.x+1, center.y  ).getName().equals("Wall");
		boolean se	= !World.lookUp(center.x+1, center.y-1).getName().equals("Wall");
		boolean s	= !World.lookUp(center.x  , center.y-1).getName().equals("Wall");
		boolean sw	= !World.lookUp(center.x-1, center.y-1).getName().equals("Wall");
		boolean w	= !World.lookUp(center.x-1, center.y  ).getName().equals("Wall");
		boolean nw	= !World.lookUp(center.x-1, center.y+1).getName().equals("Wall");
		if (n && e && ne) {return true;}
		if (s && e && se) {return true;}
		if (s && w && sw) {return true;}
		if (n && w && nw) {return true;}
		
		return false;
	}

}
