package de.leantwi.service;

import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.service.command.CommandHandler;
import de.leantwi.service.loader.LibraryLoader;
import de.leantwi.service.logger.LoggingOutputStream;
import de.leantwi.service.logger.ServiceLogger;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jline.console.ConsoleReader;
import lombok.Getter;

@Getter
public abstract class Service {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Logger logger;
    private CloudSystemInit cloudSystemInit;
    private final CommandHandler commandHandler;
    private LibraryLoader libraryLoader;

    public Service() {


        final long timeStamp = System.currentTimeMillis();
        commandHandler = new CommandHandler(this);
        registerCommands();
        this.cloudSystemInit = new CloudSystemInit();
        ConsoleReader consoleReader = null;
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setExpandEvents(false);

            ConsoleReader finalConsoleReader = consoleReader;

            this.logger = new ServiceLogger("cloud", consoleReader, "cloud", "log/");
            //init class library
            this.libraryLoader = new LibraryLoader(this.logger);
            // load all libraries in the folder libraries
            this.libraryLoader.loadLibraries();

            this.executorService.execute(() -> {

                while (!Thread.currentThread().isInterrupted()) {

                    String line = null;

                    try {
                        line = finalConsoleReader.readLine("> ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (line != null) {
                        commandHandler.handleInput(line);
                    }
                }
            });


            onBootstrap();


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    getLogger().info("§aShutting down...");
                    onShutdown();
                    logger.info("§aInitialized in " + (System.currentTimeMillis() - timeStamp) + "ms. Type \"help\" for help!");
                    finalConsoleReader.getTerminal().restore();

                    logger.info("§aGood bye...");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, "CloudSystem Shutdown Thread"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));


    }


    public abstract void onBootstrap();

    public abstract void onShutdown();

    public abstract void registerCommands();


}
