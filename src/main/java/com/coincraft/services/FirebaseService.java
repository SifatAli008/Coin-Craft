package com.coincraft.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.coincraft.models.Task;
import com.coincraft.models.User;

/**
 * Firebase service for authentication and Firestore database operations
 * Now uses real Firebase implementation with REST API
 */
public class FirebaseService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseService.class.getName());
    private static FirebaseService instance;
    
    private FirebaseConfig config;
    private FirebaseAuthService authService;
    private FirestoreService firestoreService;
    private boolean initialized = false;
    private String currentIdToken;
    
    // Collection names for future use
    // private static final String USERS_COLLECTION = "users";
    // private static final String TASKS_COLLECTION = "tasks";
    // private static final String BADGES_COLLECTION = "badges";
    // private static final String LEADERBOARD_COLLECTION = "leaderboard";
    
    private FirebaseService() {}
    
    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }
    
    /**
     * Initialize Firebase with real configuration
     * Uses dynamic loading to handle optional Firebase dependencies
     */
    public void initialize() {
        try {
            if (initialized) {
                LOGGER.info("Firebase already initialized");
                return;
            }
            
            // Try to load Firebase configuration
            config = FirebaseConfig.loadFromResources();
            
            // Initialize services with error handling for missing dependencies
            authService = new FirebaseAuthService(config);
            firestoreService = new FirestoreService(config);
            
            LOGGER.info(() -> "Firebase service initialized successfully for project: " + config.getProjectId());
            initialized = true;
            
        } catch (NoClassDefFoundError e) {
            LOGGER.info(() -> "Firebase libraries not found, initializing in mock mode: " + e.getMessage());
            initializeMockMode();
        } catch (Exception e) {
            LOGGER.warning(() -> "Failed to initialize Firebase, falling back to mock mode: " + e.getMessage());
            initializeMockMode();
        }
    }
    
    /**
     * Fallback to mock mode if Firebase initialization fails
     */
    private void initializeMockMode() {
        LOGGER.info("Initializing Firebase in mock mode");
        initialized = true;
        // Keep existing mock functionality as fallback
    }
    
    /**
     * Authenticate user with email and password
     * Returns user ID if successful, null if failed
     */
    public String authenticateUser(String email, String password) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (authService != null) {
                // Use real Firebase authentication
                FirebaseAuthService.AuthResult result = authService.signInUser(email, password);
                if (result.isSuccess()) {
                    currentIdToken = result.getIdToken();
                    firestoreService.setIdToken(currentIdToken);
                    LOGGER.info(() -> "Authentication successful for: " + email);
                    return result.getUserId();
                } else {
                    LOGGER.warning(() -> "Authentication failed: " + result.getErrorMessage());
                    return null;
                }
            } else {
                // Fallback to mock authentication
                if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
                    String mockUserId = "user_" + email.hashCode();
                    LOGGER.info("Mock authentication successful for: " + email);
                    return mockUserId;
                }
                return null;
            }
            
        } catch (Exception e) {
            LOGGER.severe("Authentication failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create a new user account
     */
    public String createUser(String email, String password, User user) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (authService != null) {
                // Use real Firebase authentication
                FirebaseAuthService.AuthResult result = authService.registerUser(email, password, user.getName());
                if (result.isSuccess()) {
                    currentIdToken = result.getIdToken();
                    firestoreService.setIdToken(currentIdToken);
                    
                    user.setUserId(result.getUserId());
                    saveUser(user);
                    
                    LOGGER.info("User created successfully: " + email);
                    return result.getUserId();
                } else {
                    LOGGER.warning("User creation failed: " + result.getErrorMessage());
                    return null;
                }
            } else {
                // Fallback to mock user creation
                String userId = "user_" + System.currentTimeMillis();
                user.setUserId(userId);
                saveUser(user);
                LOGGER.info("Mock user created successfully: " + userId);
                return userId;
            }
            
        } catch (Exception e) {
            LOGGER.severe("User creation failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Save user data to Firestore
     */
    public void saveUser(User user) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                boolean success = firestoreService.saveUser(user);
                if (success) {
                    LOGGER.info("User data saved successfully for " + user.getUserId());
                } else {
                    LOGGER.warning("Failed to save user data for " + user.getUserId());
                }
            } else {
                // Mock save operation
                LOGGER.info("Mock: Saving user data for " + user.getUserId());
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to save user: " + e.getMessage());
        }
    }
    
    /**
     * Load user data from Firestore
     */
    public User loadUser(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                User user = firestoreService.loadUser(userId);
                if (user != null) {
                    LOGGER.info("User data loaded successfully for " + userId);
                    return user;
                } else {
                    LOGGER.info("User not found: " + userId);
                }
            } else {
                // Mock load operation - create a sample user
                User mockUser = new User();
                mockUser.setUserId(userId);
                mockUser.setName("Test User");
                mockUser.setSmartCoinBalance(50);
                mockUser.setLevel(1);
                LOGGER.info("Mock: Loading user data for " + userId);
                return mockUser;
            }
            
            return null;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Save task data to Firestore
     */
    public void saveTask(Task task) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // Mock save operation for MVP
            LOGGER.info("Mock: Saving task data for " + task.getTaskId());
            
        } catch (Exception e) {
            LOGGER.severe("Failed to save task: " + e.getMessage());
        }
    }
    
    /**
     * Load tasks for a specific user
     */
    public List<Task> loadUserTasks(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // Mock load operation for MVP - return sample tasks
            List<Task> mockTasks = new ArrayList<>();
            
            Task task1 = new Task("task_1", "Complete Level 1: Budget Basics", 
                                 com.coincraft.models.TaskType.LEARNING, 20);
            Task task2 = new Task("task_2", "Set up your first savings goal", 
                                 com.coincraft.models.TaskType.CHALLENGE, 15);
            
            mockTasks.add(task1);
            mockTasks.add(task2);
            
            LOGGER.info("Mock: Loading tasks for user " + userId);
            return mockTasks;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get leaderboard data
     */
    public List<User> getLeaderboard(int limit) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                List<User> leaderboard = firestoreService.getLeaderboard(limit);
                LOGGER.info("Leaderboard loaded with " + leaderboard.size() + " entries");
                return leaderboard;
            } else {
                // Mock leaderboard for fallback
                List<User> mockLeaderboard = new ArrayList<>();
                
                for (int i = 1; i <= Math.min(limit, 10); i++) {
                    User user = new User();
                    user.setUserId("user_" + i);
                    user.setName("Player " + i);
                    user.setSmartCoinBalance(100 - (i * 5));
                    user.setLevel(Math.max(1, 5 - i));
                    user.setLeaderboardRank(i);
                    mockLeaderboard.add(user);
                }
                
                LOGGER.info("Mock: Loading leaderboard with " + limit + " entries");
                return mockLeaderboard;
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Removed unused convertUserToMap method - functionality moved to FirestoreService
    
    /**
     * Shutdown Firebase connection
     */
    public void shutdown() {
        try {
            // Firebase shutdown commented out for MVP
            // if (firebaseApp != null) {
            //     firebaseApp.delete();
            // }
            initialized = false;
            LOGGER.info("Firebase service shutdown successfully");
        } catch (Exception e) {
            LOGGER.severe("Error during Firebase shutdown: " + e.getMessage());
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
