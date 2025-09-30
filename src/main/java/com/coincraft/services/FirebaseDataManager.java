package com.coincraft.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.coincraft.models.Badge;
import com.coincraft.models.Task;
import com.coincraft.models.User;

/**
 * Enhanced Firebase Data Manager for CoinCraft
 * Provides high-level data operations with automatic caching and offline support
 */
public class FirebaseDataManager {
    private static final Logger LOGGER = Logger.getLogger(FirebaseDataManager.class.getName());
    private static FirebaseDataManager instance;
    
    private FirebaseService firebaseService;
    private FirestoreService firestoreService;
    private String currentIdToken;
    private boolean initialized = false;
    
    // Local cache for better performance
    private User currentUserCache;
    private List<Task> userTasksCache = new ArrayList<>();
    private final List<Badge> badgesCache = new ArrayList<>();
    private LocalDateTime lastCacheUpdate;
    
    private FirebaseDataManager() {}
    
    public static synchronized FirebaseDataManager getInstance() {
        if (instance == null) {
            instance = new FirebaseDataManager();
        }
        return instance;
    }
    
    /**
     * Set the current authentication token
     */
    public void setCurrentIdToken(String idToken) {
        this.currentIdToken = idToken;
        LOGGER.info("Authentication token updated");
    }
    
    /**
     * Initialize the data manager
     */
    public void initialize() {
        try {
            firebaseService = FirebaseService.getInstance();
            firebaseService.initialize();
            
            if (firebaseService.isInitialized()) {
                // Get FirestoreService from FirebaseService
                firestoreService = firebaseService.getFirestoreService();
                initialized = true;
                LOGGER.info("Firebase Data Manager initialized successfully");
            } else {
                LOGGER.warning("Firebase Data Manager initialized in offline mode");
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize Firebase Data Manager: " + e.getMessage());
        }
    }
    
    /**
     * Save user data with automatic validation and caching
     */
    public CompletableFuture<Boolean> saveUserAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized) initialize();
                
                // Validate user data
                if (!validateUserData(user)) {
                    LOGGER.warning("Invalid user data provided");
                    return false;
                }
                
                // Update timestamps
                if (user.getLastLogin() == null) {
                    user.setLastLogin(LocalDateTime.now());
                }
                
                // Save to Firebase
                boolean success = false;
                if (firestoreService != null && currentIdToken != null) {
                    firestoreService.setIdToken(currentIdToken);
                    success = firestoreService.saveUser(user);
                } else {
                    // Fallback to Firebase service
                    firebaseService.saveUser(user);
                    success = true; // Assume success for Firebase service
                }
                
                if (success) {
                    // Update cache
                    currentUserCache = user;
                    lastCacheUpdate = LocalDateTime.now();
                    LOGGER.info("User data saved successfully: " + user.getName());
                }
                
