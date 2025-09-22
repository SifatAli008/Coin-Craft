package com.coincraft.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

/**
 * Firebase Admin SDK service for server-side operations
 * Handles user management, token verification, and admin operations
 */
public class FirebaseAdminService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseAdminService.class.getName());
    
    private static FirebaseApp firebaseApp;
    private static FirebaseAuth firebaseAuth;
    private static Firestore firestore;
    private static boolean initialized = false;
    
    /**
     * Initialize Firebase Admin SDK
     */
    public static void initialize() {
        try {
            if (initialized && firebaseApp != null) {
                LOGGER.info("Firebase Admin SDK already initialized");
                return;
            }
            
            // Load service account from resources
            InputStream serviceAccountStream = FirebaseAdminService.class
                .getResourceAsStream("/firebase-service-account.json");
            
            if (serviceAccountStream == null) {
                LOGGER.warning("Firebase service account file not found. Using REST API mode.");
                return;
            }
            
            // Initialize Firebase Admin SDK
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccountStream))
                .setProjectId("coincraft-5a5d9")
                .build();
            
            firebaseApp = FirebaseApp.initializeApp(options);
            firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            firestore = FirestoreClient.getFirestore(firebaseApp);
            
            initialized = true;
            LOGGER.info("🔥 Firebase Admin SDK initialized successfully");
            
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize Firebase Admin SDK: " + e.getMessage());
            LOGGER.info("Falling back to REST API mode");
        }
    }
    
    /**
     * Verify Firebase ID token
     */
    public static FirebaseToken verifyIdToken(String idToken) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            FirebaseToken decodedToken = FirebaseAuth.getInstance()
                .verifyIdTokenAsync(idToken)
                .get();
            
            LOGGER.info("Token verified for user: " + decodedToken.getUid());
            return decodedToken;
            
        } catch (Exception e) {
            LOGGER.severe("Token verification failed: " + e.getMessage());
            throw new RuntimeException("Invalid token", e);
        }
    }
    
    /**
     * Create a new user account using Admin SDK
     */
    public static String createUser(String email, String password, String displayName) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false);
            
            UserRecord userRecord = FirebaseAuth.getInstance()
                .createUserAsync(request)
                .get();
            
            LOGGER.info("User created successfully: " + userRecord.getUid());
            return userRecord.getUid();
            
        } catch (Exception e) {
            LOGGER.severe("Failed to create user: " + e.getMessage());
            throw new RuntimeException("User creation failed", e);
        }
    }
    
    /**
     * Get user by UID
     */
    public static UserRecord getUser(String uid) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            UserRecord userRecord = FirebaseAuth.getInstance()
                .getUserAsync(uid)
                .get();
            
            return userRecord;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to get user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Delete user by UID
     */
    public static boolean deleteUser(String uid) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            FirebaseAuth.getInstance()
                .deleteUserAsync(uid)
                .get();
            
            LOGGER.info("User deleted successfully: " + uid);
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to delete user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update user custom claims (for role management)
     */
    public static boolean setUserRole(String uid, UserRole role) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role.name());
            claims.put("level", 1);
            claims.put("smartCoins", 0);
            
            FirebaseAuth.getInstance()
                .setCustomUserClaimsAsync(uid, claims)
                .get();
            
            LOGGER.info("User role set successfully: " + uid + " -> " + role);
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to set user role: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get user role from custom claims
     */
    public static UserRole getUserRole(String uid) {
        try {
            if (!initialized || firebaseAuth == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            UserRecord userRecord = FirebaseAuth.getInstance()
                .getUserAsync(uid)
                .get();
            
            Map<String, Object> claims = userRecord.getCustomClaims();
            if (claims != null && claims.containsKey("role")) {
                String roleStr = (String) claims.get("role");
                return UserRole.valueOf(roleStr);
            }
            
            return UserRole.CHILD; // Default role
            
        } catch (Exception e) {
            LOGGER.severe("Failed to get user role: " + e.getMessage());
            return UserRole.CHILD;
        }
    }
    
    /**
     * Save user data to Firestore using Admin SDK
     */
    public static boolean saveUserToFirestore(User user) {
        try {
            if (!initialized || firestore == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            DocumentReference docRef = firestore.collection("users").document(user.getUserId());
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("name", user.getName());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().name());
            userData.put("age", user.getAge());
            userData.put("level", user.getLevel());
            userData.put("smartCoinBalance", user.getSmartCoinBalance());
            userData.put("dailyStreaks", user.getDailyStreaks());
            userData.put("leaderboardRank", user.getLeaderboardRank());
            userData.put("lastLogin", user.getLastLogin());
            userData.put("createdAt", user.getCreatedAt());
            
            docRef.set(userData);
            
            LOGGER.info("User data saved to Firestore: " + user.getUserId());
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to save user to Firestore: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load user data from Firestore using Admin SDK
     */
    public static User loadUserFromFirestore(String userId) {
        try {
            if (!initialized || firestore == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            DocumentReference docRef = firestore.collection("users").document(userId);
            DocumentSnapshot document = docRef.get().get();
            
            if (document.exists()) {
                Map<String, Object> data = document.getData();
                
                User user = new User();
                user.setUserId((String) data.get("userId"));
                user.setName((String) data.get("name"));
                user.setUsername((String) data.get("username"));
                user.setEmail((String) data.get("email"));
                
                String roleStr = (String) data.get("role");
                if (roleStr != null) {
                    user.setRole(UserRole.valueOf(roleStr));
                }
                
                user.setAge((Integer) data.get("age"));
                user.setLevel((Integer) data.get("level"));
                user.setSmartCoinBalance((Integer) data.get("smartCoinBalance"));
                user.setDailyStreaks((Integer) data.get("dailyStreaks"));
                user.setLeaderboardRank((Integer) data.get("leaderboardRank"));
                
                LOGGER.info("User data loaded from Firestore: " + userId);
                return user;
            } else {
                LOGGER.info("User not found in Firestore: " + userId);
                return null;
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load user from Firestore: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all users from Firestore (admin operation)
     */
    public static java.util.List<User> getAllUsersFromFirestore() {
        java.util.List<User> users = new java.util.ArrayList<>();
        
        try {
            if (!initialized || firestore == null) {
                throw new IllegalStateException("Firebase Admin SDK not initialized");
            }
            
            QuerySnapshot querySnapshot = firestore.collection("users").get().get();
            
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Map<String, Object> data = document.getData();
                
                User user = new User();
                user.setUserId((String) data.get("userId"));
                user.setName((String) data.get("name"));
                user.setUsername((String) data.get("username"));
                user.setEmail((String) data.get("email"));
                
                String roleStr = (String) data.get("role");
                if (roleStr != null) {
                    user.setRole(UserRole.valueOf(roleStr));
                }
                
                user.setAge((Integer) data.get("age"));
                user.setLevel((Integer) data.get("level"));
                user.setSmartCoinBalance((Integer) data.get("smartCoinBalance"));
                user.setDailyStreaks((Integer) data.get("dailyStreaks"));
                user.setLeaderboardRank((Integer) data.get("leaderboardRank"));
                
                users.add(user);
            }
            
            LOGGER.info("Loaded " + users.size() + " users from Firestore");
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load users from Firestore: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Check if Firebase Admin SDK is initialized
     */
    public static boolean isInitialized() {
        return initialized && firebaseApp != null;
    }
    
    /**
     * Get Firebase connection status
     */
    public static String getConnectionStatus() {
        if (!initialized) {
            return "❌ Not Initialized";
        }
        
        if (firebaseApp != null && firebaseAuth != null && firestore != null) {
            return "✅ Firebase Admin SDK Connected";
        } else {
            return "⚠️ Partial Initialization";
        }
    }
    
    /**
     * Shutdown Firebase Admin SDK
     */
    public static void shutdown() {
        try {
            if (firebaseApp != null) {
                firebaseApp.delete();
                firebaseApp = null;
            }
            
            firebaseAuth = null;
            firestore = null;
            initialized = false;
            
            LOGGER.info("Firebase Admin SDK shutdown successfully");
            
        } catch (Exception e) {
            LOGGER.severe("Error during Firebase Admin SDK shutdown: " + e.getMessage());
        }
    }
}
