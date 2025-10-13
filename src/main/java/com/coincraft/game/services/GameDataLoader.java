package com.coincraft.game.services;

import java.io.InputStreamReader;
import java.io.Reader;

import com.coincraft.game.models.GameLevel;
import com.google.gson.Gson;

/**
 * Loads game level data from JSON files
 */
public class GameDataLoader {
    private static GameDataLoader instance;
    private final Gson gson;
    
    private GameDataLoader() {
        this.gson = new Gson();
    }
    
    public static GameDataLoader getInstance() {
        if (instance == null) {
            instance = new GameDataLoader();
        }
        return instance;
    }
    
    /**
     * Load a specific level from JSON file
     */
    public GameLevel loadLevel(int levelId) {
        try {
            String fileName = "/game/levels/level_" + levelId + ".json";
            Reader reader = new InputStreamReader(
                getClass().getResourceAsStream(fileName)
            );
            
            GameLevel level = gson.fromJson(reader, GameLevel.class);
            reader.close();
            
            System.out.println("✅ Loaded Level " + levelId + ": " + level.getTitle());
            return level;
            
        } catch (Exception e) {
            System.err.println("❌ Error loading level " + levelId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Check if a level exists
     */
    public boolean levelExists(int levelId) {
        try {
            String fileName = "/game/levels/level_" + levelId + ".json";
            return getClass().getResourceAsStream(fileName) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get total number of available levels
     */
    public int getTotalLevels() {
        int count = 0;
        while (levelExists(count + 1)) {
            count++;
        }
        return count;
    }
}

