package eu.unyfy.cloudsystem.console.api;

import eu.unyfy.cloudsystem.console.writer.ColouredWriter;
import java.io.IOException;
import jline.console.ConsoleReader;


public interface IConsole {

    String readLine() throws IOException;

    ColouredWriter getWriter();

    boolean info(final String msg);

    boolean debug(final String msg);

    boolean warn(final String msg);

    boolean error(final String msg);

    boolean clearConsole();

    boolean raw(final String msg);

    ConsoleReader getReader();

    String getPrefix();
}
