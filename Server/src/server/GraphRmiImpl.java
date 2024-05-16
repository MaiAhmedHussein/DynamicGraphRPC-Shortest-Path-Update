package server;


import graph.GraphProcessor;
import RMIInterface.GraphRmiInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GraphRmiImpl extends UnicastRemoteObject implements GraphRmiInterface {

    public GraphProcessor graphProcessor;

    public GraphRmiImpl(GraphProcessor graphProcessor) throws RemoteException {
        super();
        this.graphProcessor = graphProcessor;
    }

    @Override
    public ArrayList<String> processBatchRequests(List<String> operations) throws RemoteException {
        return this.graphProcessor.processOperations(operations);
    }
}
