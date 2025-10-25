package dev.donutquine.resources;

import android.content.res.AssetManager;

import com.daniillshei.sceditor.StageGLES;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceManager {
    public static AssetManager assetManager;
    public static boolean doesFileExist(String path) {
        try {
            assetManager.open(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static String loadString(String filename) {
        try {
            var stream = assetManager.open(filename, AssetManager.ACCESS_BUFFER);
            var bytes = new byte[stream.available()];
            stream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] loadBytes(String filename) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        try (InputStream resource = classLoader.getResourceAsStream(filename)) {
            if (resource == null) return null;

            return resource.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream loadStream(String filename) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        return classLoader.getResourceAsStream(filename);
    }
}
