package controller;

import java.util.HashMap;
import java.util.LinkedList;

import tiles.GrassTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MyAIController extends CarController{
	
	private Graph graph;
	public LinkedList<Node> pathList = new LinkedList<Node>();
	private Driver currentDriver;
	private Driver previousDriver;
	private float carAngle;
	public Coordinate currentLoc;
	private Coordinate previousLoc;
	private HashMap<Coordinate, MapTile> currentView;
	private HashMap<Coordinate, MapTile> previousViews;
	
	public MyAIController(Car car) {
		super(car);
		
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
		
		System.out.println("nextNode: " + pathList.getFirst().getCoordinate().toString());
	}

	@Override
	public void update(float delta) {
		updateLocation();
		updateAngle();
		
		if(!previousLoc.equals(currentLoc)){
			updateViews();
			graph.updateGraph(new Coordinate(getPosition()), currentView, previousViews);
			pathList = graph.getPathList(new Coordinate(getPosition()), carAngle);
			System.out.println("nextNode: " + pathList.getFirst().getCoordinate().toString());
			
		}
		
		if(getPosition().equals(pathList.getFirst().getCoordinate().toString())){
			pathList.remove(0);
		}
		
		if(currentDriver.isDone(this)){
			previousDriver.changeBehavior(this);
		}
		
		currentDriver.behave(this, delta);
		
	}

	/**
	 * Update Driver
	 * @param newDriver
	 */
	public void newDriver(Driver newDriver){
		
		previousDriver = currentDriver;
		currentDriver = newDriver;
	}
	
	/**
	 * Update currentTile
	 */
	private void updateLocation(){
		previousLoc = currentLoc;
		currentLoc = new Coordinate(getPosition());
	}
	
	/**
	 * Update currentTile
	 */
	private void updateAngle(){
		carAngle = (float) Math.toDegrees(Math.atan2(getRawVelocity().y, getRawVelocity().x));
	}
	
	/**
	 * Update currentView and previousViews
	 */
	private void updateViews(){
		
		previousViews.putAll(currentView);
		currentView = getView();
	}
		
	/**
	 * 
	 * Calculate the angle of the current Node and the destination Node
	 * 
	 * @return the angle of the current Node and the destination Node
	 */
	public float getCarNodeOrientation(Node toNode){
		if(toNode == null){
			toNode = pathList.getFirst();
		
		}
		Coordinate currentCoordinate = new Coordinate(getPosition());
		Coordinate toCoordinate = toNode.getCoordinate();
		float angleBetween = 0;
		float carToWest = getAngle();
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
		angleBetween = carToWest - toNodeAngle;
		if(Math.abs(carToWest - toNodeAngle) > 180){
			if(carToWest - toNodeAngle < 0){
				return 360 + angleBetween;
			}else{
				return angleBetween - 360;
			}
		}
		System.out.println(toNode.getCoordinate().toString() + " : " + angleBetween);
		return angleBetween;
		// Aidan's solution
//		double nodeAngle = Math.atan2(toNode.getCoordinate().x, toNode.getCoordinate().y);
//		double magnitudeNode = Math.sqrt(Math.pow(toNode.getCoordinate().x, 2) + Math.pow(toNode.getCoordinate().y, 2));
//		return (float)Math.acos((toNode.getCoordinate().x - currentLoc.x) / (magnitudeNode * Math.sin(nodeAngle)));
		
	}
	
	/**
	 *
	 * @return if the car has hit the wall or not
	 */
	public boolean detectCollision(){
		WorldSpatial.Direction direction = getOrientation();
		Coordinate currentCoordinate = new Coordinate(getPosition());
		switch(direction){
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
		default:
		break;
		}
		return false;
	}
	
	/**
	 * 
	 * @return if the car is at an edge of a grass trap
	 */
	public boolean detectGrassEdge(){
		WorldSpatial.Direction direction = getOrientation();
		Coordinate currentCoordinate = new Coordinate(getPosition());
		//switch(direction){
		//case NORTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y + 1) instanceof GrassTrap){
				return true;
			}
		//	break;
		//case SOUTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y - 1) instanceof GrassTrap){
				return true;
			}
		//	break;
		//case WEST:
			if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y) instanceof GrassTrap){
				return true;
			}
		//	break;
		//case EAST:
			if(World.lookUp(currentCoordinate.x + 1, currentCoordinate.y) instanceof GrassTrap){
				return true;
			}
		//	break;
		//default:
		//	break;
		//}
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
