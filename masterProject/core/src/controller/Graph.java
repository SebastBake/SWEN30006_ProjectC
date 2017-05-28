package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import tiles.*;
import utilities.Coordinate;
import world.World;

/**
 * responsible for the graph and path finding
 */
public class Graph {
	
	private static final boolean UNEXPLORED = true;
	private HashMap<Coordinate, Node> nodeMap;	// Contains all of the nodes in the graph
	private CostStrategy coster;				// Used in finding the shortest path
	
	public Graph() {
		nodeMap = new HashMap<Coordinate, Node>();
		coster = new TrapDistanceCostStrategy();
	}
	
	/**
	 * helpful for debug
	 */
	private void printGraph() {
		System.out.println("Printing graph:");
		for (Object n: nodeMap.entrySet().toArray()) {
			System.out.println(n.toString());
		}
	}
	
	
	/**
	 * Updates all nodes and edges of the graph
	 * @param currentPosition current position of the car
	 * @param currentView current view of the car
	 * @param previousViews
	 */
	public void updateGraph(
			Coordinate currentPosition, 
			HashMap<Coordinate, MapTile> currentView, 
			HashMap<Coordinate, MapTile> previousViews){
		
		updateUnexploredNodes(currentPosition, previousViews);
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
	public LinkedList<Node> getPathList(Coordinate currentPos, float carAngle){
		
		LinkedList<Node> path = new LinkedList<Node>();

		// get end point using Dijstra
		Node next = generateBestDestination(currentPos, carAngle);
		path.push(next);
		
		// trace the end point back to the starting point for the path and place the nodes into the list
		while (!next.isStarter()) {
			next = next.getPreviousNode();
			path.push(next);
		}
		
		// printGraph();
		
		return path;
	}
	
	/**
	 * Uses a variant of Dijstra's algorithm to find the best destination in the graph
	 * @param currentPos position of the car
	 * @return the best end destination for the car
	 */
	private Node generateBestDestination(Coordinate currentPos, float carAngle){
		
		ArrayList<Node> closeNodes = getNodesInRadius(4, currentPos); // potential starting points
		LinkedList<Node> visitQueue = new LinkedList<Node>(); // queue used for generating the best destination
		
		Node best = null;
		Node visitor = null;
		Node visiting = null;
		float visitCost = 0;
		
		// reset the graph so that all nodes have 
		for (Object key: nodeMap.keySet())  {
			nodeMap.get(key).setStarter(false);
			nodeMap.get(key).setCost(Float.MAX_VALUE);
		}
		
		// Add and cost nearby starter nodes
		for (Node closeNode : closeNodes) {
			if (!walledPath(currentPos, closeNode.getCoordinate())) {
				closeNode.setCost( coster.travelCost(currentPos, closeNode, carAngle) );
				closeNode.setStarter(true);
				visitQueue.add(closeNode);
			}
		}
		
		// initialize best to be a random node to avoid null pointer exceptions
		best = visitQueue.peek();
		
		// Iterate over all the nodes to visit
		while (!visitQueue.isEmpty()) {
			
			visitor = visitQueue.removeLast();
			
			// visit each feasible node where the cost can be lowered
			for (Edge e : visitor.getEdges()) {
				visiting = e.getPartner(visitor);
				visitCost = coster.travelCost(visitor, visiting, carAngle);
				if (feasibleVisit(visitor, visiting) &&  visiting.getCost() > visitCost) {
						
					//visiting - set new cost and add new node to the queue
					visiting.setCost( visitCost );
					visiting.setStarter(false);
					visiting.setPreviousNode(visitor);
					visitQueue.push(visiting);
					
					// always avoid explored nodes
					if (!best.isUnexplored() && visiting.isUnexplored()){
						best = visiting;
					
					// always prioritise unexplored nodes with lowest cost
					} else if (visiting.isUnexplored() && best.isUnexplored()) {
						if (visiting.getCost() < best.getCost()) { 
							best = visiting;
						}
						
					// always prioritise exit nodes
					} else if (visiting.isExitTile() && !best.isExitTile()) {
						best = visiting;
						
						// always prioritise exit nodes with lowest cost
					} else if (visiting.isExitTile() && best.isExitTile()) {
						if (visiting.getCost() < best.getCost()) { 
							best = visiting;
						}
					}
				}
			}
		}
		
		return best;
	}
	
	/**
	 * This method was intended take into account cases where the visits may not be feasible
	 * Didn't have time to fully implement this method
	 * @param fromNode node starting at
	 * @param toNode node going to
	 * @return whether the visit is feasible
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
	 * @param currentView is the list of tiles currently within the view of the car
	 */
	private void updateExploredNodes(HashMap<Coordinate, MapTile> currentView){
		
		for(Coordinate viewedCoordinate : currentView.keySet()){
			// remove nodes which were previously unexplored
			if ( nodeMap.containsKey(viewedCoordinate) ) {
				if ( nodeLookup(viewedCoordinate).isUnexplored() ) { removeNode(viewedCoordinate); }
			}
			
			// create new useful explored nodes
			if(isUseful(viewedCoordinate)){ addNode(viewedCoordinate, !UNEXPLORED); }
		}
		
	}
	
	/**
	 * Add unexplored nodes into the graph
	 * Added nodes are just outside the view, have coordinates not recorded in the previous views, and have an unobstructed path to them
	 * Change : Don't need currentView as parameter, Add currentPosition as a parameter
	 * @param currentPositon is the current position of the car
	 * @param previousViews is the list of nodes previously seen by the car
	 */
	private void updateUnexploredNodes(Coordinate currentPos, HashMap<Coordinate, MapTile> previousViews){
		
		int j=0; // relative vertical distance from car
		int i=0; // relative horizontal distance from car
		Coordinate helper = new Coordinate(currentPos.x, currentPos.y);
		
		// This works by iterating over a border just outside the view of the car
		// Adds any nodes on the border into the graph (subject to the conditions)
		
		// check EAST
		i = 4;
		for(j = -3; j <= 4; j++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, UNEXPLORED);
		}
		
		// check NORTH
		j = 4;
		for(i = -4; i <= 3; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, UNEXPLORED);
		}
		
