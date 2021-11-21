package de.leantwi.service.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

/**
 * Bungeecords code
 */
public class LogDispatcher extends Thread {

  private final ServiceLogger logger;
  private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

  public LogDispatcher(ServiceLogger logger) {
    super(logger.getName() + " Logger Thread");
    this.logger = logger;
  }

  @Override
  public void run() {
    while (!isInterrupted()) {
      LogRecord record;

      try {
        record = queue.take();
      } catch (InterruptedException ex) {
        continue;
      }

      logger.doLog(record);
    }

    for (LogRecord logRecord : queue) {
      logger.doLog(logRecord);
    }
  }

  public void queue(LogRecord record) {
    if (!isInterrupted()) {
      queue.add(record);
    }
  }
}