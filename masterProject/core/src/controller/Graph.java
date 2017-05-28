package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import tiles.*;
import utilities.Coordinate;
import world.World;

public class Graph {
	
	// change made
	// add a final boolean attribute to avoid magic booleans
	private static final boolean EXPLORED = true;
	
	private HashMap<Coordinate, Node> nodeMap;
	private CostStrategy coster;
	
	
	public Graph() {
		nodeMap = new HashMap<Coordinate, Node>();
		coster = new DistanceCostStrategy();
	}
	
	private void printGraph() {
		for (Object n: nodeMap.entrySet().toArray()) {
			System.out.println("Printing graph: " + n.toString());
		}
	}
	
	
	/**
	 * Change made
	 * Add the current position as a parameter
	 * 
	 * Updates all nodes and edges of the graph
	 * @param currentPosition 
	 * @param currentView
	 * @param previousViews
	 */
	public void updateGraph(
			Coordinate currentPosition, 
			HashMap<Coordinate, MapTile> currentView, 
			HashMap<Coordinate, MapTile> previousViews){
		updateUnexploredNodes(currentPosition, currentView);
		updateExploredNodes(currentView);
		updateEdges(currentPosition);
	}
	
	/**
	 * Change made
	 * Change the return type from ArrayList<Node> to LinkedList<Node>
	 * Change the parameters into current position only
	 * 
	 * @param Coordinate: current position
	 * @return a linked list of nodes
	 */
	public LinkedList<Node> getPathList(Coordinate currentPos){

		Node next = generateBestDestination(currentPos);
		LinkedList<Node> path = new LinkedList<Node>();
		path.push(next);
		boolean better = false;
		
		while (!next.isStarter()) {
			
			for (Edge e : path.getFirst().getEdges()) {
				
				better = e.getPartner(path.getFirst()).getCost() < next.getCost();
				if (better) {
					next = e.getPartner(path.getFirst());
				}
			}
		}
		path.push(next);
		
		printGraph();
		return path;
	}
	
	/**
	 * Change made 
	 * Add current position as parameter
	 * 
	 * Uses a variant of dijstra's algorithm to find the best destination in the graph
	 * @param current position
	 * @return the best destination
	 */
	private Node generateBestDestination(Coordinate currentPos){
		
		LinkedList<Node> visitQueue = new LinkedList<Node>();
		ArrayList<Node> closeNodes = getNodesInRadius(4, currentPos);
		Node best = null;
		Node visitor = null;
		Node visiting = null;
		float visitCost = 0;
		
		// Add and cost nearby starter nodes
		for (Node closeNode : closeNodes) {
			if (!walledPath(currentPos, closeNode.getCoordinate())) {
				closeNode.setCost( coster.travelCost(currentPos, closeNode) );
				closeNode.setStarter(true);
				visitQueue.add(closeNode);
			}
		}
		best = visitQueue.peek(); // initialize best to be a random node to avoid null pointer exceptions
		
		// Iterate over all the nodes to visit
		while (!visitQueue.isEmpty()) {
			
			visitor = visitQueue.removeLast();
			
			for (Edge e : visitor.getEdges()) {
				
				// visit each feasible node where the cost can be lowered
				visiting = e.getPartner(visitor);
				if (feasibleVisit(visitor, visiting)) {
					visitCost = coster.travelCost(visitor, visiting);
					if ( visiting.getCost() > visitCost) {
						
						//visiting - set new cost and add new node to the queue
						visiting.setCost( visitCost );
						visiting.setStarter(false);
						visitQueue.push(visiting);
						
						// update best node
						if (visiting.isUnexplored() && best.isUnexplored()) {
							if (visiting.getCost() < best.getCost()) { 
								visiting = best;
							}
						} else if (visiting.isExitTile() && !best.isExitTile()) {
							visiting = best;
						} else if (visiting.isExitTile() && best.isExitTile()) {
							if (visiting.getCost() < best.getCost()) { 
								visiting = best;
							}
						}
					}
				}
			}
		}
		
		return best;
	}
	
	/**
	 * 
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	private boolean feasibleVisit(Node fromNode, Node toNode){
		return true;
	}
	
	/**
	 * Get the list of nodes within the given radius
	 * @param radius the radius in which to search for nodes
	 * @param pos the center position
	 * @return a list of nodes within the given radius
	 */
	private ArrayList<Node> getNodesInRadius(int radius, Coordinate pos){
		Coordinate helper = new Coordinate(pos.x,pos.y);
		Node n = null;
		ArrayList<Node> radList = new ArrayList<Node>();
		
		// search in every tile
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
		return radList;
	}
	
