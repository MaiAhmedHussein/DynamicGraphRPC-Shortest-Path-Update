package RMIInterface;


import graph.GraphProcessor;

import java.rmi.RemoteException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GraphRmiImpl extends UnicastRemoteObject implements GraphRmiInterface {

    public GraphProcessor graphProcessor;
    private final Lock lock;

    public GraphRmiImpl(GraphProcessor graphProcessor) throws RemoteException {
        super();
        this.graphProcessor = graphProcessor;
        this.lock = new ReentrantLock();

    }

    @Override
    public ArrayList<String> processBatchRequests(String clientId, List<String> operations, boolean optimized) throws RemoteException {
        lock.lock();
        try {
            // Critical section of the code
            graphProcessor.logger.log("Started processing clinet: " + clientId);
            return this.graphProcessor.processOperations(operations, optimized);
        } finally {
            lock.unlock();
        }
    }
}
