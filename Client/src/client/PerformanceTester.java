package client;

import RMIInterface.GraphRmiInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class PerformanceTester {

    public static void testPerformance(String num, Logger logger, Logger batchesLogs, double writePercentage, int numRequests, GraphRmiInterface graphRmiInterface) throws RemoteException {
        ArrayList<String> batch = BatchGenerator.generateBatch(batchesLogs, writePercentage, numRequests);

        long totalResponseTimeVariant1 = 0;
        long totalResponseTimeVariant2 = 0;

        long startTime = System.nanoTime();
        graphRmiInterface.processBatchRequests(num, batch, false);
        long endTime = System.nanoTime();
        totalResponseTimeVariant1 += (endTime - startTime);

        startTime = System.nanoTime();
        graphRmiInterface.processBatchRequests(num, batch, true);
        endTime = System.nanoTime();
        totalResponseTimeVariant2 += (endTime - startTime);

        long averageResponseTimeVariant1 = totalResponseTimeVariant1 / numRequests;
        long averageResponseTimeVariant2 = totalResponseTimeVariant2 / numRequests;

        logger.log("Average response time for not optimized variant: " + averageResponseTimeVariant1 + " ns");
        logger.log("Average response time for optimized variant: " + averageResponseTimeVariant2 + " ns");
    }

    public static void varyingFrequencyOfRequests(String num, Logger logger, String batchesPath, double writePercentage, GraphRmiInterface graphRmiInterface) throws RemoteException {
        Logger batchesLogs = new Logger(batchesPath,num);
        int[] requests = {10, 100, 1000, 10000};
        for (int numOfRequests : requests) {
            logger.log("Testing with Number Of Requests: " + numOfRequests);
            PerformanceTester.testPerformance(num, logger, batchesLogs, writePercentage, numOfRequests, graphRmiInterface);
        }
    }

    public static void varyingPercentageOfWriteOp(String num, Logger logger, String batchesPath, int numRequests, GraphRmiInterface graphRmiInterface) throws RemoteException {
        Logger batchesLogs = new Logger(batchesPath,num);
        double[] writePercentages = {0.0, 0.1, 0.2, 0.5, 0.8, 1.0};
        for (double writePercentage : writePercentages) {
            logger.log("Testing with writePercentage: " + writePercentage);
            PerformanceTester.testPerformance(num, logger, batchesLogs, writePercentage, numRequests, graphRmiInterface);
        }
    }

    public static void varyingNumberOfNodes(String num, String numOfNodes, GraphRmiInterface graphRmiInterface) throws RemoteException {
        long now = System.currentTimeMillis();
        String batchesPath = "clientLogs/performancetests/responseTimeVsNumberOfNodes/" + "node"+num + "_" + numOfNodes + "_" + now + "_generated_batch";
        Logger logger = new Logger("clientLogs/performancetests/responseTimeVsNumberOfNodes/" +  "node"+num + "_" + numOfNodes + "_" + now,num);
        Logger batchesLog = new Logger(batchesPath,num);
        logger.log("Testing with number of nodes = " + numOfNodes);
        testPerformance(num, logger, batchesLog, 0.5, 100, graphRmiInterface);
    }


}

