package com.coincraft.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
     * Sign in user with Google via Firebase IdP (signInWithIdp)
     * Uses Google OAuth access token to sign in to Firebase
     */
    public AuthResult signInWithGoogleIdpAccessToken(String googleAccessToken) {
        try {
            String url = config.getAuthUrl() + ":signInWithIdp?key=" + config.getApiKey();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestUri", "http://localhost");
            requestBody.put("returnSecureToken", true);

            Map<String, Object> postBody = new HashMap<>();
            postBody.put("providerId", "google.com");
            postBody.put("access_token", googleAccessToken);
            // Non-spec field names are wrapped in url-encoded style inside JSON per REST API
            requestBody.put("postBody", "providerId=google.com&access_token=" + googleAccessToken);
            requestBody.put("returnIdpCredential", true);
            requestBody.put("autoCreate", true);
            
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
                    
                    LOGGER.info("Google sign-in successful for user: " + result.getEmail());
                    return result;
                } else {
                    String errorBody = null;
                    try { errorBody = response.body() != null ? response.body().string() : null; } catch (Exception ignored) {}
                    LOGGER.warning("Google sign-in failed: " + response.code() + (errorBody != null ? " body=" + errorBody : ""));
                    return createErrorResult("Google sign-in failed: Invalid token or configuration" + (errorBody != null ? " (" + errorBody + ")" : ""));
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Google sign-in error: " + e.getMessage());
            return createErrorResult("Google sign-in failed: " + e.getMessage());
        }
    }
    
    /**
     * Sign in user with Google user info (alternative method)
     * Creates a Firebase user from Google OAuth user information
     */
    public AuthResult signInWithGoogleUserInfo(GoogleOAuthService.GoogleUserInfo googleUser) {
        try {
            // First, try to sign in with existing account
            AuthResult existingResult = signInUser(googleUser.getEmail(), "google_oauth_user");
            
            if (existingResult.isSuccess()) {
                LOGGER.info("Google user signed in with existing account: " + googleUser.getEmail());
                return existingResult;
            }
            
            // If user doesn't exist, create new account
            String url = config.getAuthUrl() + ":signUp?key=" + config.getApiKey();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", googleUser.getEmail());
            requestBody.put("password", "google_oauth_temp_password_" + System.currentTimeMillis());
            requestBody.put("displayName", googleUser.getName());
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
                    
                    LOGGER.info("Google user registered successfully: " + result.getEmail());
                    return result;
                } else {
                    LOGGER.warning("Google user registration failed: " + response.code());
                    return createErrorResult("Google user registration failed");
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Google user sign-in error: " + e.getMessage());
            return createErrorResult("Google user sign-in failed: " + e.getMessage());
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
