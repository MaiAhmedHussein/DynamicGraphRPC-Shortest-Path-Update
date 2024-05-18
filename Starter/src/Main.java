import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FilenameFilter;
public class Main {
    public static void main(String[] args) {
        Map<Integer, Long[]> averageResponseTimes = calculateAverageResponseTimes();
        for (Map.Entry<Integer, Long[]> entry : averageResponseTimes.entrySet()) {
            System.out.println("Number of nodes: " + entry.getKey() + ", Average response time: " + Arrays.toString(entry.getValue()) + " ns");
        }
    }
    public static Map<Integer, Long[]> calculateAverageResponseTimes() {
        Map<Integer, Long[]> averageResponseTimes = new HashMap<>();
        Pattern patternOptimized = Pattern.compile("Average response time for optimized variant: (\\d+) ns");
        Pattern patternNonOptimized = Pattern.compile("Average response time for not optimized variant: (\\d+) ns");

        for (int i = 1; i <= 15; i++) { // Replace 15 with the actual maximum number of nodes
            long totalResponseTimeOptimized = 0;
            long totalResponseTimeNonOptimized = 0;
            int count = 0;

            for (int j = 0; j < i; j++) {
                String logFileDir = "clientLogs/performancetests/responseTimeVsNumberOfNodes/";
                String logFileNamePattern = "node" + j + "_" + i + "_";

                File dir = new File(logFileDir);
                File[] matchingFiles = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.startsWith(logFileNamePattern) && !name.endsWith("generated_batch");
                    }
                });

                for (File fileOrDir : matchingFiles) {
                    File[] filesToProcess;
                    if (fileOrDir.isDirectory()) {
                        // If it's a directory, list its files
                        filesToProcess = fileOrDir.listFiles();
                    } else {
                        // If it's a file, process only this file
                        filesToProcess = new File[] { fileOrDir };
                    }

                    for (File file : filesToProcess) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                Matcher matcherOptimized = patternOptimized.matcher(line);
                                if (matcherOptimized.find()) {
                                    long responseTime = Long.parseLong(matcherOptimized.group(1));
                                    totalResponseTimeOptimized += responseTime;
                                }
                                Matcher matcherNonOptimized = patternNonOptimized.matcher(line);
                                if (matcherNonOptimized.find()) {
                                    long responseTime = Long.parseLong(matcherNonOptimized.group(1));
                                    totalResponseTimeNonOptimized += responseTime;
                                }
                                count++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (count > 0) {
                averageResponseTimes.put(i, new Long[] {totalResponseTimeNonOptimized / count, totalResponseTimeOptimized / count});
            }
        }

        return averageResponseTimes;
    }
}
