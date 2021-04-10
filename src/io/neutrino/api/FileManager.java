package io.neutrino.api;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public void createDirectory(String path) {
        File dir = new File(path);
        if(dir.exists() && dir.isDirectory()) return;

        dir.mkdirs();
    }

    public void createFile(String path, String fileName) {
        createDirectory(path);
        File f = new File(path, fileName);
        if(f.exists()) return;

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(String path, String fileName) {
        return new File(path, fileName).exists();
    }
}
