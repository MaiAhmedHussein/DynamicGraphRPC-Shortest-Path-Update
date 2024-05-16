package server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {

    // Create a Logger instance
    public final Logger logger = Logger.getLogger(ServerLogger.class.getName());

    public ServerLogger() {
        setupLogger();
    }

    private void setupLogger() {
        try {
            // Create a FileHandler that writes log to a file named application.log
            FileHandler fileHandler = new FileHandler("application.log", true);

            // Set a simple formatter for the handler
            fileHandler.setFormatter(new SimpleFormatter());

            // Add the file handler to the logger
            logger.addHandler(fileHandler);

            // Set the logger level to capture all log messages
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    private void logMessages() {
        logger.info("This is an info message.");
        logger.warning("This is a warning message.");
        logger.severe("This is a severe message.");
        logger.fine("This is a fine message.");
        logger.finer("This is a finer message.");
        logger.finest("This is a finest message.");
    }
}