	/**
	 * Remove nodes which were previously unexplored but are now in the view
	 * Add viewed nodes into the graph (only useful nodes which have an unobstructed path to are added)
	 * Useful nodes: Outside corner of a wall or trap, adjacent to a trap or an exit tile
	 * @param currentView
	 */
	private void updateExploredNodes(HashMap<Coordinate, MapTile> currentView){
		
		for(Coordinate viewedCoordinate : currentView.keySet()){
			// remove nodes which were previously unexplored
			if ( nodeMap.containsKey(viewedCoordinate) ) {
				if ( nodeMap.get(viewedCoordinate).isUnexplored() ) { removeNode(viewedCoordinate); }
			}
			
			// create new useful explored nodes
			if(isUseful(viewedCoordinate)){ addNode(viewedCoordinate, EXPLORED); }
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
		i = 4;
		for(j = -3; j <= 4; j++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check NORTH
		j = 4;
		for(i = -4; i <= 3; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check WEST
		i = -4;
		for(i = -4; i <= 3; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, !EXPLORED);
		}
		
		// check SOUTH
		j = -4;
		for(i = -3; i <= 4; i++){
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
	 * @param pos The position from which to update edges
	 */
	private void updateEdges(Coordinate pos){
		
		ArrayList<Node> nodes = getNodesInRadius(4, pos);
		ArrayList<Edge> edges = null;
		boolean walled = true;
		Edge newEdge = null;
		
		// for each of the node combinations near the car
		for (int i=0; i<nodes.size(); i++) {
			for (int j=i+1; j<nodes.size(); j++) {
				
				// don't have an edge to itself
				if (nodes.get(i).equals(nodes.get(j))) { continue; }
				
				// don't add an edge that's already there
				edges = nodes.get(i).getEdges();
				for (Edge e : edges) {
					if (e.getPartner(nodes.get(i)).equals(nodes.get(j))) { continue; }
				}
				
				// don't add an inaccessible edge
				walled = walledPath( nodes.get(i).getCoordinate(), nodes.get(j).getCoordinate() );
				if (walled) { continue; }
				
				// create and add edge
				newEdge = new Edge(nodes.get(i), nodes.get(j));
				nodes.get(i).registerEdge(newEdge);
			}
		}
		
		
	}
	
	/**
	 * Used in updateEdges() draw the line
	 * @param c1 start coordinate
	 * @param c2 end coordinate
	 * @return an ArrayList of MapTile representing all the MapTile that the line has
	 */
	private LinkedList<MapTile> lineOfSight(Coordinate c1, Coordinate c2) {
		
		LinkedList<MapTile> tiles = new LinkedList<MapTile>();
		
		if (c1.equals(c2)) {
			tiles.push(World.lookUp(c1.x, c2.y));
			return tiles;
		}
		
		double angle = Math.atan2(c2.y - c1.y, c2.x - c1.x);
		double length = Math.hypot(c2.y - c1.y, c2.x - c1.x);
		double res = 0.2; 
		Coordinate point = new Coordinate(c1.x, c1.y);
		
		MapTile previousTile = World.lookUp(point.x, point.y);
		MapTile currentTile = null;
		
		double xchange =0;
		double ychange =0;
		
		for (double line = 0; line < length ; line = line + res) {
			xchange =res*Math.cos(angle);
			ychange =res*Math.sin(angle);
			
			previousTile = currentTile;
			currentTile = World.lookUp(point.x + xchange, point.y + ychange);
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
		LinkedList<MapTile> tiles = lineOfSight(c1, c2);
		
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
	 * @param center The coordinate of the tile which might be useful
	 */
	private boolean isUseful(Coordinate center) {
		
		
		// not useful if there's already a node there
		if (nodeLookup(center) != null) {
			return false;
		}
		
		MapTile tile = World.lookUp(center.x, center.y);
		
		// is a wall? -- not useful
		boolean isWall = tile.getName().equals("Wall");
		if (isWall) {
			return false;
		}
		
		// is exit? -- useful
		boolean isUtility = tile.getName().equals("Utility");
		if (isUtility) {
			if ( ( (UtilityTile) tile).isExit() ) {
				return true;
			}
		}
		
		// adjacent to a trap? -- useful
		boolean isTrap = tile.getName().equals("Trap");
		if (!isTrap) {
			Coordinate c = new Coordinate(0,0);
			for(int i = -1; i <= 1; i++){
				for(int j = -1; j <= 1; j++){
					
					if(i == 0 && j == 0) { continue; }
					c.x = center.x + i;
					c.y = center.y + j;
					
					if (World.lookUp(c.x, c.y).getName().equals("Trap")) {
						return true;
					}
					
				}
			}
		}
		
		// is a corner of a wall? -- useful
		// variables below are true if they are not walls;
		boolean n  = !World.lookUp(center.x  , center.y+1).getName().equals("Wall");
		boolean ne = !World.lookUp(center.x+1, center.y+1).getName().equals("Wall");
		boolean e  = !World.lookUp(center.x+1, center.y  ).getName().equals("Wall");
		boolean se = !World.lookUp(center.x+1, center.y-1).getName().equals("Wall");
		boolean s  = !World.lookUp(center.x  , center.y-1).getName().equals("Wall");
		boolean sw = !World.lookUp(center.x-1, center.y-1).getName().equals("Wall");
		boolean w  = !World.lookUp(center.x-1, center.y  ).getName().equals("Wall");
		boolean nw = !World.lookUp(center.x-1, center.y+1).getName().equals("Wall");
		if (n && e && ne) {return true;}
		if (s && e && se) {return true;}
		if (s && w && sw) {return true;}
		if (n && w && nw) {return true;}
		
		return false;
	}

}
