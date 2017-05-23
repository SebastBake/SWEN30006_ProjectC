package mycontroller;

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
	
	public void newDriver(Driver newDriver){
		previousDriver = currentDriver;
		currentDriver = newDriver;
	}
	
	private void updateCurrentTile(){
		previousTile = currentTile;
		Coordinate currentCoordinate = new Coordinate(getPosition());
		currentTile = World.lookUp(currentCoordinate.x, currentCoordinate.y);
	}
	
	
	private void updateCurrentView(){
		if(previousViews == null){
			previousViews = currentView;
		} else if(currentView != null){
			previousViews.putAll(currentView);
		}
		currentView = getView();
	}
	
	public float getCarNodeOrientation(){
		Node destNode = pathList.get(pathList.size() - 1);
		WorldSpatial.Direction direction = getOrientation();
		
		
	}
	
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
	
	public float getMaxSideSpace(){
		WorldSpatial.Direction direction = getOrientation();
		switch(direction){
		case NORTH:
			
		}
	}
	
	
}
