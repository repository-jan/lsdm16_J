package problem6_algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import graph.Edge;
import graph.Node;
import graph.Graph;
import programming.StopWatch;

/** Using the Girvan's and Newman's method to detect communities. */
public class P6Algorithms {
	
	// For console outputs.
	private static boolean out = true;
		
	/** Performs the method of Girvan and Newman to detect communities. 
	 * The mehtod is split into the following parts:
	 * 	calculate all edges betweenness
	 * 	delete the edge(s) with the highest value
	 * 	find connected components
	 * 	chek whether the graph is split enough by calculating modularity.
	 * 
	 * In addition, to maybe speed up the process, the edge betweenness is initially calculated for every edge,
	 * but afterwards only for those edges, affected by the removal of the highes edge(s).
	 * After removing the edge(s) the findAffectedGroups-method creats a subgraph of all that is reachable from the
	 * two end of the removed edge. For split up graphs it should reduce computing.
	 */
	public static List<List<Node>> girvanNewmanMethod(Graph graph, double qThreshold) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		
		int iteration = 0;
		List<List<Node>> groups = null;
		List<Edge> deletedEdges = null;
		Graph subGraph = null;
		double q = 0.0;
		
		do{
			iteration++;
			/**/if(out) System.out.println(" iteration: " + iteration);
			
			subGraph = findAffectedGroups(graph, groups, deletedEdges);
			
			/**/if(out) System.out.println("  nodecounter: " + subGraph.getNodes().size() + " / n=" + graph.getNodes().size());

			calculateEdgeBetweenness(subGraph);
			
			deletedEdges = removeMaxValueEdges(graph);
			
			groups = findGroups(graph);
			/**/if(out) System.out.println("   found " + groups.size() + " group(s)");
			
			q = calculateQ(groups, graph.getEdges().size());
				
			/**/if(out) System.out.println("  q=" + q + " >= qT=" + qThreshold + " ?");
			/**/if(out) System.out.println("  " + stopWatch.stopTime());
			
		}while( q < qThreshold );
		
		/**/if(out) System.out.println("   yes -> exiting loop...");
		System.out.println(" finished after " + iteration + " iteration(s).");
		
