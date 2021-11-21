package de.leantwi.service.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Bungeecords code
 */
public class ConciseFormatter extends Formatter {

  private final DateFormat date = new SimpleDateFormat("HH:mm:ss");
  private final boolean colored;

  public ConciseFormatter(boolean colored) {
    this.colored = colored;
  }

  @Override
  public String format(LogRecord record) {
    StringBuilder formatted = new StringBuilder();

    if (colored) {
      // Coloring time and log-level automatically
      ChatColor color = ChatColor.AQUA;

      if (record.getLevel().equals(Level.WARNING)) {
        color = ChatColor.RED;
      } else if (record.getLevel().equals(Level.SEVERE)) {
        color = ChatColor.DARK_RED;
      }

      // Appending color
      formatted.append(color);
    }

    formatted.append("[");
    formatted.append(date.format(record.getMillis()));
    formatted.append(" - ");
    formatted.append(record.getLevel().getLocalizedName());
    formatted.append("] ");

    if (colored) {
      formatted.append(ChatColor.RESET);
    }

    formatted.append(formatMessage(record));
    formatted.append('\n');

    if (record.getThrown() != null) {
      StringWriter writer = new StringWriter();
      record.getThrown().printStackTrace(new PrintWriter(writer));
      formatted.append(writer);
    }

    return formatted.toString();
  }
}