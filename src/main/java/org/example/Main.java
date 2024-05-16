package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void initializeGraph(String filename, int numNodes) {
        Random random = new Random();
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < numNodes; i++) {
                for (int j = i + 1; j < numNodes; j++) {
                    int weight = random.nextInt(10) + 1; // Random weight between 1 and 10
                    writer.write(i + " " + j + " " + weight + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static void main(String[] args) {
            initializeGraph("graph.txt", 100);
            GraphProcessor graphProcessor = new GraphProcessor();

            // Process initial graph input
            try (Scanner scanner = new Scanner(new File("graph.txt"))) {
                graphProcessor.processInitialGraphInput(scanner);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Signal readiness
            System.out.println("R");

            // Generate batch files
            for (int i = 0; i < 10; i++) { // Generate 10 batch files
                BatchGenerator.generateBatch("batch" + i + ".txt", 0.2); // 20% write operations
            }

            // Process batch files
            for (int i = 0; i < 10; i++) {
                try (Scanner scan = new Scanner(new File("batch" + i + ".txt"))) {
                    graphProcessor.processBatchRequests(scan);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

}