		// check WEST
		i = -4;
		for(i = -4; i <= 3; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, UNEXPLORED);
		}
		
		// check SOUTH
		j = -4;
		for(i = -3; i <= 4; i++){
			helper.x = currentPos.x+i;
			helper.y = currentPos.y+j;
			
			if (previousViews.containsKey(helper)) { continue; } 	// ensure node is unexplored
			if (nodeMap.containsKey(helper)) { continue; }			// ensure node is not in graph
			if (walledPath(currentPos, helper)) { continue; }		// ensure node is accessible
			addNode(helper, UNEXPLORED);
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
		
		MapTile previousTile = World.lookUp(c1.x, c1.y);
		MapTile currentTile = null;
		
		double xchange =res*Math.cos(angle);
		double ychange =res*Math.sin(angle);
		double pointx = c1.x;
		double pointy = c1.y;
		
		for (double line = 0; line < length; line = line + res) {
			
			pointx = pointx + xchange;
			pointy = pointy + ychange;
			
			previousTile = currentTile;
			currentTile = World.lookUp(pointx, pointy);
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
		boolean n  = World.lookUp(center.x  , center.y+1).getName().equals("Wall");
		boolean ne = World.lookUp(center.x+1, center.y+1).getName().equals("Wall");
		boolean e  = World.lookUp(center.x+1, center.y  ).getName().equals("Wall");
		boolean se = World.lookUp(center.x+1, center.y-1).getName().equals("Wall");
		boolean s  = World.lookUp(center.x  , center.y-1).getName().equals("Wall");
		boolean sw = World.lookUp(center.x-1, center.y-1).getName().equals("Wall");
		boolean w  = World.lookUp(center.x-1, center.y  ).getName().equals("Wall");
		boolean nw = World.lookUp(center.x-1, center.y+1).getName().equals("Wall");
		if (!n && !e && ne) {return true;}
		if (!s && !e && se) {return true;}
		if (!s && !w && sw) {return true;}
		if (!n && !w && nw) {return true;}
		
		return false;
	}

}