                return success;
                
            } catch (Exception e) {
                LOGGER.severe("Error saving user data: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Load user data with caching
     */
    public CompletableFuture<User> loadUserAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized) initialize();
                
                // Check cache first (if less than 5 minutes old)
                if (currentUserCache != null && 
                    currentUserCache.getUserId().equals(userId) &&
                    lastCacheUpdate != null &&
                    lastCacheUpdate.isAfter(LocalDateTime.now().minusMinutes(5))) {
                    LOGGER.info("Returning cached user data: " + userId);
                    return currentUserCache;
                }
                
                // Load from Firebase
                User user = null;
                if (firestoreService != null && currentIdToken != null) {
                    firestoreService.setIdToken(currentIdToken);
                    user = firestoreService.loadUser(userId);
                } else {
                    user = firebaseService.loadUser(userId);
                }
                
                if (user != null) {
                    // Update cache
                    currentUserCache = user;
                    lastCacheUpdate = LocalDateTime.now();
                    LOGGER.info("User data loaded successfully: " + user.getName());
                }
                
                return user;
                
            } catch (Exception e) {
                LOGGER.severe("Error loading user data: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Save task with automatic validation
     */
    public CompletableFuture<Boolean> saveTaskAsync(Task task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized) initialize();
                
                // Validate task data
                if (!validateTaskData(task)) {
                    LOGGER.warning("Invalid task data provided");
                    return false;
                }
                
                // Update timestamps
                if (task.getCreatedAt() == null) {
                    task.setCreatedAt(LocalDateTime.now());
                }
                if (task.isCompleted() && task.getCompletedAt() == null) {
                    task.setCompletedAt(LocalDateTime.now());
                }
                
                // Save to Firebase
                boolean success = false;
                if (firestoreService != null && currentIdToken != null) {
                    firestoreService.setIdToken(currentIdToken);
                    success = firestoreService.saveTask(task);
                } else {
                    // Fallback to Firebase service
                    firebaseService.saveTask(task);
                    success = true; // Assume success for Firebase service
                }
                
                if (success) {
                    // Update cache
                    updateTaskInCache(task);
                    LOGGER.info("Task saved successfully: " + task.getTaskId());
                }
                
                return success;
                
            } catch (Exception e) {
                LOGGER.severe("Error saving task: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Load user tasks with caching
     */
    public CompletableFuture<List<Task>> loadUserTasksAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized) initialize();
                
                // Check cache first (if less than 2 minutes old)
                if (!userTasksCache.isEmpty() &&
                    lastCacheUpdate != null &&
                    lastCacheUpdate.isAfter(LocalDateTime.now().minusMinutes(2))) {
                    LOGGER.info("Returning cached tasks for user: " + userId);
                    return new ArrayList<>(userTasksCache);
                }
                
                // Load from Firebase
                List<Task> tasks = null;
                if (firestoreService != null && currentIdToken != null) {
                    firestoreService.setIdToken(currentIdToken);
                    tasks = firestoreService.loadUserTasks(userId);
                } else {
                    tasks = firebaseService.loadUserTasks(userId);
                }
                
                if (tasks != null) {
                    // Update cache
                    userTasksCache = new ArrayList<>(tasks);
                    lastCacheUpdate = LocalDateTime.now();
                    LOGGER.info("Loaded " + tasks.size() + " tasks for user: " + userId);
                }
                
                return tasks != null ? tasks : new ArrayList<>();
                
            } catch (Exception e) {
                LOGGER.severe("Error loading user tasks: " + e.getMessage());
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Update user progress (coins, level, etc.)
     */
    public CompletableFuture<Boolean> updateUserProgressAsync(String userId, int coinsEarned, int experienceGained) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Load current user
                User user = loadUserAsync(userId).get();
                if (user == null) {
                    LOGGER.warning("User not found for progress update: " + userId);
                    return false;
                }
                
                // Update progress
                user.addSmartCoins(coinsEarned);
                user.setExperiencePoints(user.getExperiencePoints() + experienceGained);
                
                // Check for level up
                if (user.canLevelUp()) {
                    user.levelUp();
                    LOGGER.info("🎉 User leveled up! New level: " + user.getLevel());
                }
                
                // Save updated user
                boolean success = saveUserAsync(user).get();
                
                if (success) {
                    LOGGER.info("User progress updated: +" + coinsEarned + " coins, +" + experienceGained + " XP");
                }
                
                return success;
                
            } catch (Exception e) {
                LOGGER.severe("Error updating user progress: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Complete a task and award rewards
     */
    public CompletableFuture<Boolean> completeTaskAsync(Task task, String completionNotes) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Mark task as completed
                task.setCompleted(true);
                task.setCompletedAt(LocalDateTime.now());
                task.setCompletionNotes(completionNotes);
                task.setValidationStatus(com.coincraft.models.ValidationStatus.PENDING);
                
                // Save task
                boolean taskSaved = saveTaskAsync(task).get();
                
                if (taskSaved) {
                    // Do not award coins here. Coins are escrowed on task creation
                    // and transferred to the child only upon parent approval via the
                    // SmartCoinLedgerService in TaskVerificationDialog.
                    LOGGER.info("Task marked completed and pending approval: " + task.getTaskId());
                    return true;
                }
                
                return false;
                
            } catch (Exception e) {
                LOGGER.severe("Error completing task: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Get leaderboard data
     */
    public CompletableFuture<List<User>> getLeaderboardAsync(int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized) initialize();
                
                List<User> leaderboard = null;
                if (firestoreService != null && currentIdToken != null) {
                    firestoreService.setIdToken(currentIdToken);
                    leaderboard = firestoreService.getLeaderboard(limit);
                } else {
                    leaderboard = firebaseService.getLeaderboard(limit);
                }
                
                LOGGER.info("Loaded leaderboard with " + (leaderboard != null ? leaderboard.size() : 0) + " users");
                return leaderboard != null ? leaderboard : new ArrayList<>();
                
            } catch (Exception e) {
                LOGGER.severe("Error loading leaderboard: " + e.getMessage());
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Invalidate cache (force reload from Firebase)
     */
    public void invalidateCache() {
        currentUserCache = null;
        userTasksCache.clear();
        badgesCache.clear();
        lastCacheUpdate = null;
        LOGGER.info("Cache invalidated - next requests will reload from Firebase");
    }
    
    /**
     * Get cache statistics
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("cacheInitialized", lastCacheUpdate != null);
        stats.put("lastUpdate", lastCacheUpdate);
        stats.put("cachedUser", currentUserCache != null ? currentUserCache.getName() : null);
        stats.put("cachedTasksCount", userTasksCache.size());
        stats.put("cachedBadgesCount", badgesCache.size());
        return stats;
    }
    
    // Validation methods
    private boolean validateUserData(User user) {
        if (user == null) return false;
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) return false;
        if (user.getName() == null || user.getName().trim().isEmpty()) return false;
        if (user.getRole() == null) return false;
        if (user.getAge() < 7 || user.getAge() > 100) return false;
        return true;
    }
    
    private boolean validateTaskData(Task task) {
        if (task == null) return false;
        if (task.getTaskId() == null || task.getTaskId().trim().isEmpty()) return false;
        if (task.getDescription() == null || task.getDescription().trim().isEmpty()) return false;
        if (task.getAssignedBy() == null || task.getAssignedBy().trim().isEmpty()) return false;
        return true;
    }
    
    private void updateTaskInCache(Task task) {
        // Remove old version if exists
        userTasksCache.removeIf(t -> t.getTaskId().equals(task.getTaskId()));
        // Add updated version
        userTasksCache.add(task);
    }
    
    /**
     * Check if Firebase is available
     */
    public boolean isFirebaseAvailable() {
        return initialized && firebaseService != null && firebaseService.isInitialized();
    }
    
    /**
     * Get connection status
     */
    public String getConnectionStatus() {
        if (!initialized) return "Not Initialized";
        if (!isFirebaseAvailable()) return "Offline Mode";
        return "Connected to Firebase";
    }
}
