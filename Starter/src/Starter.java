import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Starter{
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/system.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Start the server process
        String[] serverCommand = {"/bin/bash", "-c", "java -jar Server.jar"};
        Process serverProcess = Runtime.getRuntime().exec(serverCommand);
        handleProcessOutput(serverProcess);

        // Wait for the server to start up
        Thread.sleep(5000); // Wait for 5 seconds

        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));

        // Simulate real-world scenarios with random delays
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    simulateClientOperation(nodeIndex, 0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }

        // Performance test: Recording response time vs frequency of requests
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    simulateClientOperation(nodeIndex, 1);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }

        // Performance test: Recording response time vs percentage of add/delete operations
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    simulateClientOperation(nodeIndex, 2);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }

        // Performance test: Recording response time vs number of nodes [1,15]
        for (int i = 1; i <= 15; i++) {
            executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
            for (int j = 0; j < i; j++) {
                final int nodeIndex = j;
                int finalI = i;
                executor.submit(() -> {
                    try {
                        simulateClientOperation(nodeIndex, 3, finalI);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                // Wait for all clients to finish
            }
        }

        // Shutdown the server process
        serverProcess.destroy();
        serverProcess.waitFor();
        System.exit(0);
    }

    private static void simulateClientOperation(int nodeIndex, int operationType) throws IOException, InterruptedException {
        simulateClientOperation(nodeIndex, operationType, -1);
    }

    private static void simulateClientOperation(int nodeIndex, int operationType, int numNodes) throws IOException, InterruptedException {
        String[] clientCommand;
        if (numNodes == -1) {
            clientCommand = new String[]{"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + operationType};
        } else {
            clientCommand = new String[]{"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + operationType + " " + numNodes};
        }

        while (true) {
            Process clientProcess = Runtime.getRuntime().exec(clientCommand);
            handleProcessOutput(clientProcess);
            clientProcess.waitFor();

            // Simulate random delay between 0 and 10000 milliseconds
            int randomDelay = ThreadLocalRandom.current().nextInt(0, 10001);
            Thread.sleep(randomDelay);
        }
    }

    private static void handleProcessOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
