package de.leantwi.service.command;


public interface CommandImplementation {

    void execute(String[] strings);

    String getName();

    boolean isUsageRight(String[] strings);

    String getUsage();

}
