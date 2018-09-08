package Model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//code from https://stackoverflow.com/questions/20737880/java-logging-through-multiple-classes

/**
 * Logs events pertaining to changes to the state of the restaurant that occur in other classes to log.txt
 */
public class EventLogger {
    static Logger logger;
    public Handler fileHandler;
    Formatter plainText;

    /**
     * Logs text to log.txt.
     *
     * @throws IOException
     */
    private EventLogger() throws IOException {
        logger = Logger.getLogger(EventLogger.class.getName());
        fileHandler = new FileHandler("log.txt", true);
        plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        logger.addHandler(fileHandler);
    }

    /**
     * Gets or creates a new Logger depending on if one currently exists.
     *
     * @return An instance of logger class
     */
    private static Logger getLogger() {
        if (logger == null) {
            try {
                new EventLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    /**
     * Method that is called upon by other classes in order to perform the function of logging.
     *
     * @param msg Message that is to be written to log.txt.
     */
    public static void log(String msg) {
        getLogger().info(msg);
    }
}
