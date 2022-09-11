package de.leantwi.cloudsystem.proxy.loader;

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

    @Getter
    public final Logger logger;
    private final MyClassloader myClassloader = new MyClassloader(new URL[0], this.getClass().getClassLoader());
    private final File LIBRARY_FOLDER = new File(System.getProperty("cloud.libraries.path"));

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


            try {

                Method method = myClassloader.getClass().getMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(myClassloader, libraryFile.toURI().toURL());

                this.logger.info("Loaded library " + libraryFile.getName());
            } catch (Exception ex) {
                this.logger.log(Level.WARNING, "Couldn't load library " + libraryFile.getName(), ex);
            }
        }
    }

    private class MyClassloader extends URLClassLoader {

        public MyClassloader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public void addURL(URL url) {
            super.addURL(url);
        }
    }

}
