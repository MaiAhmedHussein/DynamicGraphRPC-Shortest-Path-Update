package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GraphRmiInterface extends Remote{

    ArrayList<String> processBatchRequests(String clientId,List<String> operations,boolean optimized) throws RemoteException;

}
