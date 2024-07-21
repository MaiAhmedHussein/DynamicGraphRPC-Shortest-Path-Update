package server;

import RMIInterface.GraphRmiImpl;
import graph.GraphProcessor;

import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Properties;

public class Server {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            Logger logger = new Logger("ServerLogs/log_" + System.currentTimeMillis());
            GraphProcessor graphProcessor = new GraphProcessor(logger);
            graphProcessor.processInitialGraphInput(Objects.requireNonNull(Input.readInput("src/input.txt")));
            GraphRmiImpl obj = new GraphRmiImpl(graphProcessor);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("Update", obj);
            System.out.println("Server Started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
