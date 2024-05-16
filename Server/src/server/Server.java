package server;

import graph.GraphProcessor;

import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Server {
    public static void main(String[] args) {
        try {

            Properties props = new Properties();
            props.load(new FileInputStream("system.properties"));
            String serverAddress = props.getProperty("GSP.server");
            int registryPort = Integer.parseInt(props.getProperty("GSP.rmiregistry.port"));
            System.setProperty("java.rmi.server.hostname", serverAddress);
            GraphProcessor graphProcessor = new GraphProcessor();
            graphProcessor.processInitialGraphInput(Input.readInput("src/input.text"));
            GraphRmiImpl obj = new GraphRmiImpl(graphProcessor);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("GraphRmiInterface", obj);
            System.out.println("Server bound in registry");
            while (true) {
                Thread.sleep(1000); // Sleep for 1 second before next iteration
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
