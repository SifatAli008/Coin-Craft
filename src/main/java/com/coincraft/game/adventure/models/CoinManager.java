package com.coincraft.game.adventure.models;

import com.coincraft.models.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Coin Management System for Quiz Rewards
 * Handles coin increases for correct answers and decreases for wrong answers
 * Integrates with User model and updates dashboard
 */
public class CoinManager {
    private static User currentUser;
    private static final List<CoinTransaction> transactionHistory = new ArrayList<>();
    
    public static class CoinTransaction {
        private final String description;
        private final int amount;
        private final long timestamp;
        private final String type; // "QUIZ_CORRECT", "QUIZ_WRONG", "STARTING_BONUS", etc.
        
        public CoinTransaction(String description, int amount, String type) {
            this.description = description;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
            this.type = type;
        }
        
        public String getDescription() { return description; }
        public int getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
        public String getType() { return type; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %+d coins", type, description, amount);
        }
    }
    
    /**
     * Initialize coin manager with current user
     */
    public static void initialize(User user) {
        currentUser = user;
        System.out.println("💰 CoinManager initialized for user: " + user.getName() + " (Balance: " + user.getSmartCoinBalance() + ")");
    }
    
    /**
     * Add coins for correct quiz answers
     */
    public static int addCoinsForCorrectAnswer(int questionNumber, String questionTopic) {
        if (currentUser == null) {
            System.out.println("⚠️ CoinManager not initialized!");
            return 0;
        }
        
        int coinsEarned = 15; // Base reward for correct answer
        int newBalance = currentUser.getSmartCoinBalance() + coinsEarned;
        currentUser.setSmartCoinBalance(newBalance);
        
        String description = String.format("Correct answer on Q%d (%s)", questionNumber, questionTopic);
        transactionHistory.add(new CoinTransaction(description, coinsEarned, "QUIZ_CORRECT"));
        
        System.out.println("✅ +" + coinsEarned + " coins for correct answer! New balance: " + newBalance);
        return coinsEarned;
    }
    
    /**
     * Remove coins for wrong quiz answers
     */
    public static int removeCoinsForWrongAnswer(int questionNumber, String questionTopic) {
        if (currentUser == null) {
            System.out.println("⚠️ CoinManager not initialized!");
            return 0;
        }
        
        int coinsLost = 8; // Penalty for wrong answer
        int newBalance = Math.max(0, currentUser.getSmartCoinBalance() - coinsLost); // Don't go below 0
        currentUser.setSmartCoinBalance(newBalance);
        
        String description = String.format("Wrong answer on Q%d (%s)", questionNumber, questionTopic);
        transactionHistory.add(new CoinTransaction(description, -coinsLost, "QUIZ_WRONG"));
        
        System.out.println("❌ -" + coinsLost + " coins for wrong answer! New balance: " + newBalance);
        return coinsLost;
    }
    
    /**
     * Add bonus coins for completing quiz
     */
    public static int addQuizCompletionBonus(int correctAnswers, int totalQuestions) {
        if (currentUser == null) {
            System.out.println("⚠️ CoinManager not initialized!");
            return 0;
        }
        
        int bonus = 0;
        if (correctAnswers == totalQuestions) {
            bonus = 50; // Perfect score bonus
        } else if (correctAnswers >= totalQuestions * 0.8) {
            bonus = 30; // 80%+ score bonus
        } else if (correctAnswers >= totalQuestions * 0.6) {
            bonus = 15; // 60%+ score bonus
        }
        
        if (bonus > 0) {
            int newBalance = currentUser.getSmartCoinBalance() + bonus;
            currentUser.setSmartCoinBalance(newBalance);
            String description = String.format("Quiz completion bonus (%d/%d correct)", correctAnswers, totalQuestions);
            transactionHistory.add(new CoinTransaction(description, bonus, "QUIZ_BONUS"));
            System.out.println("🎉 +" + bonus + " coins quiz completion bonus! New balance: " + newBalance);
        }
        
        return bonus;
    }
    
    /**
     * Get current coin balance
     */
    public static int getTotalCoins() {
        return currentUser != null ? currentUser.getSmartCoinBalance() : 0;
    }
    
    /**
     * Set coin balance (for testing or reset)
     */
    public static void setTotalCoins(int coins) {
        if (currentUser != null) {
            currentUser.setSmartCoinBalance(Math.max(0, coins));
            System.out.println("💰 Coin balance set to: " + currentUser.getSmartCoinBalance());
        }
    }
    
    /**
     * Get transaction history
     */
    public static List<CoinTransaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    /**
     * Get recent transactions (last N)
     */
    public static List<CoinTransaction> getRecentTransactions(int count) {
        int start = Math.max(0, transactionHistory.size() - count);
        return new ArrayList<>(transactionHistory.subList(start, transactionHistory.size()));
    }
    
    /**
     * Get quiz score percentage
     */
    public static int getQuizScore() {
        int correctAnswers = getCorrectAnswers();
        int wrongAnswers = getWrongAnswers();
        int totalAnswers = correctAnswers + wrongAnswers;
        
        if (totalAnswers == 0) return 0;
        return (int) Math.round((double) correctAnswers / totalAnswers * 100);
    }
    
    /**
     * Get number of correct answers
     */
    public static int getCorrectAnswers() {
        return (int) transactionHistory.stream()
            .filter(t -> "QUIZ_CORRECT".equals(t.getType()))
            .count();
    }
    
    /**
     * Get number of wrong answers
     */
    public static int getWrongAnswers() {
        return (int) transactionHistory.stream()
            .filter(t -> "QUIZ_WRONG".equals(t.getType()))
            .count();
    }
    
    /**
     * Get total coins earned from correct answers
     */
    public static int getCoinsEarned() {
        return transactionHistory.stream()
            .filter(t -> "QUIZ_CORRECT".equals(t.getType()))
            .mapToInt(CoinTransaction::getAmount)
            .sum();
    }
    
    /**
     * Get total coins lost from wrong answers
     */
    public static int getCoinsLost() {
        return Math.abs(transactionHistory.stream()
            .filter(t -> "QUIZ_WRONG".equals(t.getType()))
            .mapToInt(CoinTransaction::getAmount)
            .sum());
    }
    
    /**
     * Get coin balance summary
     */
    public static String getCoinSummary() {
        if (currentUser == null) {
            return "💰 CoinManager not initialized!";
        }
        
        int correctAnswers = getCorrectAnswers();
        int wrongAnswers = getWrongAnswers();
        int totalEarned = transactionHistory.stream()
            .filter(t -> t.getAmount() > 0)
            .mapToInt(CoinTransaction::getAmount)
            .sum();
        int totalLost = Math.abs(transactionHistory.stream()
            .filter(t -> t.getAmount() < 0)
            .mapToInt(CoinTransaction::getAmount)
            .sum());
        
        return String.format("""
            💰 COIN SUMMARY:
            Current Balance: %d SmartCoins
            Correct Answers: %d (+%d coins)
            Wrong Answers: %d (-%d coins)
            Total Earned: %d coins
            Total Lost: %d coins
            """, 
            currentUser.getSmartCoinBalance(), correctAnswers, correctAnswers * 15, 
            wrongAnswers, wrongAnswers * 8, totalEarned, totalLost);
    }
}
