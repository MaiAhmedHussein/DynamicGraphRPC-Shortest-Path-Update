package org.example;

import java.util.*;

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
                    int distance = shortestPath.dijkstra(graph, src, dest);
                    System.out.println(distance);
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

