package controller;

import java.util.HashMap;
import java.util.List;

import tiles.GrassTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MyAIController extends CarController{
	
	private Graph graph;
	public List<Node> pathList;
	private Driver currentDriver;
	private Driver previousDriver;
	private MapTile currentTile;
	private MapTile previousTile;
	private HashMap<Coordinate, MapTile> currentView;
	private HashMap<Coordinate, MapTile> previousViews;
	
	public MyAIController(Car car) {
		super(car);
		graph = new Graph();
	}

	@Override
	public void update(float delta) {
		updateCurrentTile();
		if(!previousTile.equals(currentTile)){
			updateViews();
			graph.updateGraph(new Coordinate(getPosition()), currentView, previousViews);
			pathList = graph.getPathList(currentView, previousViews);
		}
		if(previousDriver.isDone(this)){
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
	private void updateCurrentTile(){
		previousTile = currentTile;
		Coordinate currentCoordinate = new Coordinate(getPosition());
		currentTile = World.lookUp(currentCoordinate.x, currentCoordinate.y);
	}
	
	/**
	 * Update currentView and previousViews
	 */
	private void updateViews(){
		if(previousViews == null){
			previousViews = currentView;
		} else {
			previousViews.putAll(currentView);
		}
		
		currentView = getView();
	}
		
	/**
	 * 
	 * Calculate the angle of the current Node and the destination Node
	 * 
	 * @return the angle of the current Node and the destination Node
	 */
	public float getCarNodeOrientation(Node destNode){
		if(destNode == null){
			destNode = pathList.get(0);
		}
		WorldSpatial.Direction direction = getOrientation();
		Coordinate currentCoordinate = new Coordinate(getPosition());
		Coordinate destCooridinate = destNode.getCoordinate();
		float a = 1; // line stand for the car's orientation
		float b; // line stand for the line between the two nodes
		float c; // the other line of the triangle
		Coordinate orientCoordinate = null;
		switch(direction){
		case NORTH:
			orientCoordinate = new Coordinate(currentCoordinate.x, currentCoordinate.y + 1);	
		case SOUTH:
			orientCoordinate = new Coordinate(currentCoordinate.x, currentCoordinate.y - 1);
		case WEST:
			orientCoordinate = new Coordinate(currentCoordinate.x - 1, currentCoordinate.y);
		case EAST:
			orientCoordinate = new Coordinate(currentCoordinate.x + 1, currentCoordinate.y);
		}
		b = (float) Math.sqrt(Math.pow(currentCoordinate.x - destCooridinate.x, 2) + Math.pow(currentCoordinate.y - destCooridinate.y, 2));
		c = (float) Math.sqrt(Math.pow(orientCoordinate.x - destCooridinate.x, 2) + Math.pow(orientCoordinate.y - destCooridinate.y, 2));
		return (float) Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2.0 * a * b));
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
		switch(direction){
		case NORTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y + 1) instanceof GrassTrap){
				return true;
			}
			break;
		case SOUTH:
			if(World.lookUp(currentCoordinate.x, currentCoordinate.y - 1) instanceof GrassTrap){
				return true;
			}
			break;
		case WEST:
			if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y) instanceof GrassTrap){
				return true;
			}
			break;
		case EAST:
			if(World.lookUp(currentCoordinate.x + 1, currentCoordinate.y) instanceof GrassTrap){
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
