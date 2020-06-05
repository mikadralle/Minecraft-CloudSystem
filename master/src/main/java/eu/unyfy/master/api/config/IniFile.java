package eu.unyfy.master.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

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
