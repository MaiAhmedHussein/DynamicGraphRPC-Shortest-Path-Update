package com.org.example.server;

import com.org.example.graph.GraphProcessor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphRmiImpl extends UnicastRemoteObject implements GraphRmiInterface {

    public GraphProcessor graphProcessor;

    public GraphRmiImpl() throws RemoteException {
        super();
        this.graphProcessor = new GraphProcessor();
    }

    @Override
    public String initializeGraph(ArrayList<int[]> adjList) throws RemoteException {
        return this.graphProcessor.processInitialGraphInput(adjList);
    }

    @Override
    public ArrayList<String> processBatchRequests(List<String> operations) throws RemoteException {
        return this.graphProcessor.processOperations(operations);
    }
}
