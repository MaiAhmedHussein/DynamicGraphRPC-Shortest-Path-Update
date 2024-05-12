package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Integer, List<Integer>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addEdge(int source, int destination) {
        if(!adjList.containsKey(source)) {
            adjList.put(source, new ArrayList<>());
        }
        adjList.get(source).add(destination);
    }

    public void removeEdge(int source, int destination) {
        if(adjList.containsKey(source)) {
            adjList.get(source).remove((Integer) destination);
        }

    }

    public List<Integer> getNeighbors(int node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    public boolean hasEdge(int source, int destination) {
        return adjList.containsKey(source) && adjList.get(source).contains(destination);
    }
}
