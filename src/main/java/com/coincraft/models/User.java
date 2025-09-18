package com.coincraft.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User model representing a player in the CoinCraft system
 * Supports children, parents, teachers, and admin roles
 */
public class User {
    private String userId;
    private String name;
    private UserRole role;
    private int age;
    private int level;
    private int smartCoinBalance;
    private List<Task> currentTasks;
    private List<Badge> earnedBadges;
    private Avatar avatar;
    private int dailyStreaks;
    private int leaderboardRank;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    
    // Constructors
    public User() {
        this.currentTasks = new ArrayList<>();
        this.earnedBadges = new ArrayList<>();
        this.avatar = new Avatar();
        this.level = 1;
        this.smartCoinBalance = 0;
        this.dailyStreaks = 0;
        this.leaderboardRank = 0;
        this.createdAt = LocalDateTime.now();
    }
    
    public User(String userId, String name, UserRole role, int age) {
        this();
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.age = age;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getSmartCoinBalance() { return smartCoinBalance; }
    public void setSmartCoinBalance(int smartCoinBalance) { this.smartCoinBalance = smartCoinBalance; }
    
    public List<Task> getCurrentTasks() { return currentTasks; }
    public void setCurrentTasks(List<Task> currentTasks) { this.currentTasks = currentTasks; }
    
    public List<Badge> getEarnedBadges() { return earnedBadges; }
    public void setEarnedBadges(List<Badge> earnedBadges) { this.earnedBadges = earnedBadges; }
    
    public Avatar getAvatar() { return avatar; }
    public void setAvatar(Avatar avatar) { this.avatar = avatar; }
    
    public int getDailyStreaks() { return dailyStreaks; }
    public void setDailyStreaks(int dailyStreaks) { this.dailyStreaks = dailyStreaks; }
    
    public int getLeaderboardRank() { return leaderboardRank; }
    public void setLeaderboardRank(int leaderboardRank) { this.leaderboardRank = leaderboardRank; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Business methods
    public void addSmartCoins(int coins) {
        this.smartCoinBalance += coins;
    }
    
    public boolean spendSmartCoins(int coins) {
        if (this.smartCoinBalance >= coins) {
            this.smartCoinBalance -= coins;
            return true;
        }
        return false;
    }
    
    public void addTask(Task task) {
        this.currentTasks.add(task);
    }
    
    public void removeTask(Task task) {
        this.currentTasks.remove(task);
    }
    
    public void awardBadge(Badge badge) {
        if (!this.earnedBadges.contains(badge)) {
            this.earnedBadges.add(badge);
        }
    }
    
    public void incrementDailyStreak() {
        this.dailyStreaks++;
    }
    
    public void resetDailyStreak() {
        this.dailyStreaks = 0;
    }
    
    public boolean canLevelUp() {
        // Level up logic based on SmartCoins and completed tasks
        int requiredCoins = level * 100; // Progressive requirement
        return smartCoinBalance >= requiredCoins && earnedBadges.size() >= level;
    }
    
    public void levelUp() {
        if (canLevelUp()) {
            this.level++;
        }
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", level=" + level +
                ", smartCoinBalance=" + smartCoinBalance +
                '}';
    }
}
