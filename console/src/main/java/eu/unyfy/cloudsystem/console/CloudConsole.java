package eu.unyfy.cloudsystem.console;

import com.google.common.base.Preconditions;
import eu.unyfy.cloudsystem.console.api.IConsole;
import eu.unyfy.cloudsystem.console.console.Console;
import eu.unyfy.cloudsystem.console.writer.ColouredWriter;
import jline.console.ConsoleReader;
import org.jetbrains.annotations.NotNull;

public final class CloudConsole {

  private static CloudConsole instance;

  private static ConsoleReader consoleReader;
  private static ColouredWriter colouredWriter;

  private final String prefix;

  public CloudConsole(final String prefix) {

    Preconditions.checkNotNull(prefix, "The prefix can not be null!");
    Preconditions.checkArgument(!prefix.isEmpty(), "The prefix can not be empty!");

    instance = this;

    this.prefix = prefix;

    try {
      consoleReader = new ConsoleReader();
      colouredWriter = new ColouredWriter(consoleReader);
    } catch (final Exception exception) {
      exception.printStackTrace();
    }
  }

  public static CloudConsole getInstance() {
    return instance;
  }

  public IConsole console(@NotNull final Class aClass) {
    return new Console(prefix, aClass, consoleReader, colouredWriter);
  }
}
