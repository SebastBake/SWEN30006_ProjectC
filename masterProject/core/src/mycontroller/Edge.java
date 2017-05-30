package mycontroller;

/**
 * Represents Edges in the graph
 */
public class Edge {

	private Node node1;
	private Node node2;
	
	public Edge(Node node1, Node node2){
		this.node1 = node1;
		this.node2 = node2;
	}
	
	/**
	 * Get the other node of the edge
	 * If the argument is not one of the edge's nodes, return null
	 * @param node is the node not returned
	 * @return the other node which isn't the one which is the parameter of the method
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

}
