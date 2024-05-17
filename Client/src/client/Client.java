package client;

import RMIInterface.GraphRmiInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Client {


    private static void responseTimeVsRequestsFrequency(boolean optimized, String clientID, GraphRmiInterface stub)
            throws IOException, InterruptedException {

        Logger logger = new Logger("performancetest/responseTimeVsRequestsFrequency_" + clientID + "_optimized=" + optimized);

        int trials = 5, points = 10, percent = 50;
        for (int j = 0; j < points; j++) {
            long[] median = new long[trials];
            for (int i = 0; i < trials; i++) {
                String path = "performancetest/responseTimeVsRequestsFrequency/" + clientID + "_batch_"
                        + j + "_Trial_" + i + ".txt";
                ArrayList<String> batch = BatchGenerator.generateBatch(logger, percent);
                // Calculate Response Time
                long start = System.currentTimeMillis();
                List<String> results = stub.processBatchRequests(clientID, batch, optimized);
                long end = System.currentTimeMillis();
                median[i] = (end - start);
                logger.log(median[i] + "");

                // Sleep till the next batch
                Thread.sleep(ThreadLocalRandom.current().nextInt(1, 11) * 1000L);
            }
            Arrays.sort(median);
            logger.log("Point " + (j + 1) + ": Response Time = " + median[trials / 2] + " ms");
        }
    }

    private static void responseTimeVsOperationsPercentage(boolean optimized, String clientID, GraphRmiInterface stub)
            throws IOException, InterruptedException {

        Logger logger = new Logger("performancetest/responseTimeVsOperationsPercentage_" + clientID + "_optimized=" + optimized);

        int percent = 10, trials = 5, points = 10, quires = 1000;
        for (int j = 0; j < points; j++) {

            long[] median = new long[trials];
            for (int i = 0; i < trials; i++) {


                ArrayList<String> batch = BatchGenerator.generateBatch(logger, percent);

                // Calculate Response Time
                long start = System.currentTimeMillis();
                ArrayList<String> results = stub.processBatchRequests(clientID, batch, optimized);
                long end = System.currentTimeMillis();

                median[i] = (end - start);
                logger.log(median[i] + "");

                // Sleep till the next batch
                Thread.sleep(ThreadLocalRandom.current().nextInt(1, 11) * 1000L);
            }
            Arrays.sort(median);
            logger.log("Point " + (j + 1) + ": Response Time = " + median[trials / 2] + " ms");
        }
    }

    /**
     * Performance Analysis:
     * Response Time(median of 20 trials) vs Number of Nodes(1:5)
     * Quires = 30 query
     *
     * @param stub for RMI processing
     */
    private static void responseTimeVsNumberOfNodes(boolean optimized, String clientID, GraphRmiInterface stub)
            throws IOException, InterruptedException {

        Logger logger = new Logger("performancetest/responseTimeVsNumberOfNodes13_" + clientID + "_optimized=" + optimized);

        int trials = 5, quires = 1000, percent = 50;
        long[] median = new long[trials];
        for (int i = 0; i < trials; i++) {
            ArrayList<String> batch = BatchGenerator.generateBatch(logger, percent);

            // Calculate Response Time
            long start = System.currentTimeMillis();
            List<String> results = stub.processBatchRequests(clientID, batch, optimized);
            long end = System.currentTimeMillis();

            median[i] = (end - start);
            logger.log(median[i] + "");

            // Sleep till the next batch
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 11) * 1000L);
        }
        Arrays.sort(median);
        logger.log("Response Time = " + median[trials / 2] + " ms");
    }

    public static void main(String[] args) {
        try {
            String clientID = args[0]; //client id
            String performanceTesting = args[1]; //performance test or not
            String optimized = args[2]; //optimized or not
            Logger logger = new Logger(clientID + "_" + System.currentTimeMillis() + ".txt");
            Properties props = new Properties();
            props.load(new FileInputStream("system.properties"));
            String serverAddress = props.getProperty("GSP.server");
            int registryPort = Integer.parseInt(props.getProperty("GSP.rmiregistry.port"));
            Registry registry = LocateRegistry.getRegistry(serverAddress, registryPort);
            GraphRmiInterface stub = (GraphRmiInterface) registry.lookup("Update");
            boolean b = !Objects.equals(optimized, "0");

            switch (performanceTesting) {
                case "0":
                    ArrayList<String> operations = BatchGenerator.generateBatch(logger, 0.5);
                    ArrayList<String> res = stub.processBatchRequests(clientID, operations, false);
                    logger.log("Result:");
                    logger.log(res.toString());
                    break;
                case "1":
                    Client.responseTimeVsRequestsFrequency(b, clientID, stub);
                    break;
                case "2":
                    Client.responseTimeVsOperationsPercentage(b, clientID, stub);
                    break;
                case "3":
                    Client.responseTimeVsNumberOfNodes(b, clientID, stub);
                    break;
                default:
                    logger.log("Please Enter a valid input");
            }

        } catch (NotBoundException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}