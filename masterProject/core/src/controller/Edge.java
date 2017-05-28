package controller;

public class Edge {
	
	// change made 
	// remove the attribute ArrayList<MapTile> tiles
	
	private float angle;
	private float length;
	private Node node1;
	private Node node2;
	
	public Edge(Node node1, Node node2){
		this.node1 = node1;
		this.node2 = node2;
		this.length = (float)Math.hypot(node1.getCoordinate().x - node2.getCoordinate().x, 
										node1.getCoordinate().y - node2.getCoordinate().y);
		this.angle = (float)Math.atan((float)(node1.getCoordinate().y - node2.getCoordinate().y)/
											(node1.getCoordinate().x - node2.getCoordinate().x));
	}
	
	/**
	 * Get the other partner node of the edge
	 * If the argument is not one of the edge's nodes, return null
	 * @param node
	 * @return the other node
	 */
	public Node getPartner(Node node){
		if(node.getCoordinate().equals(node1.getCoordinate())){
			return node2;
		}
		if(node.getCoordinate().equals(node2.getCoordinate())){
			return node1;
		}
		return null;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public float getLength(){
		return length;
	}

}
