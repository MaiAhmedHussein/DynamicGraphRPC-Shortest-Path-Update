package RMIInterface;


import graph.GraphProcessor;
import server.Logger;

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
    public ArrayList<String> processBatchRequests(String clientId, List<String> operations, boolean optimized) throws RemoteException {
        graphProcessor.logger.log("Started processing clinet: " + clientId);
        return this.graphProcessor.processOperations(operations, optimized);
    }
}
