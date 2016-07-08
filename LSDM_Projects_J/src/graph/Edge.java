package graph;

public class Edge {

	private Node _from;
	private Node _to;
	private double _flow = 0.0;;
	private double _betweenness = 0.0;
	
	
	public Edge(Node from, Node to) {
		_from = from;
		_to = to;
	}
	
	/** Returns the opposite node. */
	public Node getOpposite(Node node) {
		if( node.getId() == _from.getId() ) {
			return _to;
		} else if( node.getId() == _to.getId() ) {
			return _from;
		} else {
			throw new RuntimeException("Edge.getOpposite-Error");
		}
	}
	
	@Override
	public String toString() {
		return ("" + "{" + _from + "_" + _to + "}");
	}
	
	public void setFrom(Node from) { _from = from; }
	public Node getFrom() { return _from; }
	public void setTo(Node to) { _to = to; }
	public Node getTo() { return _to; }
	public void setFlow(double flow) { _flow = flow; }
	public double getFlow() { return _flow; }
	public void setBetweenness(double betweenness) { _betweenness = betweenness; }
	public double getBetweenness() { return _betweenness; }
}