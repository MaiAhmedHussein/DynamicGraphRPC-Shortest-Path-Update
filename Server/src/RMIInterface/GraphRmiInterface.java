package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GraphRmiInterface extends Remote{

    ArrayList<String> processBatchRequests(List<String> operations) throws RemoteException;

}
