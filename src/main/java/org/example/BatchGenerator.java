package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BatchGenerator {
    private static final char[] OPERATIONS = {'Q', 'A', 'D'};
    private static final int MAX_NODES = 100; // Adjust this value based on your graph size

    public static void generateBatch(String filename, double writePercentage) {
        Random random = new Random();
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < 1000; i++) { // Generate 1000 operations
                char operation;
                if (random.nextDouble() < writePercentage) {
                    operation = OPERATIONS[random.nextInt(2) + 1]; // 'A' or 'D'
                } else {
                    operation = OPERATIONS[0]; // 'Q'
                }
                int src = random.nextInt(MAX_NODES);
                int dest = random.nextInt(MAX_NODES);
                writer.write(operation + " " + src + " " + dest + "\n");
            }
            writer.write("F\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}