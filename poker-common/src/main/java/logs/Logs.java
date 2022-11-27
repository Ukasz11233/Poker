package logs;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logs {
    private Logs() {
        throw new IllegalStateException("logs - utility class");
    }
    private static final Logger logger = Logger.getAnonymousLogger();

    public static void debug(String msg) {logger.log(Level.WARNING, "DEBUG {0}",  msg);}
    public static void info(String msg) {logger.log(Level.INFO, "INFO {0}",  msg);}

}
