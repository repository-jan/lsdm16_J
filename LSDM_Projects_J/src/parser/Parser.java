package parser;

import java.io.BufferedReader;
import java.io.FileReader;

import programming.StopWatch;
import graph.Graph;

/** Expected is a list of edges.
 * The file can contain comment lines, starting with #.
 * The regex for an edge line is: "([a-zA-Z0-9]+)([ \\t])([a-zA-Z0-9]+)"
 * The nodes can have names containing letters and numbers, separated by a single tab or a single blank.
 * The graph is interpreted as an undirected graph. Also if an edge (x,y) is added, the edge (y,x) will not be added additionally.
 */
public class Parser {
	
	private static String pattern = "([a-zA-Z0-9]+)([ \\t])([a-zA-Z0-9]+)";
	private static char separatorT = '\t';
	private static char separatorB = ' ';
	
	public static Graph parse(String fileName) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.startTimer();
		
		Graph graph = new Graph();
		
		char separator = ' ';
		
		try( FileReader fr = new FileReader(fileName); 
				BufferedReader br = new BufferedReader(fr);) {
			
	        String line = "";
	        String from = "";
	        String to = "";
	        	        
	        while( (line = br.readLine()) != null ) {
	        	
	        	if( line.startsWith("#") ) {
	        		// comment
	        	}else if ( line.matches(pattern) ) {
	        		
	        		if( (line.indexOf(separatorT) == -1) && (line.indexOf(separatorB) != -1) ) {
	        			separator = separatorB;
	        		}else if( (line.indexOf(separatorT) != -1) && (line.indexOf(separatorB) == -1) ) {
	        			separator = separatorT;
	        		}else{
	        			throw new RuntimeException("Parsing Error - separation between numbers is off (line: \"" + line + "\").");
	        		}

	        		from = line.substring(0, line.indexOf(separator));
	        		to = line.substring(line.indexOf(separator)+1);
	        		
	        		graph.addNode(from);
	        		graph.addNode(to);
	        		
	        		graph.addEdge(from, to);
	        		
	        	}else{
	        		throw new RuntimeException("Parsing Error - expected line format: "+ pattern +" (in line: \"" + line + "\").");
	        	}
	        }
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		System.out.println( "# Parsing finished after " + stopWatch.stopTime() + "." );
		return graph;
	}
}