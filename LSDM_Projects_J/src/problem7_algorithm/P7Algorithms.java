package problem7_algorithm;

import java.util.ArrayList;
import java.util.List;

import graph.Edge;
import graph.Node;
import graph.Graph;
import programming.StopWatch;

/** Counting triangles in a graph. */
public class P7Algorithms {
	
	// For console outputs.
	private static boolean out = false;
	
	/** Counts the number of triangles in the given graph. */
	public static int countTriangles(Graph graph) {
		int triangles = 0;
		
		List<Node> heavyHitters = new ArrayList<>();
		List<Node> nonHeavyHitters = new ArrayList<>();
		
		partition(graph, heavyHitters, nonHeavyHitters);
		
		System.out.println("number of heavy hitters: " + heavyHitters.size());
		int heavyTriangles = countTrianglesHeavyHitters(graph, heavyHitters);
		
		System.out.println("number of non heavy hitters: " + nonHeavyHitters.size());
		int nonHeavyTriangles = countTrianglesNonHeavyHitters(graph, nonHeavyHitters);
		
		System.out.println("number of heavy triangles: " + heavyTriangles);
		System.out.println("number of non heavy triangles: " + nonHeavyTriangles);
		
		triangles = heavyTriangles + nonHeavyTriangles;
		
		return triangles;
	}
	
	/** Calculates the average clustering coefficient. */
	public static double calcACC(Graph graph) {
		double acc = 0.0;
		double n = graph.getNodes().size();
		for( Node node : graph.getNodes() ) {
			acc = acc + calcCC(node);
		}
		acc = acc / n;
		return acc;
	}
	
	/** Calculates the clustering coefficient of a node. */
	public static double calcCC(Node node) {
		double cc = 0.0;
		double nc2 = nc2(node.getEdges().size());
		if( nc2 != 0 ) {
			cc = node.getValue() /  nc2;
		}
		return cc;
	}
	
	/** Calculates n choose 2. */
	private static double nc2(double n) {
		if( n < 2 ) {
			return 0.0;
		}
		return n * ((n-1) / 2.0);
	}
	
	/** Partition the nodes of the graph into two groups: heavy hitters and non heavy hitters.
	 * Heavy hitter nodes are nodes with a degree higher than sqrt(m). 
	 */
	private static void partition(Graph graph, List<Node> heavyHitters, List<Node> nonHeavyHitters) {
		int m = graph.getEdges().size();
		int sqrtM = (int)Math.sqrt(m);
		System.out.println("sqrt(m)=" + sqrtM);
		
		for( Node n : graph.getNodes() ) {
			if( n.getEdges().size() > sqrtM ) {
				heavyHitters.add(n);
			}else{
				nonHeavyHitters.add(n);
			}
		}
	}
	
	/** Counting the heavy triangles (triangles with all three nodes beeing heavy hitters).
	 * Checking all combinations of heavy hitters, whether they form a triangle.
	 * Checking a combination of three heavy hitters only ones by only checking against nodes further in the list.
	 */
	private static int countTrianglesHeavyHitters(Graph graph, List<Node> heavyHitters) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();

		double n = heavyHitters.size();
		int nn = (int)(Math.ceil((n/100.0)*20.0)); //every 20%

		int triangles = 0;
		
		for( int v = 0; v < heavyHitters.size()-2; v++ ) {
			if( out && (nn != 0 && (v % nn  == 0 || v == (int)(n-3))) ) {
				System.out.println("   " + (v+3) + " / " + (int)n + " nodes done, " + (((int)(((v+3)/n)*10000))/100.0) + "% (" + stopWatch.stopTime() + ")");
			}
						
			for( int u = v+1; u < heavyHitters.size(); u++ ) {
				Edge vu = graph.getEdge(heavyHitters.get(v), heavyHitters.get(u));
				for( int w = u+1; w < heavyHitters.size(); w++ ) {

					if( (vu != null) 
							&& ( graph.getEdge(heavyHitters.get(v), heavyHitters.get(w)) != null) 
							&& ( graph.getEdge(heavyHitters.get(u), heavyHitters.get(w)) != null) ) {
						
						// Increase numbuer of counted triangles:
						triangles++;
						// Increase number of triangles a node is part of:
						graph.getNodes().get(v).setValue( graph.getNodes().get(v).getValue() + 1 );
						graph.getNodes().get(u).setValue( graph.getNodes().get(u).getValue() + 1 );
						graph.getNodes().get(w).setValue( graph.getNodes().get(w).getValue() + 1 );
					}
				}
			}
		}
		return triangles;
	}
	
	/**	Counting the non heavy triangles (triangles with not all three nodes beeing heavy hitters). */
	private static int countTrianglesNonHeavyHitters(Graph graph, List<Node> nonHeavyHitters) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		double n = nonHeavyHitters.size();
		int nn = (int)(Math.ceil((n/100.0)*20.0)); //every 20%
		int i = 1;

		int triangles = 0;
		boolean foundCandidate = false;

		// for all nodes v
		for( Node v : nonHeavyHitters ) {
			if( out && (nn != 0 && (i % nn == 0 || i == n)) ) {
				System.out.println("   " + i + " / " + (int)n + " nodes done, " + (((int)((i/n)*10000)/100.0)) + "% (" + stopWatch.stopTime() + ")");
			}
			
			int degV = v.getEdges().size();
			// for all neighbors u,w of v
			for( Node u : v.getOpposites() ) {
				int degU = u.getEdges().size();
				for( Node w : v.getOpposites() ) {
					
					// skip u == w
					if( u.getId() == w.getId() ) {
						continue;
					}
					
					int degW = w.getEdges().size();
					
					if( (degU < degV) || (degW < degV) ) {
						continue;
					}
					

					if( (degU > degV) && (degW > degV) ) {
						foundCandidate = true;
					}else if( (degU == degV) && (degW > degV) ) {
						if( u.getId() > v.getId() ) {
							foundCandidate = true;
						}
					}else if( (degU > degV) && (degW == degV) ) {
						if( w.getId() > v.getId() ) {
							foundCandidate = true;
						}
					}else {
						if( u.getId() > v.getId() && w.getId() > v.getId() ) {
							foundCandidate = true;
						}
					}
					
					
					if( foundCandidate ) {
						foundCandidate = false;
						// if edge(u,w) dose not exists then skip:
						Edge uw = graph.getEdge(u, w);
						if( uw == null ) {
							continue;
						}
						// Increase numbuer of counted triangles:
						triangles++;
						// Increase number of triangles a node is part of:
						v.setValue( v.getValue() + 1 );
						u.setValue( u.getValue() + 1 );
						w.setValue( w.getValue() + 1 );
					}
				}
			}
			i++;
		}
		// Counted every triangle twice:
		triangles = triangles / 2;
		for( Node node : graph.getNodes() ) {
			node.setValue( node.getValue() / 2);
		}
		return triangles;
	}
}