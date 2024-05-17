package graph;

import server.Logger;

import java.util.*;
import java.util.function.Function;

public class GraphProcessor {
    private Graph graph;
    private ShortestPath shortestPath;
    public Logger logger;

    public GraphProcessor(Logger logger) {
        graph = new Graph();
        shortestPath = new ShortestPath();
        this.logger = logger;
    }

    public void processInitialGraphInput(ArrayList<int[]> graphList) {
        for (int[] i : graphList) {
            graph.addEdge(i[0], i[1]);
        }
    }


    public ArrayList<String> processOperations(List<String> operations, boolean optimized) {
        ArrayList<String> res = new ArrayList<>();
        for (String operation : operations) {
            String[] tokens = operation.split(" ");
            char opType = tokens[0].charAt(0);
            int src = Integer.parseInt(tokens[1]);
            int dest = Integer.parseInt(tokens[2]);

            switch (opType) {
                case 'Q':
                    long startTime, endTime;
                    int distance;
                    /*long startTime = System.nanoTime();
                    int distance = shortestPath.dijkstra(graph, src, dest);
                    long endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("Dijkstra's algorithm execution time: " + (endTime - startTime) + " nanoseconds");*/
                    if (optimized) {
                        startTime = System.nanoTime();
                        distance = shortestPath.bfs(graph, src, dest);
                        endTime = System.nanoTime();
                        System.out.println(distance);
                        logger.log(operation + " (BFS algorithm) ----> " +  distance + "\nExecution time: " + (endTime - startTime) + " nanoseconds");
                    }
                    /*Function<Integer, Integer> heuristic = node -> 0; // Replace with your actual heuristic function
                    startTime = System.nanoTime();
                    distance = shortestPath.aStar(graph, src, dest, heuristic);
                    endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("A* algorithm execution time: " + (endTime - startTime) + " nanoseconds");*/
                    else {
                        startTime = System.nanoTime();
                        distance = shortestPath.bellmanFord(graph, src, dest);
                        endTime = System.nanoTime();
                        System.out.println(distance);
                        logger.log(operation + " (Bellman Ford algorithm) ----> " +  distance + "\nExecution time: " + (endTime - startTime) + " nanoseconds");
                    }
                    res.add(distance + "\n");
                    break;
                case 'A':
                    if (!graph.hasEdge(src, dest)) {
                        graph.addEdge(src, dest);
                        logger.log("Edge added: " + src + " " + dest);
                        res.add("1\n");
                    }else {
                        res.add("0\n");
                    }
                    break;
                case 'D':
                    if(graph.hasEdge(src, dest)) {
                        graph.removeEdge(src, dest);
                        logger.log("Edge removed: " + src + " " + dest);
                        res.add("1\n");
                    }else {
                        res.add("0\n");
                    }
                    break;

            }
        }
        //logger.log("result of processing: " + res.toString());
        return res;
    }
}
