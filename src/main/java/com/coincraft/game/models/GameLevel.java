package com.coincraft.game.models;

import java.util.List;

/**
 * Represents a game level in the financial literacy adventure
 */
public class GameLevel {
    private int levelId;
    private String title;
    private String description;
    private String npcName;
    private String npcEmoji;
    private String npcImagePath; // Path to character sprite image
    private String npcGreeting;
    private String npcFarewell;
    private String backgroundStyle; // CSS style for background
    private List<Question> questions;
    private int completionReward;
    private int nextLevel;
    
    public GameLevel() {
        // Default constructor for JSON deserialization
    }
    
    public int getLevelId() {
        return levelId;
    }
    
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNpcName() {
        return npcName;
    }
    
    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }
    
    public String getNpcEmoji() {
        return npcEmoji;
    }
    
    public void setNpcEmoji(String npcEmoji) {
        this.npcEmoji = npcEmoji;
    }
    
    public String getNpcImagePath() {
        return npcImagePath;
    }
    
    public void setNpcImagePath(String npcImagePath) {
        this.npcImagePath = npcImagePath;
    }
    
    public String getNpcGreeting() {
        return npcGreeting;
    }
    
    public void setNpcGreeting(String npcGreeting) {
        this.npcGreeting = npcGreeting;
    }
    
    public String getNpcFarewell() {
        return npcFarewell;
    }
    
    public void setNpcFarewell(String npcFarewell) {
        this.npcFarewell = npcFarewell;
    }
    
    public String getBackgroundStyle() {
        return backgroundStyle;
    }
    
    public void setBackgroundStyle(String backgroundStyle) {
        this.backgroundStyle = backgroundStyle;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public int getCompletionReward() {
        return completionReward;
    }
    
    public void setCompletionReward(int completionReward) {
        this.completionReward = completionReward;
    }
    
    public int getNextLevel() {
        return nextLevel;
    }
    
    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
    }
}

