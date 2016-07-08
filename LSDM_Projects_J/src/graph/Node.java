package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private Integer _id;
	private String _name;
	private List<Edge> _edges = new ArrayList<>();
	/** Level / dept / distance. */
	private Integer _level = -1;
	/** Number of shortest paths / Number of triangles */
	private int _value = 0;
	
	
	public Node(int id, String name) {
		_id = id;
		_name = name;
	}
	
	public void addEdge(Edge edge) {
		if( ! _edges.contains(edge) ) {
			_edges.add(edge);
		}
	}
	
	public List<Node> getOpposites() {
		List<Node> opposites = new ArrayList<>();
		for( Edge edge : _edges ) {
			opposites.add(edge.getOpposite(this));
		}
		return opposites;
	}

	@Override
	public String toString() {
		return ("" + _name);
//		return ("" + _name + "(" + _id + ")");
	}
	
	@Override
	public int hashCode() {
		return _id;
	}
	
	public void setId(int id) { _id = id; }
	public Integer getId() { return _id; }
	public void setName(String name) { _name = name; }
	public String getName() { return _name; }
	public void setEdges(List<Edge> edges) { _edges = edges; }
	public List<Edge> getEdges() { return _edges; }
	public void setLevel(int level) { _level = level; }
	public Integer getLevel() { return _level; }
	public void setValue(int value) { _value = value; }
	public int getValue() { return _value; }
}