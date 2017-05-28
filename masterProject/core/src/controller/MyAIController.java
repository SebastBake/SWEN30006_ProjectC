package controller;

import java.util.HashMap;
import java.util.LinkedList;

import tiles.GrassTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

/**
 * We tried to make it work
 * But this implementation doesn't really make the car escape the maze successfully
 *
 */
public class MyAIController extends CarController{
	// graph object to store nodes in the maze
	private Graph graph;
	
	// the path list that the car should be on sequentially
	public LinkedList<Node> pathList = new LinkedList<Node>();
	
	// keep track of the driver
	private Driver currentDriver;
	private Driver previousDriver;
	
	// keep track of the angle of the car
	private float carAngle;
	
	// keep track of the location of the car
	public Coordinate currentLoc;
	private Coordinate previousLoc;

	private HashMap<Coordinate, MapTile> currentView;
	
	// keep track of the view, hence the whole visited maze
	private HashMap<Coordinate, MapTile> previousViews;
	
	public MyAIController(Car car) {
		super(car);
		
		/* initialize the controller */
		updateAngle();
		currentLoc = new Coordinate(getPosition());
		previousLoc = currentLoc;
		
		currentView = getView();
		previousViews = new HashMap<Coordinate, MapTile>();
		previousViews.putAll(currentView);
		
		graph = new Graph();
		graph.updateGraph(new Coordinate(getPosition()), currentView, previousViews);
		pathList = graph.getPathList(new Coordinate(getPosition()), carAngle);
		
		currentDriver = new FrontAlign_Forward();
		previousDriver = currentDriver;
		//Troubleshooting print
		//System.out.println("nextNode: " + pathList.getFirst().getCoordinate().toString());
	}

	@Override
	public void update(float delta) {
		updateLocation();
		updateAngle();
		
		//When you move to a new square, update the graph
		if(!previousLoc.equals(currentLoc)){
			updateViews();
			graph.updateGraph(new Coordinate(getPosition()), currentView, previousViews);
			pathList = graph.getPathList(new Coordinate(getPosition()), carAngle);
			//Troubleshooting print
			//System.out.println("nextNode: " + pathList.getFirst().getCoordinate().toString());
			
		}
		
		if(getPosition().equals(pathList.getFirst().getCoordinate().toString())){
			// Remove a node once it is reached
			pathList.remove(0);
		}
		
		if(currentDriver.isDone(this)){
			// Change out the driver once it has finished its manoevre
			previousDriver.changeBehavior(this);
		}
		// Make the car go
		currentDriver.behave(this, delta);
		
	}

	/**
	 * Update Driver - Helper
	 * @param newDriver
	 */
	public void newDriver(Driver newDriver){
		
		previousDriver = currentDriver;
		currentDriver = newDriver;
	}
	
	/**
	 * Update currentTile - Helper
	 */
	private void updateLocation(){
		previousLoc = currentLoc;
		currentLoc = new Coordinate(getPosition());
	}
	
	/**
	 * Update currentTile - Helper
	 */
	private void updateAngle(){
		carAngle = (float) Math.toDegrees(Math.atan2(getRawVelocity().y, getRawVelocity().x));
	}
	
	/**
	 * Update currentView and previousViews - Helper
	 */
	private void updateViews(){
		
		previousViews.putAll(currentView);
		currentView = getView();
	}
		
	/**
	 * 
	 * Calculate the angle of the current Node and the destination Node
	 *  - Helper
	 * @return the angle of the current Node and the destination Node
	 */
	public float getCarNodeOrientation(Node toNode){
		if(toNode == null){
			toNode = pathList.getFirst();
		
		}
		Coordinate currentCoordinate = new Coordinate(getPosition());
		Coordinate toCoordinate = toNode.getCoordinate();
		// store the angle between the car's heading direction and the destination node
		float angleBetween = 0;
		// store the angle of the car's heading direction
		float carToWest = getAngle();
		
		// get the destination node's angle by taking West as the base direction (counter clock-wise)
		float toNodeAngle = (float) Math.toDegrees(Math.atan((float)(toCoordinate.y - currentCoordinate.y)/(float)(toCoordinate.x - currentCoordinate.x)));
		if(toCoordinate.y > currentCoordinate.y){
			if(toNodeAngle < 0){
				toNodeAngle = 180 + toNodeAngle;
			}
		}else if(toCoordinate.y < currentCoordinate.y){
			if(toNodeAngle > 0){
				toNodeAngle = 180 + toNodeAngle;
			} else {
				toNodeAngle = 360 + toNodeAngle;
			} 
		}else{
			if(toCoordinate.x > currentCoordinate.x){
				toNodeAngle = 0;
			}else{
				toNodeAngle = 180;
			}
		}
		if(toNodeAngle < 0){
			toNodeAngle = 360 + toNodeAngle;
			
		}
		
		// calculate the angle between these two counterparts
		// -90 - 90 stands for in front of the car
		// abs > 90 stands for behind the car
		// positive stands for right
		// negative stands for left
		angleBetween = carToWest - toNodeAngle;
		if(Math.abs(carToWest - toNodeAngle) > 180){
			if(carToWest - toNodeAngle < 0){
				return 360 + angleBetween;
			}else{
				return angleBetween - 360;
			}
		}
		//Debug
		//System.out.println(toNode.getCoordinate().toString() + " : " + angleBetween);
		return angleBetween;
		
	}
	
