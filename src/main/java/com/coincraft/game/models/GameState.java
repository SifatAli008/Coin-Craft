package com.coincraft.game.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks the player's game progress
 */
public class GameState {
    private String userId;
    private int currentLevel;
    private Map<Integer, Boolean> completedLevels;
    private int totalCoinsEarned;
    private int totalQuestionsAnswered;
    private int correctAnswers;
    
    public GameState() {
        this.completedLevels = new HashMap<>();
        this.currentLevel = 1;
        this.totalCoinsEarned = 0;
        this.totalQuestionsAnswered = 0;
        this.correctAnswers = 0;
    }
    
    public GameState(String userId) {
        this();
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public Map<Integer, Boolean> getCompletedLevels() {
        return completedLevels;
    }
    
    public void setCompletedLevels(Map<Integer, Boolean> completedLevels) {
        this.completedLevels = completedLevels;
    }
    
    public int getTotalCoinsEarned() {
        return totalCoinsEarned;
    }
    
    public void setTotalCoinsEarned(int totalCoinsEarned) {
        this.totalCoinsEarned = totalCoinsEarned;
    }
    
    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }
    
    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
    
    /**
     * Mark a level as completed
     */
    public void completeLevel(int levelId) {
        completedLevels.put(levelId, true);
        if (levelId >= currentLevel) {
            currentLevel = levelId + 1;
        }
    }
    
    /**
     * Check if a level is completed
     */
    public boolean isLevelCompleted(int levelId) {
        return completedLevels.getOrDefault(levelId, false);
    }
    
    /**
     * Add coins from game rewards
     */
    public void addCoins(int amount) {
        totalCoinsEarned += amount;
    }
    
    /**
     * Record a question answer
     */
    public void recordAnswer(boolean isCorrect) {
        totalQuestionsAnswered++;
        if (isCorrect) {
            correctAnswers++;
        }
    }
    
    /**
     * Get accuracy percentage
     */
    public int getAccuracyPercentage() {
        if (totalQuestionsAnswered == 0) return 0;
        return (correctAnswers * 100) / totalQuestionsAnswered;
    }
}

