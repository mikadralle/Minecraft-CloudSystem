package eu.unyfy.cloudsystem.console.console;

import com.google.common.base.Preconditions;
import eu.unyfy.cloudsystem.console.api.IConsole;
import eu.unyfy.cloudsystem.console.writer.ColouredWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import jline.console.ConsoleReader;


public final class Console implements IConsole {

  private final Class aClass;
  private final ConsoleReader consoleReader;
  private final ColouredWriter colouredWriter;

  private final String prefix;

  public Console(final String prefix, final Class aClass, final ConsoleReader consoleReader, final ColouredWriter colouredWriter) {

    Preconditions.checkNotNull(aClass, "The aClass can not be null!");

    Preconditions.checkNotNull(consoleReader, "The consoleReader can not be null!");

    Preconditions.checkNotNull(colouredWriter, "The colouredWriter can not be null!");

    this.prefix = prefix;

    this.aClass = aClass;
    this.consoleReader = consoleReader;
    this.colouredWriter = colouredWriter;
  }

  @Override
  public String readLine() throws IOException {
    return consoleReader.readLine();
  }

  @Override
  public ColouredWriter getWriter() {
    return colouredWriter;
  }

  @Override
  public boolean info(String message) {

    Preconditions.checkNotNull(message, "The message can not be null!");

    message = "§9["
        + "§9%time%"
        + " INFO]§8: §7"
        + message.replaceAll("\n", System.getProperty("line.separator"));

    raw(message);

    return true;
  }

  @Override
  public boolean debug(String message) {

    Preconditions.checkNotNull(message, "The message can not be null!");

    message = "§e["
        + aClass.getName()
        + " §8> §e%time%"
        + " DEBUG]§8: §7"
        + message;

    raw(message);

    return true;
  }

  @Override
  public boolean warn(String message) {

    Preconditions.checkNotNull(message, "The message can not be null!");

    message = "§c["
        + "§c%time%"
        + " WARN]§8: §7"
        + message;

    raw(message);

    return true;
  }

  @Override
  public boolean error(String message) {

    Preconditions.checkNotNull(message, "The message can not be null!");

    message = "§4["
        + aClass.getName()
        + " §8> §4%time%"
        + " ERROR]§8: §7"
        + message;

    raw(message);

    return true;
  }

  @Override
  public boolean clearConsole() {
    return true;
  }

  @Override
  public boolean raw(String message) {

    Preconditions.checkNotNull(message, "The message can not be null!");

    colouredWriter.print(message
        .replaceAll("%time%", new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()))
        .replaceAll("\n", System.getProperty("line.separator"))
        + System.getProperty("line.separator")
        + this.prefix);

    colouredWriter.flush();

    return true;
  }

  @Override
  public ConsoleReader getReader() {
    return consoleReader;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }
}
