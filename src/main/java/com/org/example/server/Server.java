package com.org.example.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            GraphRmiImpl obj = new GraphRmiImpl();
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
