package main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import graph.Node;
import graph.Graph;
import parser.Parser;
import programming.StopWatch;
import project_5_algorithm.P5Algorithms;
import project_6_algorithm.P6Algorithms;
import project_7_algorithm.P7Algorithms;

public class Main {

	/** 
	 * args[0] : problemnumber (5 / 6 / 7)
	 * args[1] : datafilepath
	 *  
	 *  if problem 5 - shortest path:
	 *  args[2] : name of start node
	 *  args[3] : name of end node
	 *  
	 *  if problem 6 - communities detection
	 *  args[2] : Q-Threshold value
	 *  
	 *  there are no arguments needed for problem 7
	 */
	public static void main(String[] args) {
		System.out.println("##########################################################################");
		
		int problemNumber = 0;
		String fileName = "";
		double qThreshhold = 0.0;
		String strNodeFrom = "";
		String strNodeTo = "";
		if( args.length < 2 ) {
			System.out.println("! Error - Too few arguments.");
			System.exit(1);
		}
			
		if( args[0].matches("[567]") ) {
			problemNumber = Integer.parseInt(args[0]);
		}else{
			System.out.println("! Error - The first argument should be the number of the problem you want to start (5/6/7).");
			System.exit(1);
		}

		fileName = args[1];
		System.out.println("# Graph data path: " + fileName);
		
		// Parsing the data file:
		Graph graph = Parser.parse(fileName);
		if( graph == null ) {
			System.out.println("! Error - Parsing didn't work.");
			System.exit(1);
		}
		System.out.println("# The parsed graph has " + graph.getNodes().size() + " nodes and " + graph.getEdges().size() + " edges.");

		
		// (5) Shortest Path Indexing:
		if( problemNumber == 5 ) {
			if( args.length < 4 ) {
				System.out.println("! Error - Too few arguments.");
				System.exit(1);
			}
			try{
				strNodeFrom = args[2];
				strNodeTo = args[3];
			}catch(Exception e) {
				System.out.println("! Error - Third and/or forth argument do not match assumed node names.");
				System.exit(1);
			}
			if( (! graph.containsNode(strNodeFrom)) || (! graph.containsNode(strNodeTo)) ) {
				System.out.println("! Error - Nodes are not contained in the graph.");
				System.exit(1);
			}

			System.out.println("##########################################################################");
			System.out.println("# Problem (5):");
			System.out.println("# Shortest path indexing with pruned landmark labelling.");
			System.out.println("# Startnode s: " + strNodeFrom);
			System.out.println("# Endnode t: " + strNodeTo);
			System.out.println("##########################################################################");
			startProblem5(graph, strNodeFrom, strNodeTo);
			
		// (6) Community Detection:
		}else if( problemNumber == 6 ) {
			if( args.length < 3 ) {
				System.out.println("! Error - Too few arguments.");
				System.exit(1);
			}
			try{
				qThreshhold = Double.parseDouble(args[2]);
			}catch(Exception e) {
				System.out.println("! Error - Third argument is not a double.");
				System.exit(1);
			}
			System.out.println("##########################################################################");
			System.out.println("# Problem (6):");
			System.out.println("# Using the method of Girvan and Newman to detect communities in a graph.");
			System.out.println("# Given modularity threshold Q: " + qThreshhold);
			System.out.println("##########################################################################");
			startProblem6(graph, qThreshhold);
			
		// (7) Counting Triangles:
		}else if( problemNumber == 7 ) {
			System.out.println("##########################################################################");
			System.out.println("# Problem (7):");
			System.out.println("# Counting triangles in a graph.");
			System.out.println("##########################################################################");
			startProblem7(graph);
		}
		System.out.println("##########################################################################");
	}

	/** Shortest Path Indexing with pruned landmark labelling. */
	private static void startProblem5(Graph graph, String strNodeFrom, String strNodeTo) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		
		System.out.println("# STARTING ###############################################################");
		
