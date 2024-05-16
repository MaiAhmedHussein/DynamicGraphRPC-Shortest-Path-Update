package com.org.example.client;

import com.org.example.server.GraphRmiInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Get the registry
            Registry registry = LocateRegistry.getRegistry(null);
            // Look up the remote object
            GraphRmiInterface stub = (GraphRmiInterface) registry.lookup("GraphRmiInterface");
            Scanner scanner = new Scanner(System.in);
            ArrayList<int[]> adjList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("S")) {
                    break;
                }
                String[] tokens = line.split(" ");
                int src = Integer.parseInt(tokens[0]);
                int dest = Integer.parseInt(tokens[1]);
                adjList.add(new int[]{src, dest});
            }
            String response = stub.initializeGraph(adjList);
            System.out.println(response);
            while (scanner.hasNextLine()) {
                List<String> operations = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equals("F")) {
                        break;
                    }
                    operations.add(line);
                }
                ArrayList<String> res = stub.processBatchRequests(operations);
                for (String r : res){
                    System.out.println(r);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}