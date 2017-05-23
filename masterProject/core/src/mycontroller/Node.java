package mycontroller;

import tiles.MapTile;
import utilities.*;

public class Node {
	
	private Coordinate coordinate;
	private MapTile mapTile;
	
	public Node(Coordinate coordinate, MapTile mapTile){
		this.coordinate = coordinate;
		this.mapTile = mapTile;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}


	public MapTile getMapTile() {
		return mapTile;
	}

	

}
