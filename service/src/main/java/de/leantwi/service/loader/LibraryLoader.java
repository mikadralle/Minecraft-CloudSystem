package de.leantwi.service.loader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class LibraryLoader {


    private final File LIBRARY_FOLDER = new File("libraries/");
    @Getter
    public final Logger logger;

    /**
     * Loads the libraries into the classpath
     */
    public void loadLibraries() {
        if (!LIBRARY_FOLDER.exists()) {
            LIBRARY_FOLDER.mkdir();
        }
        if (!LIBRARY_FOLDER.exists() || !LIBRARY_FOLDER.isDirectory()) {
            return;
        }

        for (File libraryFile : LIBRARY_FOLDER.listFiles()) {
            if (!libraryFile.getName().endsWith(".jar")) {
                continue;
            }

            // Adding library to classpath
            try {
                URLClassLoader loader = (URLClassLoader) LibraryLoader.class.getClassLoader();
                Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                addURL.invoke(loader, libraryFile.toURI().toURL());

                this.logger.info("Loaded library " + libraryFile.getName());
            } catch (Exception ex) {
                this.logger.log(Level.WARNING, "Couldn't load library " + libraryFile.getName(), ex);
            }
        }
    }

}
