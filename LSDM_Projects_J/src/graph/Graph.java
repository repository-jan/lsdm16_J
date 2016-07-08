package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	
	private List<Node> _nodes = new ArrayList<>();
	private List<Edge> _edges = new ArrayList<>();
	/** Maps the parsed name of a node to an internally given id. */
	private Map<String, Integer> _mapNodeNameToId = new HashMap<>();

	
	public Node getNode(String nodeName) {
		return _nodes.get(_mapNodeNameToId.get(nodeName));
	}
	
	public Edge getEdge(Node s, Node t) {
		Edge edge = null;
		for( Edge e : s.getEdges() ) {
			if( e.getOpposite(s).getId() == t.getId() ) {
				edge = e;
				break;
			}
		}
		return edge;
	}
	
	public boolean containsNode(String name) {
		return _mapNodeNameToId.containsKey(name);
	}
	
	/** For adding a new node while parsing. */
	public void addNode(String nodeName) {
		if( ! _mapNodeNameToId.containsKey(nodeName) ) {
			int id = _nodes.size();
			_nodes.add(new Node(id, nodeName));
			_mapNodeNameToId.put(nodeName, id);
		}		
	}
	
	/** For adding a new edge while  parsing. */
	public void addEdge(String n1, String n2) {
		if( n1 == null || n2 == null ) {
			throw new RuntimeException("adding Edge failed (edge1:" + n1 + " edge2:" + n2 + ")");
		}
		
		Node v = _nodes.get(_mapNodeNameToId.get(n1));
		Node u = _nodes.get(_mapNodeNameToId.get(n2));
		
		if( v != null && u != null) {
			
			if( v.getOpposites().contains(u) ) {
				return;
			}
			Edge e = new Edge(v, u);
			
			_edges.add(e);
			
			v.addEdge(e);
			u.addEdge(e);
		}else{
			throw new RuntimeException("you are trying to add an edge between nodes that are not contained in the graph!");
		}
	}
	
	/** For removing an edge. */
	public void removeEdge(Edge edge) {
		if( ! _edges.contains(edge) ) {
			throw new RuntimeException("you are trying to remove an not existing edge from the graph!");
		}
		_edges.remove(edge);
		edge.getTo().getEdges().remove(edge);
		edge.getFrom().getEdges().remove(edge);
	}
	
	/** For making a subgraph with existing node objects. */
	public void addNode(Node node) {
		if( ! _mapNodeNameToId.containsKey(node.getName()) ) {
			_nodes.add(node);
			_mapNodeNameToId.put(node.getName(), node.getId());
		}
	}
	
	/** For making a subgraph with existing edge objects. */
	public void addEdge(Edge edge) {
		_edges.add(edge);
	}
	
	
	public void setNodes(List<Node> nodes) { _nodes = nodes; }
	public List<Node> getNodes() { return _nodes; }
	public void setEdges(List<Edge> edges) { _edges = edges; }
	public List<Edge> getEdges() { return _edges; }
	public void setMapNodeNameToId(Map<String, Integer> mapNodeNameToId) { _mapNodeNameToId = mapNodeNameToId; }
	public Map<String, Integer> getMapNodeNameToId() { return _mapNodeNameToId; }
}