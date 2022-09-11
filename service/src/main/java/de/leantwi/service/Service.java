package de.leantwi.service;

import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.service.command.CommandHandler;
import de.leantwi.service.config.IniFile;
import de.leantwi.service.loader.LibraryLoader;
import de.leantwi.service.logger.LoggingOutputStream;
import de.leantwi.service.logger.ServiceLogger;
import jline.console.ConsoleReader;
import lombok.Getter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public abstract class Service {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Logger logger;
    private final CommandHandler commandHandler;
    private LibraryLoader libraryLoader;
    private final IniFile configAPI;
    private CloudSystemInit cloudSystemInit;

    public Service() {

        System.out.println("Debug-1");
        this.configAPI = new IniFile("database.ini");
        loadConfig();
        final long timeStamp = System.currentTimeMillis();
        commandHandler = new CommandHandler(this);
        System.out.println("Debug-2");
        registerCommands();

        ConsoleReader consoleReader = null;
        try {
            System.out.println("Debug-3");
            consoleReader = new ConsoleReader();
            consoleReader.setExpandEvents(false);

            ConsoleReader finalConsoleReader = consoleReader;
            System.out.println("Debug-4");
            this.logger = new ServiceLogger("cloud", consoleReader, "cloud", "log/");
            //init class library
            System.out.println("Debug-5");
            this.libraryLoader = new LibraryLoader(this.logger);
            // load all libraries in the folder libraries
            System.out.println("Debug-6");
            this.libraryLoader.loadLibraries();
            sleep();
            System.out.println("Debug-7");
            RedisData redisData = new RedisData(
                    this.configAPI.getProperty("redis.hostname"),
                    this.configAPI.getProperty("redis.password"),
                    Integer.parseInt(this.configAPI.getProperty("redis.port")),
                    Integer.parseInt(this.configAPI.getProperty("redis.databaseID")));
            System.out.println("Debug-8");
            MongoDBData mongoDBData = new MongoDBData(
                    this.configAPI.getProperty("mongoDB.hostname"),
                    this.configAPI.getProperty("mongoDB.password"),
                    this.configAPI.getProperty("mongoDB.username"),
                    this.configAPI.getProperty("mongoDB.authDB"),
                    this.configAPI.getProperty("mongoDB.defaultDB"),
                    Integer.parseInt(this.configAPI.getProperty("mongoDB.port")));

            NatsData natsData = new NatsData(
                    this.configAPI.getProperty("nats.hostname"),
                    this.configAPI.getProperty("nats.token"),
                    Integer.parseInt(this.configAPI.getProperty("nats.port")));
            System.out.println("Debug-9");
            this.cloudSystemInit = new CloudSystemInit(redisData, mongoDBData, natsData, logger);
            System.out.println("Debug-10");
            this.executorService.execute(() -> {
                System.out.println("Debug-11");
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Debug-12");
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

            System.out.println("Debug-13");
            onBootstrap();
            System.out.println("Debug-14");

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

    private void loadConfig() {

        if (this.configAPI.isEmpty()) {

            //redis connector configuration //
            this.configAPI.setProperty("redis.hostname", "127.0.0.1");
            this.configAPI.setProperty("redis.port", "6379");
            this.configAPI.setProperty("redis.password", "password");
            this.configAPI.setProperty("redis.databaseID", "7");

            //mongdb connector configuration //
            this.configAPI.setProperty("mongoDB.hostname", "127.0.0.1");
            this.configAPI.setProperty("mongoDB.username", "admin");
            this.configAPI.setProperty("mongoDB.password", "password");
            this.configAPI.setProperty("mongoDB.authDB", "admin");
            this.configAPI.setProperty("mongoDB.defaultDB", "cloud");
            this.configAPI.setProperty("mongoDB.port", "27017");

            // nats.io connector configuration
            this.configAPI.setProperty("nats.hostname", "127.0.0.0.1");
            this.configAPI.setProperty("nats.port", "4222");
            this.configAPI.setProperty("nats.token", "token");

            this.configAPI.saveToFile();
        }
    }

    private void sleep() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
