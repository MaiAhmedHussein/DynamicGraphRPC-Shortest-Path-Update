package org.example;

import java.util.*;

public class ShortestPath {
    public int dijkstra(Graph graph, int src, int dest) {
        Map<Integer, Integer> distance = new HashMap<>();
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        Set<Integer> visited = new HashSet<>();

        distance.put(src, 0);
        minHeap.offer(src);

        while (!minHeap.isEmpty()) {
            int node = minHeap.poll();
            if (node == dest) {
                return distance.get(node);
            }
            visited.add(node);

            List<Integer> neighbors = graph.getNeighbors(node);
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    int newDist = distance.get(node) + 1; // Assuming unweighted graph
                    if (!distance.containsKey(neighbor) || newDist < distance.get(neighbor)) {
                        distance.put(neighbor, newDist);
                        minHeap.offer(neighbor);
                    }
                }
            }
        }
        return -1; // No path found
    }
}
