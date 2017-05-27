package controller;

import java.util.ArrayList;
import java.util.HashMap;

import controller.CarController;
import tiles.*;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MyAIController extends CarController{
	
	private Graph graph;
	private ArrayList<Node> pathList;
	private Driver currentDriver;
	private Driver previousDriver;
	private MapTile currentTile;
	private MapTile previousTile;
	private HashMap<Coordinate, MapTile> currentView;
	private HashMap<Coordinate, MapTile> previousViews;
	
	public MyAIController(Car car) {
		super(car);
	}

	@Override
	public void update(float delta) {
		
		
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
	private void updateCurrentView(){
		if(previousViews == null){
			previousViews = currentView;
		} else if(currentView != null){
			previousViews.putAll(currentView);
		}
		currentView = getView();
	}
		
	/**
	 * Change made
	 * 
	 * Originally, it was public float getCarNodeOrientation()
	 * Originally, it was designed to get the angle of the car and the destination node
	 * Since it's designed only to know if the destination is behind the car or not
	 * We simply change it to an easier approach
	 * 
	 * Decide whether the destination is behind the car or not
	 * 
	 * @return if the destination is behind the car or not
	 */
	public boolean isDestinationBehind(){
		Node destNode = pathList.get(pathList.size() - 1);
		WorldSpatial.Direction direction = getOrientation();
		Coordinate currentCoordinate = new Coordinate(getPosition());
		Coordinate destCooridinate = destNode.getCoordinate();
		switch(direction){
		case NORTH:
			if(currentCoordinate.y > destCooridinate.y){
				return true;
			}
			break;
		case SOUTH:
			if(currentCoordinate.y < destCooridinate.y){
				return true;
			}
			break;
		case EAST:
			if(currentCoordinate.x < destCooridinate.x){
				return true;
			}
			break;
		case WEST:
			if(currentCoordinate.x > destCooridinate.x){
				return true;
			}
			break;
		}
		return false;	
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
		case EAST:
			if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y).getName().equals("Wall")){
				return true;
			}
			break;
		case WEST:
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
		case EAST:
			if(World.lookUp(currentCoordinate.x - 1, currentCoordinate.y) instanceof GrassTrap){
				return true;
			}
			break;
		case WEST:
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
		case EAST:
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
		case WEST:
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
		return leftDist + rightDist;
	}
	
	
}
