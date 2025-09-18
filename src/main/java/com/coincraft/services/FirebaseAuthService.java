package com.coincraft.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
// import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Firebase Authentication service using REST API
 * Handles user registration, login, and token management
 */
public class FirebaseAuthService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseAuthService.class.getName());
    
    private final FirebaseConfig config;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public FirebaseAuthService(FirebaseConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Register a new user with email and password
     */
    public AuthResult registerUser(String email, String password, String displayName) {
        try {
            String url = config.getAuthUrl() + ":signUp?key=" + config.getApiKey();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("returnSecureToken", true);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    
                    AuthResult result = new AuthResult();
                    result.setSuccess(true);
                    result.setUserId((String) responseMap.get("localId"));
                    result.setIdToken((String) responseMap.get("idToken"));
                    result.setEmail((String) responseMap.get("email"));
                    result.setDisplayName(displayName);
                    
                    LOGGER.info("User registered successfully: " + email);
                    return result;
                } else {
                    LOGGER.warning("Registration failed: " + response.code());
                    return createErrorResult("Registration failed: " + response.message());
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Registration error: " + e.getMessage());
            return createErrorResult("Registration error: " + e.getMessage());
        }
    }
    
    /**
     * Sign in user with email and password
     */
    public AuthResult signInUser(String email, String password) {
        try {
            String url = config.getAuthUrl() + ":signInWithPassword?key=" + config.getApiKey();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("returnSecureToken", true);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    
                    AuthResult result = new AuthResult();
                    result.setSuccess(true);
                    result.setUserId((String) responseMap.get("localId"));
                    result.setIdToken((String) responseMap.get("idToken"));
                    result.setEmail((String) responseMap.get("email"));
                    result.setDisplayName((String) responseMap.get("displayName"));
                    
                    LOGGER.info("User signed in successfully: " + email);
                    return result;
                } else {
                    LOGGER.warning("Sign in failed: " + response.code());
                    return createErrorResult("Sign in failed: Invalid credentials");
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Sign in error: " + e.getMessage());
            return createErrorResult("Sign in error: " + e.getMessage());
        }
    }
    
    /**
     * Verify ID token
     */
    public boolean verifyToken(String idToken) {
        try {
            String url = config.getAuthUrl() + ":lookup?key=" + config.getApiKey();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("idToken", idToken);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
            
        } catch (Exception e) {
            LOGGER.severe("Token verification error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sign in user with Google OAuth token
     * In production, this would handle the actual Google OAuth flow
     */
    public AuthResult signInWithGoogle(String idToken) {
        try {
            // In a real implementation, this would:
            // 1. Verify the Google ID token
            // 2. Extract user information from the token
            // 3. Create or update the user in Firebase
            // 4. Return authentication result
            
            // For now, simulate the process
            Thread.sleep(1000); // Simulate network delay
            
            // Mock successful Google sign-in
            AuthResult result = new AuthResult();
            result.setSuccess(true);
            result.setUserId("google_user_123");
            result.setIdToken("mock_firebase_token");
            result.setEmail("user@gmail.com");
            result.setDisplayName("Google User");
            
            LOGGER.info("Google sign-in successful for user: " + result.getEmail());
            return result;
            
        } catch (InterruptedException e) {
            LOGGER.severe("Google sign-in interrupted: " + e.getMessage());
            return createErrorResult("Google sign-in was cancelled");
        } catch (Exception e) {
            LOGGER.severe("Google sign-in error: " + e.getMessage());
            return createErrorResult("Google sign-in failed: " + e.getMessage());
        }
    }
    
    private AuthResult createErrorResult(String errorMessage) {
        AuthResult result = new AuthResult();
        result.setSuccess(false);
        result.setErrorMessage(errorMessage);
        return result;
    }
    
    /**
     * Authentication result class
     */
    public static class AuthResult {
        private boolean success;
        private String userId;
        private String idToken;
        private String email;
        private String displayName;
        private String errorMessage;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
