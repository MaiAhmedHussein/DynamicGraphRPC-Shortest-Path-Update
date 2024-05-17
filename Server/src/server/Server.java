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
            Properties props = new Properties();
            props.load(new FileInputStream("src/system.properties"));
            String serverAddress = props.getProperty("GSP.server");
            System.setProperty("java.rmi.server.hostname", serverAddress);
            Logger logger = new Logger("ServerLogs_" + System.currentTimeMillis());
            GraphProcessor graphProcessor = new GraphProcessor(logger);
            graphProcessor.processInitialGraphInput(Objects.requireNonNull(Input.readInput("src/input.txt")));
            GraphRmiImpl obj = new GraphRmiImpl(graphProcessor);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("Update", obj);
            System.out.println("Server Started");
            while (true) {
                Thread.sleep(1000); // Sleep for 1 second before next iteration
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
