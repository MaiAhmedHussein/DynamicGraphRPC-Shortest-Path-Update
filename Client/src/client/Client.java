package client;

import RMIInterface.GraphRmiInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        try {

            String clientID = args[0]; //client id
            String performanceTesting = args[1]; //performance test or not
            Logger logger;
            Registry registry = LocateRegistry.getRegistry("192.168.1.9", 1099);
            GraphRmiInterface stub = (GraphRmiInterface) registry.lookup("Update");
            long now;
            ArrayList<String> operations;
            String batchesPath;
            switch (performanceTesting) {
                case "0":
                    logger = new Logger("clientLogs/randomRuns/" + clientID);
                    operations = BatchGenerator.generateBatch(logger, 0.5, 100);
                    long startTime = System.currentTimeMillis();
                    ArrayList<String> res = stub.processBatchRequests(clientID, operations, false);
                    long endTime = System.currentTimeMillis();
                    long responseTime = endTime - startTime;
                    for (int i = 0; i < operations.size(); i++) {
                        String operation = operations.get(i);
                        String result = res.get(i);
                        String logMessage;

                        if (operation.startsWith("A")) {
                            if (result.equals("1\n")) {
                                logMessage = operation + " ----> Edge added successfully";
                            } else {
                                logMessage = operation + " ----> Already there is an edge";
                            }
                        } else if (operation.startsWith("D")) {
                            if (result.equals("1\n")) {
                                logMessage = operation + " ----> Edge deleted successfully";
                            } else {
                                logMessage = operation + " ----> Already there is no edge";
                            }
                        } else if (operation.startsWith("Q")) {
                            logMessage = operation + " ----> " + result;
                        } else {
                            logMessage = operation + " ----> Unknown operation";
                        }
                        logger.log(logMessage);
                    }
                    logger.log("Response time: " + responseTime + " ms");
                    break;
                case "1":
                    now = System.currentTimeMillis();
                    logger = new Logger("clientLogs/performancetests/responseTimeVsRequestsFrequency/" + clientID + "_" + now);
                    batchesPath = "clientLogs/performancetests/responseTimeVsRequestsFrequency/" + clientID + "_" + now + "_generatedBatches";
                    PerformanceTester.varyingFrequencyOfRequests(clientID, logger, batchesPath, 0.5, stub);
                    break;
                case "2":
                    now = System.currentTimeMillis();
                    logger = new Logger("clientLogs/performancetests/responseTimeVsOperationsPercentage/" + clientID + "_" + now);
                    batchesPath = "clientLogs/performancetests/responseTimeVsOperationsPercentage/" + clientID + "_" +  now + "_generatedBatches";
                    PerformanceTester.varyingPercentageOfWriteOp(clientID, logger, batchesPath, 100, stub);
                    break;
                case "3":
                    String numOfNodes = args[2];
                    PerformanceTester.varyingNumberOfNodes(clientID, numOfNodes, stub);
                    break;
                default:
                    logger = new Logger("clientLogs/randomRuns/" + clientID);
                    logger.log("Please Enter a valid input");
            }

        } catch (NotBoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}