		System.out.println("computeLandmarkLabels ... ");
		List<Map<Node, Integer>> index = P5Algorithms.computeLandmarkLabels(graph);
		System.out.println("computeLandmarkLabels ... done (" + stopWatch.stopTime() + ")");
		
		Node s = graph.getNode(strNodeFrom);
		Node t = graph.getNode(strNodeTo);
		System.out.println("findShortestPath... ");
		double distance = P5Algorithms.findShortestPath(s, t, index);
		System.out.println("findShortestPath... done (" + stopWatch.stopTime() + ")");
		
		// Result output:
		System.out.println("# RESULT #################################################################");
		if( distance != Double.POSITIVE_INFINITY ) {
			System.out.println("# Distance from " + strNodeFrom + " to " + strNodeTo + " is " + (int)distance + " hop(s).");
		}else{
			System.out.println("# Distance from " + strNodeFrom + " to " + strNodeTo + " is " + distance + " hops - not reachable!");
		}
		System.out.println("##########################################################################");
		// Index output, if wanted:
		System.out.println("# Do you want to print the whole index to the console? [ (y)es / (n)o ]");
		System.out.println("#  It is only recommended for small graphs!");
		Scanner sc = new Scanner(System.in);
		String userInput = sc.next();
		if( userInput.equals("y") ) {
			System.out.println("##########################################################################");
			P5Algorithms.printIndex(index, graph.getNodes().size(), graph);
		}
		sc.close();
	}
	
	
	/** Using the method of Girvan and Newman to detect communities in a graph. */
	private static void startProblem6(Graph graph, double qThreshhold) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		
		System.out.println("# STARTING ###############################################################");
		
		int initialM = graph.getEdges().size();
		
		List<List<Node>> initialGroups = P6Algorithms.findGroups(graph);
		if( initialGroups.size() == 1 ) {
			System.out.println("The given graph is connected.");
			System.out.println("Initial modularity q = " + P6Algorithms.calculateQ(initialGroups, initialM));
		}else{
			System.out.println("The given graph has " + initialGroups.size() + " connected components (graph is not connected).");
			System.out.println("Initial modularity q = " + P6Algorithms.calculateQ(initialGroups, initialM));
		}
		
		System.out.println("girvanNewmanMethod ... ");
		List<List<Node>> groups = P6Algorithms.girvanNewmanMethod(graph, qThreshhold);
		System.out.println("girvanNewmanMethod ... done (" + stopWatch.stopTime() + ")");
		
		int m = graph.getEdges().size();
		
		// Result output:
		System.out.println("# RESULT #################################################################");
		System.out.println("# Removed " + (initialM - m) + " edge(s).");
		System.out.println("# The graph had " + initialGroups.size() + " connected component(s) before.");
		System.out.println("# The graph has " + groups.size() + " group(s)/communitie(s) now.");
		System.out.println("# The graphs modularity value q is now " + P6Algorithms.calculateQ(groups, m));
		System.out.println("##########################################################################");
		// Communities output, if wanted:
		System.out.println("# Do you want to print all communities to the consol? [ (y)es / (n)o ]");
		System.out.println("#  It is only recommended for small graphs!");
		Scanner sc = new Scanner(System.in);
		String userInput = sc.next();
		if( userInput.equals("y") ) {
			System.out.println("##########################################################################");
			P6Algorithms.printGroups(groups);
		}
		sc.close();
	}
	
	/** Counting triangles in a graph. */	
	private static void startProblem7(Graph graph) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		
		System.out.println("# STARTING ###############################################################");
		
		System.out.println("counting triangles ... ");
		int triangles = P7Algorithms.countTriangles(graph);
		System.out.println("counting triangles ... done (" + stopWatch.stopTime() + ")");

		// Result output:
		System.out.println("# RESULT #################################################################");
		System.out.println("# The graph has " + triangles + " triangles.");
		System.out.println("# The graphs average clustering coefficient is: " + P7Algorithms.calcACC(graph));
	}
}