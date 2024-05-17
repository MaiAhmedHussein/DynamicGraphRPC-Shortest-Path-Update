import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Start {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/system.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String arg2 = "0";

        // Start the server process
        String[] serverCommand = {"/bin/bash", "-c", "java -jar Server.jar"};
        Process serverProcess = Runtime.getRuntime().exec(serverCommand);
        handleProcessOutput(serverProcess);

        // Wait for the server to start up
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
        //Random Run
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    String[] clientCommand = {"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + 0};
                    Process clientProcess = Runtime.getRuntime().exec(clientCommand);
                    handleProcessOutput(clientProcess);
                    clientProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }

        //Performance test : Recording response time Vs frequency of requests
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    String[] clientCommand = {"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + 1};
                    Process clientProcess = Runtime.getRuntime().exec(clientCommand);
                    handleProcessOutput(clientProcess);
                    clientProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }

       //Performance test : Recording response time Vs percentage of add/delete operations
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
        for (int i = 0; i < 2; i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    String[] clientCommand = {"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + 2};
                    Process clientProcess = Runtime.getRuntime().exec(clientCommand);
                    handleProcessOutput(clientProcess);
                    clientProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all clients to finish
        }
/*
        //Performance test : Recording response time Vs number of nodes [1,15]
        for (int i = 1; i < 15; i++) {
            executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));
            for (int j = 0; j < i; j++) {
                final int nodeIndex = j;
                int finalI = i;
                executor.submit(() -> {
                    try {
                        String[] clientCommand = {"/bin/bash", "-c", "java -jar Client.jar node" + nodeIndex + " " + 3 + " " + finalI};
                        Process clientProcess = Runtime.getRuntime().exec(clientCommand);
                        handleProcessOutput(clientProcess);
                        clientProcess.waitFor();
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
        // Shutdown the executor
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(1000);
        }*/
        // Stop the server process
        serverProcess.destroy();
        serverProcess.waitFor();
        System.exit(0);
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
