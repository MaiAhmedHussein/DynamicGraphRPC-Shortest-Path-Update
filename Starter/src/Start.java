
import client.Client;
import server.Server;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Start {

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/system.properties"));

            String serverAddress = props.getProperty("GSP.server");
            int serverPort = Integer.parseInt(props.getProperty("GSP.server.port"));
            int numberOfNodes = Integer.parseInt(props.getProperty("GSP.numberOfnodes"));

            // Start server in a new thread
            new Thread(() -> {
                Server.main(null);
            }).start();

            // Wait a bit for the server to start
            Thread.sleep(1000);

            // Start clients
            for (int i = 0; i < numberOfNodes; i++) {
                String nodeName = "GSP.node" + i;
                String nodeAddress = props.getProperty(nodeName);
                System.out.println("starting node: " + nodeName + "with address " + nodeAddress);
                new Thread(() -> {
                    Client.main(new String[]{nodeAddress});
                }).start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