	/**
	 *
	 * @return if the car has hit the wall or not
	 */
	public boolean detectCollision(){
		Coordinate currentCoordinate = new Coordinate(getPosition());
		WorldSpatial.Direction orientation = getOrientation();
		switch(orientation){
		case NORTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y + 1).getName().equals("Wall")){
				return true;
			}
			break;
		case SOUTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y - 1).getName().equals("Wall")){
				return true;
			}
			break;
		case WEST:
			if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y).getName().equals("Wall")){
				return true;
			}
			break;
		case EAST:
			if(World.lookUp(currentCoordinate.x + 1, currentCoordinate.y).getName().equals("Wall")){
				return true;
			}
			break;
		}
		return false;
	}
	
	/**
	 * 
	 * @return if the car is at an edge of a grass trap
	 */
	public boolean detectGrassEdge(){
		Coordinate currentCoordinate = new Coordinate(getPosition());
		if(World.lookUp(currentCoordinate.x, currentCoordinate.y + 1) instanceof GrassTrap){
			return true;
		}
		if(World.lookUp(currentCoordinate.x, currentCoordinate.y - 1) instanceof GrassTrap){
			return true;
		}
		if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y) instanceof GrassTrap){
			return true;
		}
		if(World.lookUp(currentCoordinate.x + 1, currentCoordinate.y) instanceof GrassTrap){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return the space for the car to make a turn
	 */
	public float getMaxSideSpace(){
		WorldSpatial.Direction direction = getOrientation();
		Coordinate currentCoordinate = new Coordinate(getPosition());
		float leftDist = 0;
		float rightDist = 0;
		switch(direction){
		case NORTH:
			for(int i = 0; i < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x - i, currentCoordinate.y));
				if(mapTile.getName().equals("Wall")){
					leftDist = i;
				}
			}
			for(int i = 0; i  < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x + i, currentCoordinate.y));
				if(mapTile.getName().equals("Wall")){
					rightDist = i;
				}
			}
			break;
		case SOUTH:
			for(int i = 0; i < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x - i, currentCoordinate.y));
				if(mapTile.getName().equals("Wall")){
					rightDist = i;
				}
			}
			for(int i = 0; i  < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x + i, currentCoordinate.y));
				if(mapTile.getName().equals("Wall")){
					leftDist = i;
				}
			}
			break;
		case WEST:
			for(int i = 0; i < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x, currentCoordinate.y - i));
				if(mapTile.getName().equals("Wall")){
					leftDist = i;
				}
			}
			for(int i = 0; i  < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x, currentCoordinate.y + i));
				if(mapTile.getName().equals("Wall")){
					rightDist = i;
				}
			}
			break;
		case EAST:
			for(int i = 0; i < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x, currentCoordinate.y - i));
				if(mapTile.getName().equals("Wall")){
					rightDist = i;
				}
			}
			for(int i = 0; i  < getViewSquare(); i++){
				MapTile mapTile = currentView.get(new Coordinate(currentCoordinate.x, currentCoordinate.y + i));
				if(mapTile.getName().equals("Wall")){
					leftDist = i;
				}
			}
			break;	
		}
		return leftDist > rightDist ? leftDist : rightDist;
	}
	
	// Helper function for proximity
	public double distanceTo(Node node){
		Coordinate carloc = new Coordinate(getPosition());
		Coordinate nodecoords = node.getCoordinate();
		float x_dist = Math.max(carloc.x, nodecoords.x) - Math.min(carloc.x, nodecoords.x);
		float y_dist = Math.max(carloc.y, nodecoords.y) - Math.min(carloc.y, nodecoords.y);
		return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
	}
	
}
