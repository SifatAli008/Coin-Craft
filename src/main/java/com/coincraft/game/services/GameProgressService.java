package com.coincraft.game.services;

import com.coincraft.game.models.GameState;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages game progress - saving and loading player state
 */
public class GameProgressService {
    private static GameProgressService instance;
    private final FirebaseService firebaseService;
    
    private GameProgressService() {
        this.firebaseService = FirebaseService.getInstance();
    }
    
    public static GameProgressService getInstance() {
        if (instance == null) {
            instance = new GameProgressService();
        }
        return instance;
    }
    
    /**
     * Load game state for a user
     */
    public GameState loadGameState(String userId) {
        try {
            // Try to load from Firebase
            Map<String, Object> data = firebaseService.getGameProgress(userId);
            
            if (data != null && !data.isEmpty()) {
                GameState state = new GameState(userId);
                state.setCurrentLevel((int) data.getOrDefault("currentLevel", 1));
                state.setTotalCoinsEarned((int) data.getOrDefault("totalCoinsEarned", 0));
                state.setTotalQuestionsAnswered((int) data.getOrDefault("totalQuestionsAnswered", 0));
                state.setCorrectAnswers((int) data.getOrDefault("correctAnswers", 0));
                
                // Load completed levels
                @SuppressWarnings("unchecked")
                Map<String, Boolean> completedLevelsRaw = 
                    (Map<String, Boolean>) data.getOrDefault("completedLevels", new HashMap<>());
                
                Map<Integer, Boolean> completedLevels = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : completedLevelsRaw.entrySet()) {
                    completedLevels.put(Integer.valueOf(entry.getKey()), entry.getValue());
                }
                state.setCompletedLevels(completedLevels);
                
                System.out.println("✅ Loaded game progress for user: " + userId);
                return state;
            }
        } catch (RuntimeException e) {
            System.err.println("⚠️ Error loading game state: " + e.getMessage());
        }
        
        // Return new game state if nothing found
        return new GameState(userId);
    }
    
    /**
     * Save game state for a user
     */
    public void saveGameState(GameState gameState) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("currentLevel", gameState.getCurrentLevel());
            data.put("totalCoinsEarned", gameState.getTotalCoinsEarned());
            data.put("totalQuestionsAnswered", gameState.getTotalQuestionsAnswered());
            data.put("correctAnswers", gameState.getCorrectAnswers());
            
            // Convert completed levels to String keys for Firebase
            Map<String, Boolean> completedLevelsForFirebase = new HashMap<>();
            for (Map.Entry<Integer, Boolean> entry : gameState.getCompletedLevels().entrySet()) {
                completedLevelsForFirebase.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            data.put("completedLevels", completedLevelsForFirebase);
            
            firebaseService.saveGameProgress(gameState.getUserId(), data);
            System.out.println("✅ Saved game progress for user: " + gameState.getUserId());
            
        } catch (Exception e) {
            System.err.println("❌ Error saving game state: " + e.getMessage());
        }
    }
    
    /**
     * Award coins to user's account
     */
    public void awardCoins(User user, int amount) {
        try {
            int newBalance = user.getSmartCoinBalance() + amount;
            user.setSmartCoinBalance(newBalance);
            firebaseService.saveUser(user);
            
            System.out.println("💰 Awarded " + amount + " coins to " + user.getName() + 
                             ". New balance: " + newBalance);
        } catch (Exception e) {
            System.err.println("❌ Error awarding coins: " + e.getMessage());
        }
    }
}

