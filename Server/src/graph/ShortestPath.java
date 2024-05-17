package graph;

import java.util.*;
import java.util.function.Function;

public class ShortestPath {
    public int bellmanFord(Graph graph, int src, int dest) {
        Map<Integer, Integer> distance = new HashMap<>();
        for (int node : graph.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
        }
        distance.put(src, 0);

        for (int i = 1; i <= graph.getNodes().size(); i++) {
            for (int node : graph.getNodes()) {
                for (int neighbor : graph.getNeighbors(node)) {
                    if (distance.get(node) != Integer.MAX_VALUE && distance.containsKey(neighbor) && distance.get(node) + 1 < distance.get(neighbor)) {
                        distance.put(neighbor, distance.get(node) + 1);
                    }
                }
            }
        }

        for (int node : graph.getNodes()) {
            for (int neighbor : graph.getNeighbors(node)) {
                if (distance.get(node) != Integer.MAX_VALUE && distance.containsKey(neighbor) && distance.get(node) + 1 < distance.get(neighbor)) {
                    throw new RuntimeException("Graph contains a negative-weight cycle");
                }
            }
        }

        if (distance.containsKey(dest) && distance.get(dest) != Integer.MAX_VALUE) {
            return distance.get(dest);
        } else {
            return -1; // Return -1 if the shortest path distance is infinity or the destination node is not reachable
        }
    }
    public int bfs(Graph graph, int src, int dest) {
        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        visited.add(src);
        distance.put(src, 0);
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int curr = queue.remove();
            for (int neighbor : graph.getNeighbors(curr)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distance.put(neighbor, distance.get(curr) + 1);
                    if (neighbor == dest) // stopping condition
                        return distance.get(dest);
                    queue.add(neighbor);
                }
            }
        }
        return -1;  // no path
    }

    public int aStar(Graph graph, int src, int dest, Function<Integer, Integer> heuristic) {
        Map<Integer, Integer> distance = new HashMap<>();
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> distance.get(node) + heuristic.apply(node)));
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