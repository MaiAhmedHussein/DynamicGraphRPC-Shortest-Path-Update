package client;
import rmiInterface.GraphRmiInterface;
import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        try {

            Properties props = new Properties();
            props.load(new FileInputStream("src/system.properties"));

            String serverAddress = props.getProperty("GSP.server");
            int registryPort = Integer.parseInt(props.getProperty("GSP.rmiregistry.port"));

            // Get the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress, registryPort);

            // Look up the remote object
            GraphRmiInterface stub = (GraphRmiInterface) registry.lookup("Update");
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                List<String> operations = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equals("F")) {
                        break;
                    }
                    operations.add(line);
                }
                ArrayList<String> res = stub.processBatchRequests(operations);
                for (String r : res){
                    System.out.println(r);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}