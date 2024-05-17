package client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BatchGenerator {
    private static final char[] OPERATIONS = {'Q', 'A', 'D'};
    private static final int MAX_NODES = 100; // Adjust this value based on your graph size
    private static final Random random = new Random();

    public static ArrayList<String> generateBatch(Logger logger,double writePercentage) {
        Random random = new Random();
        logger.log("Generated Batch of Requests:\n");
        ArrayList<String> ops = new ArrayList<>();
            for (int i = 0; i < 1000; i++) { // Generate 1000 operations
                char operation;
                if (random.nextDouble() < writePercentage) {
                    operation = OPERATIONS[random.nextInt(2) + 1]; // 'A' or 'D'
                } else {
                    operation = OPERATIONS[0]; // 'Q'
                }
                int src = random.nextInt(MAX_NODES);
                int dest = random.nextInt(MAX_NODES);
                String op = operation + " " + src + " " + dest;
                logger.log(op + "\n");
                ops.add(op);
            }
         return ops;
    }

}