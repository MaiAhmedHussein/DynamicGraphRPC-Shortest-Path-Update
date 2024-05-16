package graph;

import java.util.*;

public class GraphProcessor {
    private Graph graph;
    private ShortestPath shortestPath;
    private Scanner scanner;

    public GraphProcessor() {
        graph = new Graph();
        shortestPath = new ShortestPath();
        scanner = new Scanner(System.in);
    }

    public String processInitialGraphInput(ArrayList<int[]> list) {
        for (int[] ints : list) {
            this.graph.addEdge(ints[0], ints[1]);
        }
        return "OK";
    }



    public ArrayList<String> processOperations(List<String> operations) {
        ArrayList<String> res = new ArrayList<>();
        for (String operation : operations) {
            String[] tokens = operation.split(" ");
            char opType = tokens[0].charAt(0);
            int src = Integer.parseInt(tokens[1]);
            int dest = Integer.parseInt(tokens[2]);

            switch (opType) {
                case 'Q':
                    int distance = shortestPath.dijkstra(graph, src, dest);
                    res.add(distance+"");
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
        return res;
    }
}

