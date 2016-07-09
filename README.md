# lsdm16_J

Programming Assignments - LSDM 2016

I programmed three of the seven programming assignments:

- **Project (5):** Shortest Path Indexing.
  
- **Project (6):** Clustering by pertitioning usning Betweenness Centrality.
  
- **Project (7):** Triangle counting.
  
The input graph is considered to be undirected.
The data file from which the graph is parsed has to be a list of edges.
The file can contain comment lines starting with #.
Otherwise a line represents an edge, given by the two nodes seperated with a single tab or blank.
The nodes can be named using numbers and letters.
  
Use the jar to run the projects.
You have to input the following arguments, seperated by a blank:
  - **Argument 1** is to choose the project. So you have to put in the corresponding number of the above mentioned projects.
  - **Argument 2** should be the path to the data file.
  
  - If argument 1 is 5: You have to put in 2 nodes to output distance between them.
    - **Argument 3** should be the name of the one node.
    - **Argument 4** should be the name of the other node.
    
  - If argument 1 is 6: You have to put in the treshold modularity value you want to reach.
    - **Argument 3** should be the treshold value.

Used Java 1.8.
- java -jar LSDM_Projects_J.jar 5 [data file path] [node 1] [node 2]
- java -jar LSDM_Projects_J.jar 6 [data file path] [treshold]
- java -jar LSDM_Projects_J.jar 7 [data file path]