		return groups;
	}
	
	/** Calculates the betweenness of all edges in the given graph. */
	private static void calculateEdgeBetweenness(Graph graph) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		double n = graph.getNodes().size();
		int nn = (int)(Math.ceil((n/100.0)*20.0)); //every 20%
		int i = 1;

		
		// Clear edges:
		for( Edge e : graph.getEdges() ) {
			e.setBetweenness(0.0);
		}
		
		List<Node> nodes = graph.getNodes();
		// For every node as root-node v:
		for( Node v : nodes ) {
			
			if( out && (nn != 0 && (i % nn  == 0 || i == (int)n)) ) {
				System.out.println("   " + i + " / " + (int)n + " nodes done, " + (((int)((i/n)*10000))/100.0) + "% (" + stopWatch.stopTime() + ")");
			}
			
			// Init nodes:
			for( Node u : nodes ) {
				u.setLevel(-1);
				u.setValue(0);
			}
			// Clear edges:
			for( Edge e : graph.getEdges() ) {
				e.setFlow(0.0);
			}
			
			// Starting from the root going down the graph using BFS, assigning levels and #SPs to nodes:
//			Queue<Node> q = new LinkedHashSet()<>;
			UniqueQueue<Node> q = new UniqueQueue<>();
			v.setLevel(0);
			v.setValue(1);
			q.add(v);

			while( ! q.isEmpty() ) {
				Node u = q.poll();
				for( Edge e : u.getEdges() ) {
					Node opposite = e.getOpposite(u);
					if( opposite.getLevel() == -1 ) {
						opposite.setLevel(u.getLevel()+1);
					}
					if( opposite.getLevel() == (u.getLevel()+1) ) {
						opposite.setValue( opposite.getValue() + u.getValue() );
						if( ! q.contains(opposite) ) {
							q.add(opposite);
						}
					}
				}
			}
			
			// Going back up, assigning flows to edges:
			List<Node> nds = new ArrayList<>();
			nds.addAll(nodes);
			
			List<Node> sortedNodes = nds;
			sortedNodes.sort((o1, o2) -> o2.getLevel().compareTo(o1.getLevel()));
//			sortedNodes.sort(Comparator.comparing(Node::getLevel));
			for( Node u : sortedNodes ) {
				double nSPs = 0;
				double inFlow = 1.0;
				for( Edge e : u.getEdges() ) {
					Node opposite = e.getOpposite(u);
					if( opposite.getLevel() == (u.getLevel()-1) ) {
						nSPs = nSPs + opposite.getValue();
					}
					if( opposite.getLevel() == (u.getLevel()+1) ) {
						inFlow = inFlow + e.getFlow();
					}
				}

				for( Edge e : u.getEdges() ) {
					Node opposite = e.getOpposite(u);
					if( opposite.getLevel() == (u.getLevel()-1) ) {
						e.setFlow( inFlow  * ( opposite.getValue() / nSPs) );
					}
				}
			}
			for( Edge e : graph.getEdges() ) {
				e.setBetweenness( e.getBetweenness() + (e.getFlow() / 2.0) );

			}
			i++;
		}
	}
	
	/** Removes the edge(s) with the highst betweenness value. */
	public static List<Edge> removeMaxValueEdges(Graph graph) {
		int mBefore = graph.getEdges().size();
		List<Edge> maxEdges = new ArrayList<>();
		List<Edge> edges = graph.getEdges();
		maxEdges.add(edges.get(0));
		for( int i = 1; i < edges.size(); i++ ) {
			if( edges.get(i).getBetweenness() > maxEdges.get(0).getBetweenness() ) {
				maxEdges = new ArrayList<>();
				maxEdges.add(edges.get(i));
			}else if( edges.get(i).getBetweenness() == maxEdges.get(0).getBetweenness() ) {
				maxEdges.add(edges.get(i));
			}
		}
		for( Edge e : maxEdges ) {
			graph.removeEdge(e);
		}
		/**/if(out) System.out.println("   removed " + maxEdges.size() + " edge(s) (m=" + mBefore + " -> m=" + graph.getEdges().size() + ") " + maxEdges);
		
		return maxEdges;
	}
	
	/** Finds all connected components using BFS. */
	public static List<List<Node>> findGroups(Graph graph) {
		List<List<Node>> groups = new ArrayList<>();
		List<Node> group = null;
		UniqueQueue<Node> q = null;
		Set<Node> done = new HashSet<>();
		for( Node n : graph.getNodes() ) {
			if( done.contains(n) ) {
				continue;
			}
			group = new ArrayList<>();
			q = new UniqueQueue<>();
			q.add(n);
			while( ! q.isEmpty() ) {
				Node v = q.poll();
				group.add(v);
				done.add(v);
				for( Edge e : v.getEdges() ) {
					Node opposite = e.getOpposite(v);
					if( ! q.contains(opposite) && ! done.contains(opposite)) {
						q.add(opposite);
					}
				}
			}
			groups.add(group);
		}	
		return groups;
	}
	
	/** Calculates the modularity of the graph given as a list of components. */
	public static double calculateQ(List<List<Node>> groups, int m) {
		double q = 0.0;
		for( List<Node> group : groups ) {
			for( Node i : group ) {
				List<Edge> edges = i.getEdges();
				for( Node j : group ) {
					int aij = 0;
					for( Edge e : edges ) {
						if( e.getOpposite(i).getId() == j.getId() ) {
							aij = 1;
							break;
						}
					}
					double kikj2m = (i.getEdges().size() * j.getEdges().size() ) / (2.0*m);
					q = q + (aij - kikj2m);
				}
			}
		}
		q = q / (2.0*m);
		return q;
	}
	
	/** Creates a subgraph containing what was connected before removing the edge(s). */
	private static Graph findAffectedGroups(Graph graph, List<List<Node>> groups, List<Edge> deletedEdges) {
		if( deletedEdges == null ) {
			return graph;
		}
		Graph subGraph = new Graph();
		for( Edge edge : deletedEdges ) {
			for( List<Node> group : groups ) {
				if( (group.contains(edge.getFrom())) || (group.contains(edge.getTo())) ) {
					for( Node v : group ) {
						subGraph.addNode(v);
					}
				}
			}
		}
		for( Edge edge : graph.getEdges() ) {
			if( subGraph.getNodes().contains(edge.getFrom()) && subGraph.getNodes().contains(edge.getTo()) ) {
				subGraph.addEdge(edge);
			}
		}
		return subGraph;
	}
	
	/**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**/
	/** Method to print the groups/communities to console. */
	public static void printGroups(List<List<Node>> groups) {
		System.out.println("# The group(s)/communitie(s) is(are):");
		for( int i = 0; i < groups.size(); i++ ) {
			String group = "";
			for( Node v : groups.get(i) ) {
				group = group + v.getName() + " ";
			}
			System.out.println("#  C_" + (i+1) + ": " + group);
		}
	}
}