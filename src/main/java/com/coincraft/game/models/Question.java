package com.coincraft.game.models;

import java.util.List;

/**
 * Represents a question in the financial literacy game
 */
public class Question {
    private int id;
    private String text;
    private List<QuestionChoice> choices;
    private String explanation;
    
    public Question() {
        // Default constructor for JSON deserialization
    }
    
    public Question(int id, String text, List<QuestionChoice> choices, String explanation) {
        this.id = id;
        this.text = text;
        this.choices = choices;
        this.explanation = explanation;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public List<QuestionChoice> getChoices() {
        return choices;
    }
    
    public void setChoices(List<QuestionChoice> choices) {
        this.choices = choices;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    /**
     * Get the correct choice from this question
     */
    public QuestionChoice getCorrectChoice() {
        return choices.stream()
                .filter(QuestionChoice::isCorrect)
                .findFirst()
                .orElse(null);
    }
}

