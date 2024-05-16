package com.org.example.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface GraphRmiInterface extends Remote{
    String initializeGraph(ArrayList<int[]> list) throws RemoteException;
    ArrayList<String> processBatchRequests(List<String> operations) throws RemoteException;

}
