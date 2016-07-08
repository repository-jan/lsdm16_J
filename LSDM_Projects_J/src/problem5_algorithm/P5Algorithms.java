package problem5_algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import graph.Edge;
import graph.Node;
import graph.Graph;
import programming.StopWatch;

/** Shortest Path Indexing with pruned BFS. */
public class P5Algorithms {
	
	// For console outputs.
	private static boolean out = false;
	
	/** Finds the number of hops between the given nodes by querying the index. */
	public static double findShortestPath(Node s, Node t, List<Map<Node, Integer>> index) {
		double distance = query(s, t, index);
		return distance;
	}
	
	/** Computes the labels using pruned BFS. */
	public static List<Map<Node, Integer>> computeLandmarkLabels(Graph graph) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		double n = graph.getNodes().size();
		int nn = (int)(Math.ceil((n/100.0)*10.0)); //every 10%
		
		List<Map<Node, Integer>> index = new ArrayList<>();
		
		// L_0 - init:
		for( int i = 0; i < graph.getNodes().size(); i++ ) {
			index.add(new HashMap<>());
		}

		// L_1 ... L_n:
		for( int k = 1; k <= graph.getNodes().size() ; k++ ) {
			if( out && (nn != 0 && (k % nn  == 0 || k == (int)n)) ) {
				System.out.println("   " + k + " / " + (int)n + " nodes done, " + (((int)((k/n)*10000))/100.0) + "% (" + stopWatch.stopTime() + ")");
			}
			index = prunedBFS(graph, graph.getNodes().get(k-1), index);
		}
		return index;
	}
	
	/** Performes the pruned BSF starting with the given node to return the next iteration of the index. */
	private static List<Map<Node, Integer>> prunedBFS(Graph graph, Node k, List<Map<Node, Integer>> index) {
		
		// Init nodes:
		for( Node v : graph.getNodes() ) {
			v.setLevel(-1);
		}
		k.setLevel(0);

		// BFS:
		Queue<Node> q = new LinkedList<>();
		q.add(k);
		while( ! q.isEmpty() ) {
			Node u = q.poll();
			
			if( query(k, u, index) <= u.getLevel()) {
				continue;
			}
			for( Edge e : u.getEdges() ) {
				Node opposite = e.getOpposite(u);
				if( opposite.getLevel() == -1 ) {
					opposite.setLevel(u.getLevel()+1);
					if( ! q.contains(opposite) ) {
						q.add(opposite);
					}
				}
			}
			if( ! k.equals(u) ) {
				index.get(u.getId()).put(k, u.getLevel());
			}
		}
		return index;
	}
	
	/** Looks in the given index, for the distance between the two given nodes. */
	private static double query(Node k, Node u, List<Map<Node, Integer>> index) {		
		Map<Node, Integer> indexK = index.get(k.getId());
		Map<Node, Integer> indexU = index.get(u.getId());
		
		if( indexK.containsKey(u) ) {
			return indexK.get(u);
		}
		if( indexU.containsKey(k) ) {
			return indexU.get(k);
		}
		
		double min = Double.POSITIVE_INFINITY;
		for( Map.Entry<Node, Integer> entryK : indexK.entrySet() ) {
			if( indexU.containsKey(entryK.getKey()) ) {
				int dist = entryK.getValue() + indexU.get(entryK.getKey());
				if( dist < min ) {
					min = dist;
				}
			}
		}
		return min;
	}
	
	/**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**/
	/** Method to print the index to console. */
	public static void printIndex(List<Map<Node, Integer>> index, int k, Graph graph) {
		System.out.println("# Index " + k + ":");
		for( int i = 0; i < index.size(); i++ ) {
			String strNodes = "";
			String strDistances = "";
			String name = "";
			String dist = "";
			int lenNode = 0;
			int lenDist = 0;
			for( Map.Entry<Node, Integer> ele : index.get(i).entrySet() ) {
				name = ele.getKey().getName();
				dist = "" + ele.getValue();
				lenNode = name.length();
				lenDist = dist.length();
				
				
				strNodes = strNodes + name;
				strDistances = strDistances + dist;
				
				
				if( lenDist > lenNode ) {
					strNodes = addNBlankspace(strNodes, lenDist-lenNode+1);
					strDistances = addNBlankspace(strDistances, 1);
				}else if( lenDist < lenNode ) {
					strNodes = addNBlankspace(strNodes, 1);
					strDistances = addNBlankspace(strDistances, lenNode);
				}else{
					strNodes = addNBlankspace(strNodes, 1);
					strDistances = addNBlankspace(strDistances, 1);
				}

			}
			System.out.println("#  L" + k + "_(" + graph.getNodes().get(i).getName() + "):");
			System.out.println("#   " + strNodes);
			System.out.println("#   " + strDistances);
		}
	}
	
	private static String addNBlankspace(String str, int n) {
		for( int i = 0; i < n; i++ ) {
			str = str + " ";
		}
		return str;
	}
}