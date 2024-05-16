package org.example;

import java.util.*;
import java.util.function.Function;

class GraphProcessor {
    private Graph graph;
    private ShortestPath shortestPath;

    public GraphProcessor() {
        graph = new Graph();
        shortestPath = new ShortestPath();
    }

    public void processInitialGraphInput(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("S")) {
                break;
            }
            String[] tokens = line.split(" ");
            int src = Integer.parseInt(tokens[0]);
            int dest = Integer.parseInt(tokens[1]);
            graph.addEdge(src, dest);
        }
    }

    public void processBatchRequests(Scanner scanner) {
        while (scanner.hasNextLine()) {
            List<String> operations = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("F")) {
                    break;
                }
                operations.add(line);
            }
            processOperations(operations);
        }
    }

    private void processOperations(List<String> operations) {
        for (String operation : operations) {
            String[] tokens = operation.split(" ");
            char opType = tokens[0].charAt(0);
            int src = Integer.parseInt(tokens[1]);
            int dest = Integer.parseInt(tokens[2]);

            switch (opType) {
                case 'Q':
                    long startTime = System.nanoTime();
                    int distance = shortestPath.dijkstra(graph, src, dest);
                    long endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("Dijkstra's algorithm execution time: " + (endTime - startTime) + " nanoseconds");

                    startTime = System.nanoTime();
                    distance = shortestPath.bfs(graph, src, dest);
                    endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("BFS algorithm execution time: " + (endTime - startTime) + " nanoseconds");

                    Function<Integer, Integer> heuristic = node -> 0; // Replace with your actual heuristic function
                    startTime = System.nanoTime();
                    distance = shortestPath.aStar(graph, src, dest, heuristic);
                    endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("A* algorithm execution time: " + (endTime - startTime) + " nanoseconds");

                    startTime = System.nanoTime();
                    distance = shortestPath.bellmanFord(graph, src, dest);
                    endTime = System.nanoTime();
                    System.out.println(distance);
                    System.out.println("Bellman Ford algorithm execution time: " + (endTime - startTime) + " nanoseconds");

                    break;
                case 'A':
                    if (!graph.hasEdge(src, dest)) {
                        graph.addEdge(src, dest);
                    }
                    break;
                case 'D':
                    graph.removeEdge(src, dest);
                    break;
            }
        }
    }
}

