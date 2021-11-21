package de.leantwi.cloudsystem.wrapper.utils.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConfigAPI {


    private final String fileName;
    private final String path;
    private String split = "";
    private final File file;

    public ConfigAPI(String path, String fileName, String split) {

        this.path = path;
        this.fileName = fileName;
        this.split = split;
        file = new File(path, fileName);

        if (path != null) {
            File f = new File(path);
            if (f.exists()) {
                f.mkdirs();
            }
        }
    }

    public boolean existsFile() {
        return file.exists();
    }

    public void createFile() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        file.delete();
    }

    public File getFile() {
        return file;
    }

    public void set(String path, Object object) {
        ArrayList<String> oldFile = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                oldFile.add(scanner.nextLine());
            }

            FileWriter fileWriter = new FileWriter(file);

            if (!(oldFile.isEmpty())) {
                for (String s : oldFile) {
                    fileWriter.write(s);
                    fileWriter.write("\n");
                }
            }

            fileWriter.write(path + this.split + object);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                String[] split = s.split(this.split);

                if (path.equalsIgnoreCase(split[0])) {
                    return split[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Integer getInt(String path) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                String[] split = s.split(this.split);
                if (path.equalsIgnoreCase(split[0])) {
                    return Integer.valueOf(split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getBoolean(String path) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                String[] split = s.split(this.split);
                if (path.equalsIgnoreCase(split[0])) {
                    return Boolean.parseBoolean(split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object get(String path) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                String[] split = s.split(this.split);
                if (path.equalsIgnoreCase(split[0])) {
                    return split[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getStringList(String path) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                String[] split = s.split(this.split);
                if (path.equalsIgnoreCase(split[0])) {
                    return Collections.singletonList(split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}



