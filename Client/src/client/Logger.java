package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class Logger {

    private String logFilePath;

    public Logger(String logDir, String num) {

        File parentDir = new File(logDir);
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create parent directory: " + parentDir);
                return;
            }
        }
         this.logFilePath = logDir + "/log" + num + ".txt";
        File logFile = new File(logFilePath);

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Failed to create logs: " + e.getMessage());
        }

    }

    public void log(String event) {
        try (FileWriter writer = new FileWriter(this.logFilePath, true)) {
            writer.write(event + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Failed to write to logs: " + e.getMessage());
        }
    }

}