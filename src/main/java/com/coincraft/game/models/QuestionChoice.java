package com.coincraft.game.models;

/**
 * Represents a single choice/answer option for a question
 */
public class QuestionChoice {
    private String text;
    private boolean isCorrect;
    private int reward;  // Coins earned if this choice is selected and correct
    private int penalty; // Coins lost if this choice is selected and wrong
    
    public QuestionChoice() {
        // Default constructor for JSON deserialization
    }
    
    public QuestionChoice(String text, boolean isCorrect, int reward, int penalty) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.reward = reward;
        this.penalty = penalty;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    
    public int getReward() {
        return reward;
    }
    
    public void setReward(int reward) {
        this.reward = reward;
    }
    
    public int getPenalty() {
        return penalty;
    }
    
    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}

