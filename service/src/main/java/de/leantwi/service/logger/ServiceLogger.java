package de.leantwi.service.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import jline.console.ConsoleReader;

/**
 * Bungeecords code
 */
public class ServiceLogger extends Logger {

  private final LogDispatcher dispatcher = new LogDispatcher(this);
  private final boolean systemPrint;
  private File logDirectory;

  public ServiceLogger(String name, ConsoleReader consoleReader, String logFileName, String logDirectory) {
    this(name, consoleReader, logFileName, false, logDirectory);
  }

  public ServiceLogger(String name, ConsoleReader consoleReader, String logFileName, boolean systemPrint, String logDirectory) {
    super(name, null);
    this.systemPrint = systemPrint;

    setLevel(Level.ALL);

    if (!systemPrint) {
      try {
        File logDir = new File(logDirectory);
        if (!logDir.exists()) {
          if (!logDir.mkdir()) {
            throw new RuntimeException("Could not create log dir. Please be sure to check the Filesystem permissions");
          }
        }

        FileHandler fileHandler = new FileHandler(logDirectory + File.separator + logFileName + ".log", 1 << 24, 8, true);
        fileHandler.setFormatter(new ConciseFormatter(false));
        addHandler(fileHandler);

        ColouredWriter consoleHandler = new ColouredWriter(consoleReader);
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new ConciseFormatter(true));
        addHandler(consoleHandler);
      } catch (IOException ex) {
        System.err.println("Could not register logger!");
        ex.printStackTrace();
      }
    }

    dispatcher.start();
  }

  @Override
  public void log(LogRecord record) {
    dispatcher.queue(record);
  }

  void doLog(LogRecord record) {
    if (systemPrint) {
      if (record.getLevel() == Level.SEVERE || record.getLevel() == Level.WARNING) {
        System.err.println("[" + record.getLoggerName() + "] " + record.getMessage());
      } else {
        System.out.println("[" + record.getLoggerName() + "] " + record.getMessage());
      }

      if (record.getThrown() != null) {
        record.getThrown().printStackTrace();
      }
    } else {
      super.log(record);
    }
  }

}
