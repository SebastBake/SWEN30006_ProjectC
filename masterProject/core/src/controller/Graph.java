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
	
	public void updateGraph(
			Coordinate currentPosition, 
			HashMap<Coordinate, MapTile> currentView, 
			HashMap<Coordinate, MapTile> previousViews){
		updateUnexploredNodes(currentPosition, currentView);
		updateExploredNodes(currentView, previousViews);
		updateEdges();
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
	
	/**
	 * Returns a list of nodes within the given radius
	 * @param radius the radius in which to search for nodes
	 * @param pos the center position
	 * @return
	 */
	private ArrayList<Node> getNodesInRadius(int radius, Coordinate pos){
		Coordinate helper = new Coordinate(pos.x,pos.y);
		Node n = null;
		ArrayList<Node> radList = new ArrayList<Node>();
		
		for(int i = -radius;i<=radius; i++) {
			for(int j = -radius;j<=radius; j++) {
				helper.x = pos.x+i;
				helper.y = pos.y+j;
				
				n = nodeLookup(helper);
				if (n!=null) {
					radList.add(n);
				}
			}
		}
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
	 * Change : Don't need currentView
	 * @param currentPositon 
	 * @param currentView
	 * @param previousViews
	 */
	private void updateUnexploredNodes(Coordinate currentPos, HashMap<Coordinate, MapTile> previousViews){
		
		int j=0; // relative vertical distance from car
		int i=0; // relative horizontal distance from car
		Coordinate helper = new Coordinate(currentPos.x, currentPos.y);
		
		// check EAST
		i = 3;
		for(j = -2; j <= 3; j++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check NORTH
		j = 3;
		for(i = -3; i <= 2; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check WEST
		i = -3;
		for(i = -3; i <= 2; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check SOUTH
		j = -3;
		for(i = -2; i <= 3; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}

	}
	
	
	
	/**
	 * Draws edges between every pair of nodes in the view which have a clear path between them (use lineOfSight()).
	 * Don't draw edges already in the graph.
	 */
	private void updateEdges(){
	}
	
	/**
	 * Used in updateEdges() draw the line
	 * @param c1 start coordinate
	 * @param c2 end coordinate
	 * @return an ArrayList of MapTile representing all the MapTile that the line has
	 */
	private ArrayList<MapTile> lineOfSight(Coordinate c1, Coordinate c2) {
		double gradient = (c2.y - c1.y)/(c2.x - c1.x);
		double length = Math.hypot(c2.y - c1.y, c2.x - c1.x);
		double res = 0.1; 
		Coordinate point = new Coordinate(c1.x, c1.y);
		
		MapTile previousTile = World.lookUp(point.x, point.y);
		MapTile currentTile = null;
		
		double xchange =0;
		double ychange =0;
		
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		
		for (double line = 0; line < length ; line =+ res) {
			xchange =Math.sqrt( Math.pow(res,2) / ( 1 + Math.pow(gradient,2)) );
			ychange =xchange*gradient;
			point.x =(int) Math.round(point.x + xchange);
			point.y =(int) Math.round(point.y + ychange);
			
			previousTile = currentTile;
			currentTile = World.lookUp(point.x, point.y);
			if (currentTile!=previousTile) {
				tiles.add(currentTile);
			}
		}
		
		return tiles;
	}
	
	/** This is a helper method which wasn't in the original design
	 * Returns true if there is a wall between two 
	 * @param c1
	 * @param c2
	 * @return an ArrayList of MapTile representing all the MapTile that the line has
	 */
	private boolean walledPath(Coordinate c1, Coordinate c2) {
		ArrayList<MapTile> tiles = lineOfSight(c1, c2);
		
		for(MapTile t : tiles) {
			if (t.getName().equals("Wall")) {
				return true;
			}
		}
		
		return false;
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
