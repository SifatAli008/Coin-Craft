package com.coincraft.engine.save;

import java.nio.file.*;
import java.io.*;

/**
 * Simple JSON-based save manager
 */
public class SaveManager {
    private final Path saveDir;

    public SaveManager() {
        this.saveDir = Paths.get(System.getProperty("user.home"), ".coincraft", "saves");
        try {
            Files.createDirectories(saveDir);
        } catch (IOException e) {
            System.err.println("❌ Failed to create save directory: " + e.getMessage());
        }
    }

    public boolean saveString(String slot, String data) {
        try {
            Path file = saveDir.resolve(slot + ".json");
            Files.writeString(file, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Failed to save: " + e.getMessage());
            return false;
        }
    }

    public String loadString(String slot) {
        try {
            Path file = saveDir.resolve(slot + ".json");
            if (Files.exists(file)) {
                return Files.readString(file);
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + e.getMessage());
        }
        return null;
    }
}


