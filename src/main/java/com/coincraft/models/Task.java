package com.coincraft.models;

import java.time.LocalDateTime;

/**
 * Task model representing chores, challenges, quests, and donations in CoinCraft
 */
public class Task {
    private String taskId;
    private String description;
    private String assignedBy;
    private LocalDateTime deadline;
    private boolean completed;
    private int rewardCoins;
    private ValidationStatus validationStatus;
    private TaskType type;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String completionNotes;
    private int difficultyLevel;
    
    // Constructors
    public Task() {
        this.validationStatus = ValidationStatus.PENDING;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.difficultyLevel = 1;
    }
    
    public Task(String taskId, String description, TaskType type, int rewardCoins) {
        this();
        this.taskId = taskId;
        this.description = description;
        this.type = type;
        this.rewardCoins = rewardCoins;
    }
    
    // Getters and Setters
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
    
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { 
        this.completed = completed;
        if (completed) {
            this.completedAt = LocalDateTime.now();
        }
    }
    
    public int getRewardCoins() { return rewardCoins; }
    public void setRewardCoins(int rewardCoins) { this.rewardCoins = rewardCoins; }
    
    public ValidationStatus getValidationStatus() { return validationStatus; }
    public void setValidationStatus(ValidationStatus validationStatus) { this.validationStatus = validationStatus; }
    
    public TaskType getType() { return type; }
    public void setType(TaskType type) { this.type = type; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getCompletionNotes() { return completionNotes; }
    public void setCompletionNotes(String completionNotes) { this.completionNotes = completionNotes; }
    
    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    // Business methods
    public boolean isOverdue() {
        return deadline != null && LocalDateTime.now().isAfter(deadline) && !completed;
    }
    
    public boolean isValidated() {
        return validationStatus == ValidationStatus.APPROVED;
    }
    
    public boolean needsValidation() {
        return completed && validationStatus == ValidationStatus.PENDING;
    }
    
    public void approve(String validatorId) {
        this.validationStatus = ValidationStatus.APPROVED;
        this.assignedBy = validatorId; // Track who approved
    }
    
    public void reject(String validatorId, String reason) {
        this.validationStatus = ValidationStatus.REJECTED;
        this.completionNotes = reason;
        this.completed = false; // Allow re-completion
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", rewardCoins=" + rewardCoins +
                ", completed=" + completed +
                ", validationStatus=" + validationStatus +
                '}';
    }
}
