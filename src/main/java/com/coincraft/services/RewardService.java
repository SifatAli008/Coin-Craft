package com.coincraft.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;

/**
 * Service for handling reward distribution and user balance management
 */
public class RewardService {
    private static final Logger LOGGER = Logger.getLogger(RewardService.class.getName());
    private static RewardService instance;
    private FirebaseService firebaseService;
    
    private RewardService() {
        this.firebaseService = FirebaseService.getInstance();
    }
    
    public static synchronized RewardService getInstance() {
        if (instance == null) {
            instance = new RewardService();
        }
        return instance;
    }
    
    /**
     * Award coins to a user for completing a task
     */
    public boolean awardCoinsForTask(User user, Task task) {
        if (user == null || task == null) {
            LOGGER.warning("Cannot award coins: user or task is null");
            return false;
        }
        
        if (task.getValidationStatus() != ValidationStatus.APPROVED) {
            LOGGER.warning("Cannot award coins: task is not approved");
            return false;
        }
        
        if (!task.isCompleted()) {
            LOGGER.warning("Cannot award coins: task is not completed");
            return false;
        }
        
        int rewardAmount = calculateRewardAmount(task);
        
        try {
            // Update user balance
            int currentBalance = user.getSmartCoinBalance();
            int newBalance = currentBalance + rewardAmount;
            user.setSmartCoinBalance(newBalance);
            
            // Add experience points based on task difficulty and type
            int experienceGained = calculateExperiencePoints(task);
            user.setExperiencePoints(user.getExperiencePoints() + experienceGained);
            
            // Check for level up
            int newLevel = calculateLevel(user.getExperiencePoints());
            if (newLevel > user.getLevel()) {
                user.setLevel(newLevel);
                LOGGER.info("User " + user.getName() + " leveled up to level " + newLevel);
            }
            
            // Save updated user to Firebase
            firebaseService.saveUser(user);
            
            LOGGER.info("Awarded " + rewardAmount + " SmartCoins and " + experienceGained + 
                       " XP to user " + user.getName() + " for task " + task.getTitle());
            
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to award coins to user " + user.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Calculate reward amount based on task properties
     */
    private int calculateRewardAmount(Task task) {
        int baseReward = task.getRewardCoins();
        int difficultyMultiplier = task.getDifficultyLevel();
        
        // Apply difficulty multiplier
        int finalReward = baseReward * difficultyMultiplier;
        
        // Bonus for completing on time
        if (task.getDeadline() != null && task.getCompletedAt() != null) {
            if (task.getCompletedAt().isBefore(task.getDeadline())) {
                finalReward = (int) (finalReward * 1.2); // 20% bonus for early completion
            }
        }
        
        return Math.max(1, finalReward); // Minimum 1 coin
    }
    
    /**
     * Calculate experience points based on task properties
     */
    private int calculateExperiencePoints(Task task) {
        int baseXP = task.getRewardCoins(); // Base XP equals reward coins
        int difficultyBonus = task.getDifficultyLevel() * 5; // 5 XP per difficulty level
        int typeBonus = getTypeBonus(task.getType());
        
        return baseXP + difficultyBonus + typeBonus;
    }
    
    /**
     * Get bonus XP based on task type
     */
    private int getTypeBonus(com.coincraft.models.TaskType type) {
        return switch (type) {
            case LEARNING -> 10; // Learning tasks give extra XP
            case CHALLENGE -> 8;
            case QUEST -> 6;
            case CREATIVE -> 5;
            case PHYSICAL -> 4;
            case SOCIAL -> 3;
            case CHORE -> 2;
            case DONATION -> 1;
        };
    }
    
    /**
     * Calculate user level based on experience points
     */
    private int calculateLevel(int experiencePoints) {
        // Simple leveling system: 100 XP per level
        return Math.max(1, (experiencePoints / 100) + 1);
    }
    
    /**
     * Award bonus coins for streaks or special achievements
     */
    public boolean awardBonusCoins(User user, int bonusAmount, String reason) {
        if (user == null || bonusAmount <= 0) {
            return false;
        }
        
        try {
            int currentBalance = user.getSmartCoinBalance();
            user.setSmartCoinBalance(currentBalance + bonusAmount);
            
            firebaseService.saveUser(user);
            
            LOGGER.info("Awarded " + bonusAmount + " bonus SmartCoins to user " + 
                       user.getName() + " for: " + reason);
            
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to award bonus coins to user " + user.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deduct coins from user (for purchases, penalties, etc.)
     */
    public boolean deductCoins(User user, int amount, String reason) {
        if (user == null || amount <= 0) {
            return false;
        }
        
        int currentBalance = user.getSmartCoinBalance();
        if (currentBalance < amount) {
            LOGGER.warning("Insufficient balance for user " + user.getName() + 
                          ": has " + currentBalance + ", needs " + amount);
            return false;
        }
        
        try {
            user.setSmartCoinBalance(currentBalance - amount);
            firebaseService.saveUser(user);
            
            LOGGER.info("Deducted " + amount + " SmartCoins from user " + 
                       user.getName() + " for: " + reason);
            
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to deduct coins from user " + user.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get user's current balance
     */
    public int getUserBalance(User user) {
        return user != null ? user.getSmartCoinBalance() : 0;
    }
    
    /**
     * Check if user has sufficient balance for a transaction
     */
    public boolean hasSufficientBalance(User user, int amount) {
        return getUserBalance(user) >= amount;
    }
    
    /**
     * Get reward summary for a completed task
     */
    public RewardSummary getRewardSummary(Task task) {
        if (task == null || task.getValidationStatus() != ValidationStatus.APPROVED) {
            return null;
        }
        
        int coins = calculateRewardAmount(task);
        int experience = calculateExperiencePoints(task);
        
        return new RewardSummary(coins, experience, task.getDifficultyLevel());
    }
    
    /**
     * Data class for reward summary information
     */
    public static class RewardSummary {
        private final int coins;
        private final int experience;
        private final int difficultyLevel;
        
        public RewardSummary(int coins, int experience, int difficultyLevel) {
            this.coins = coins;
            this.experience = experience;
            this.difficultyLevel = difficultyLevel;
        }
        
        public int getCoins() { return coins; }
        public int getExperience() { return experience; }
        public int getDifficultyLevel() { return difficultyLevel; }
        
        @Override
        public String toString() {
            return String.format("RewardSummary{coins=%d, experience=%d, difficulty=%d}", 
                               coins, experience, difficultyLevel);
        }
    }
}
