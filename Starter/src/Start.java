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
        String arg3 = "0";

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



        // Initialize the executor for client processes
        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("GSP.numberOfnodes")));

        for (int i = 0; i < Integer.parseInt(props.getProperty("GSP.numberOfnodes")); i++) {
            final int nodeIndex = i;
            executor.submit(() -> {
                try {
                    String[] clientCommand = {"/bin/bash", "-c", "java -jar /home/mai/DynamicGraphRPC-Shortest-Path-Update/Starter/Client.jar " + nodeIndex + " " + arg2 + " " + arg3};
                    Process clientProcess = Runtime.getRuntime().exec(clientCommand);
                    handleProcessOutput(clientProcess);
                    clientProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        /* With semaphore
        * // Create a Semaphore with a single permit
Semaphore semaphore = new Semaphore(1);

for (int i = 0; i < Integer.parseInt(props.getProperty("GSP.numberOfnodes")); i++) {
    final int nodeIndex = i;
    executor.submit(() -> {
        try {
            // Acquire the semaphore before accessing the shared resource
            semaphore.acquire();

            String[] clientCommand = {"/bin/bash", "-c", "java -jar /home/mai/DynamicGraphRPC-Shortest-Path-Update/Starter/Client.jar " + nodeIndex + " " + arg2 + " " + arg3};
            Process clientProcess = Runtime.getRuntime().exec(clientCommand);
            handleProcessOutput(clientProcess);
            clientProcess.waitFor();

            // Release the semaphore after accessing the shared resource
            semaphore.release();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    });
}*/
        // Shutdown the executor
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(1000);
        }

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
