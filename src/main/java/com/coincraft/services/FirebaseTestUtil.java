package com.coincraft.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.models.ValidationStatus;

/**
 * Firebase Test Utility for CoinCraft
 * Provides methods to test Firebase data operations
 */
public class FirebaseTestUtil {
    private static final Logger LOGGER = Logger.getLogger(FirebaseTestUtil.class.getName());
    
    /**
     * Run comprehensive Firebase tests
     */
    public static void runFirebaseTests() {
        LOGGER.info("🧪 Starting Firebase Data Tests...");
        
        try {
            FirebaseDataManager dataManager = FirebaseDataManager.getInstance();
            dataManager.initialize();
            
            if (!dataManager.isFirebaseAvailable()) {
                LOGGER.warning("Firebase not available - running in offline mode");
                return;
            }
            
            // Test 1: Create and save a test user
            LOGGER.info("Test 1: Creating test user...");
            User testUser = createTestUser();
            boolean userSaved = dataManager.saveUserAsync(testUser).get();
            LOGGER.info("Test 1 Result: " + (userSaved ? "PASSED" : "FAILED"));
            
            // Test 2: Load the test user
            LOGGER.info("Test 2: Loading test user...");
            User loadedUser = dataManager.loadUserAsync(testUser.getUserId()).get();
            boolean userLoaded = loadedUser != null && loadedUser.getName().equals(testUser.getName());
            LOGGER.info("Test 2 Result: " + (userLoaded ? "PASSED" : "FAILED"));
            
            // Test 3: Create and save a test task
            LOGGER.info("Test 3: Creating test task...");
            Task testTask = createTestTask(testUser.getUserId());
            boolean taskSaved = dataManager.saveTaskAsync(testTask).get();
            LOGGER.info("Test 3 Result: " + (taskSaved ? "PASSED" : "FAILED"));
            
            // Test 4: Load user tasks
            LOGGER.info("Test 4: Loading user tasks...");
            List<Task> userTasks = dataManager.loadUserTasksAsync(testUser.getUserId()).get();
            boolean tasksLoaded = userTasks != null && !userTasks.isEmpty();
            LOGGER.info("Test 4 Result: " + (tasksLoaded ? "PASSED" : "FAILED"));
            
            // Test 5: Update user progress
            LOGGER.info("Test 5: Updating user progress...");
            boolean progressUpdated = dataManager.updateUserProgressAsync(
                testUser.getUserId(), 50, 25).get();
            LOGGER.info("Test 5 Result: " + (progressUpdated ? "PASSED" : "FAILED"));
            
            // Test 6: Complete task
            LOGGER.info("Test 6: Completing task...");
            boolean taskCompleted = dataManager.completeTaskAsync(testTask, "Test completion notes").get();
            LOGGER.info("Test 6 Result: " + (taskCompleted ? "PASSED" : "FAILED"));
            
            // Test 7: Get leaderboard
            LOGGER.info("Test 7: Loading leaderboard...");
            List<User> leaderboard = dataManager.getLeaderboardAsync(10).get();
            boolean leaderboardLoaded = leaderboard != null;
            LOGGER.info("Test 7 Result: " + (leaderboardLoaded ? "PASSED" : "FAILED"));
            
            // Test 8: Cache statistics
            LOGGER.info("Test 8: Checking cache...");
            var cacheStats = dataManager.getCacheStats();
            LOGGER.info("Test 8 Result: Cache stats - " + cacheStats.toString());
            
            LOGGER.info("Firebase Tests Completed!");
            LOGGER.info("Connection Status: " + dataManager.getConnectionStatus());
            
        } catch (Exception e) {
            LOGGER.severe("Firebase Tests Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create a test user for Firebase testing
     */
    private static User createTestUser() {
        User user = new User();
        user.setUserId("test_user_" + System.currentTimeMillis());
        user.setName("Test Adventurer");
        user.setUsername("testadventurer");
        user.setEmail("test@example.com");
        user.setRole(UserRole.CHILD);
        user.setAge(10);
        user.setLevel(1);
        user.setSmartCoinBalance(100);
        user.setExperiencePoints(50);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        return user;
    }
    
    /**
     * Create a test task for Firebase testing
     */
    private static Task createTestTask(String assignedBy) {
        Task task = new Task();
        task.setTaskId("test_task_" + System.currentTimeMillis());
        task.setDescription("Test task for Firebase validation");
        task.setAssignedBy(assignedBy);
        task.setCompleted(false);
        task.setRewardCoins(25);
        task.setValidationStatus(ValidationStatus.PENDING);
        task.setType(TaskType.CHORE);
        task.setDifficultyLevel(2);
        task.setCreatedAt(LocalDateTime.now());
        task.setDeadline(LocalDateTime.now().plusDays(7));
        return task;
    }
    
    /**
     * Test Firebase connection without data operations
     */
    public static boolean testFirebaseConnection() {
        try {
            FirebaseDataManager dataManager = FirebaseDataManager.getInstance();
            dataManager.initialize();
            
            boolean isConnected = dataManager.isFirebaseAvailable();
            LOGGER.info("Firebase Connection Test: " + (isConnected ? "CONNECTED" : "OFFLINE"));
            LOGGER.info("Status: " + dataManager.getConnectionStatus());
            
            return isConnected;
            
        } catch (Exception e) {
            LOGGER.severe("Firebase Connection Test Failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Clean up test data
     */
    public static void cleanupTestData() {
        try {
            FirebaseDataManager dataManager = FirebaseDataManager.getInstance();
            dataManager.invalidateCache();
            LOGGER.info("Test data cleanup completed");
        } catch (Exception e) {
            LOGGER.warning("Failed to cleanup test data: " + e.getMessage());
        }
    }
}

