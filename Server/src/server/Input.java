package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Input {

    public static ArrayList<int[]> readInput(String inputFile) {
        ArrayList<int[]> adjList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("S")) {
                    break;
                }
                String[] tokens = line.split(" ");
                int src = Integer.parseInt(tokens[0]);
                int dest = Integer.parseInt(tokens[1]);
                adjList.add(new int[]{src, dest});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adjList;
    }
}
