package de.leantwi.cloudsystem.proxy.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@Getter
public class IniFile extends Properties {

    private final long VERSION_ID = 2495627423L;
    private final File dest;

    public IniFile(File dest) {
        if (dest.exists()) {
            loadFromFile(dest);
        }
        this.dest = dest;
    }

    public IniFile(String dest) {
        this(new File(dest));
    }

    public void addDefault(String key, String value) {
        if (!containsKey(key)) {
            setProperty(key, value);
            saveToFile();
        }
    }

    public boolean saveToFile() {
        AtomicBoolean success = new AtomicBoolean(false);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dest))) {
            if (!dest.exists()) {
                success.set(dest.createNewFile());
            }
            store(writer, "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return success.get();
    }

    public void loadFromFile(File file) {
        clear();
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            load(reader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
