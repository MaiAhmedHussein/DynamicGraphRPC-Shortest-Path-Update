package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GraphProcessor graphProcessor = new GraphProcessor();

        // Process initial graph input
        graphProcessor.processInitialGraphInput(scanner);

        // Signal readiness
        System.out.println("R");

        // Process batch requests
        graphProcessor.processBatchRequests(scanner);

        scanner.close();
    }
}
