package com.coincraft.services;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

/**
 * Google OAuth2 Authentication Service
 * Handles proper Google OAuth2 flow for user authentication
 */
public class GoogleOAuthService {
    private static final Logger LOGGER = Logger.getLogger(GoogleOAuthService.class.getName());
    
    // Google OAuth2 configuration
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URI;
    private final java.util.Collection<String> SCOPES;
    
    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private final NetHttpTransport httpTransport;
    
    public GoogleOAuthService() throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        // Load configuration from properties file
        Properties config = loadOAuthConfig();
        
        this.CLIENT_ID = config.getProperty("google.oauth.client.id", "YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com");
        this.CLIENT_SECRET = config.getProperty("google.oauth.client.secret", "YOUR_GOOGLE_CLIENT_SECRET");
        this.REDIRECT_URI = config.getProperty("google.oauth.redirect.uri", "http://localhost:8888/Callback");
        
        String scopesString = config.getProperty("google.oauth.scopes", 
            "openid,https://www.googleapis.com/auth/userinfo.email,https://www.googleapis.com/auth/userinfo.profile");
        this.SCOPES = Arrays.asList(scopesString.split(","));
        
        // Check if OAuth is properly configured
        if (CLIENT_ID.startsWith("YOUR_GOOGLE_CLIENT_ID") || CLIENT_ID.startsWith("DISABLED_")) {
            LOGGER.warning("Google OAuth is not configured with real credentials. Google sign-in will be disabled.");
            LOGGER.info("To enable Google sign-in, please configure real OAuth credentials in google-oauth-config.properties");
        } else {
            LOGGER.info("Google OAuth service initialized with client ID: " + CLIENT_ID.substring(0, 20) + "...");
        }
    }
    
    /**
     * Load OAuth configuration from properties file
     */
    private Properties loadOAuthConfig() {
        Properties config = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("google-oauth-config.properties")) {
            if (input != null) {
                config.load(input);
                LOGGER.info("Loaded Google OAuth configuration from properties file");
            } else {
                LOGGER.warning("Google OAuth configuration file not found, using default values");
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to load Google OAuth configuration: " + e.getMessage());
        }
        return config;
    }
    
    /**
     * Authenticate user with Google OAuth2
     * Opens browser for user consent and returns user information
     */
    public CompletableFuture<GoogleUserInfo> authenticateUser() {
        return CompletableFuture.supplyAsync(() -> {
            // Check if OAuth is properly configured
            if (CLIENT_ID.startsWith("YOUR_GOOGLE_CLIENT_ID") || CLIENT_ID.startsWith("DISABLED_")) {
                throw new RuntimeException("Google OAuth is not configured. Please set up real OAuth credentials in google-oauth-config.properties");
            }
            
            try {
                // Create authorization code flow
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPES)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
                
                // Set up local server receiver with automatic port selection
                LocalServerReceiver receiver = null;
                int[] ports = {8888, 8889, 8890, 9000, 9001};  // Try multiple ports
                
                for (int port : ports) {
                    try {
                        receiver = new LocalServerReceiver.Builder()
                            .setPort(port)
                            .build();
                        LOGGER.info("OAuth callback server started on port: " + port);
                        break;
                    } catch (Exception e) {
                        LOGGER.warning("Port " + port + " is in use, trying next port...");
                    }
                }
                
                if (receiver == null) {
                    // Fallback to automatic port selection
                    receiver = new LocalServerReceiver.Builder()
                        .setPort(-1)  // Let system choose available port
                        .build();
                    LOGGER.info("Using automatic port selection for OAuth callback");
                }
                
                // Authorize and get credentials
                com.google.api.client.auth.oauth2.Credential credential = 
                    new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
                
                // Get user information
                Oauth2 oauth2 = new Oauth2.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("CoinCraft")
                    .build();
                
                Userinfo userInfo = oauth2.userinfo().get().execute();
                
                // Create Google user info object
                GoogleUserInfo googleUser = new GoogleUserInfo();
                googleUser.setEmail(userInfo.getEmail());
                googleUser.setName(userInfo.getName());
                googleUser.setId(userInfo.getId());
                googleUser.setPictureUrl(userInfo.getPicture());
                googleUser.setVerifiedEmail(userInfo.getVerifiedEmail());
                // Expose access token for Firebase IdP sign-in
                try { googleUser.setAccessToken(credential.getAccessToken()); } catch (Exception ignored) {}
                
                LOGGER.info("Google authentication successful for user: " + googleUser.getEmail());
                return googleUser;
                
            } catch (Exception e) {
                LOGGER.severe("Google authentication failed: " + e.getMessage());
                throw new RuntimeException("Google authentication failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * Alternative method using manual authorization URL for desktop applications
     */
    public String getAuthorizationUrl() {
        try {
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
            
            AuthorizationCodeRequestUrl authorizationUrl = 
                flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
            
            return authorizationUrl.build();
            
        } catch (Exception e) {
            LOGGER.severe("Failed to generate authorization URL: " + e.getMessage());
            throw new RuntimeException("Failed to generate authorization URL", e);
        }
    }
    
    /**
     * Exchange authorization code for access token and user info
     */
    public CompletableFuture<GoogleUserInfo> exchangeCodeForUserInfo(String authorizationCode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPES)
                    .build();
                
                // Exchange code for token
                TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
                
                // Create credential from token
                com.google.api.client.auth.oauth2.Credential credential = 
                    flow.createAndStoreCredential(tokenResponse, "user");
                
                // Get user information
                Oauth2 oauth2 = new Oauth2.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("CoinCraft")
                    .build();
                
                Userinfo userInfo = oauth2.userinfo().get().execute();
                
                // Create Google user info object
                GoogleUserInfo googleUser = new GoogleUserInfo();
                googleUser.setEmail(userInfo.getEmail());
                googleUser.setName(userInfo.getName());
                googleUser.setId(userInfo.getId());
                googleUser.setPictureUrl(userInfo.getPicture());
                googleUser.setVerifiedEmail(userInfo.getVerifiedEmail());
                
                LOGGER.info("Google code exchange successful for user: " + googleUser.getEmail());
                return googleUser;
                
            } catch (Exception e) {
                LOGGER.severe("Google code exchange failed: " + e.getMessage());
                throw new RuntimeException("Google code exchange failed: " + e.getMessage(), e);
            }
        });
    }
    
    /**
     * Google User Information class
     */
    public static class GoogleUserInfo {
        private String email;
        private String name;
        private String id;
        private String pictureUrl;
        private Boolean verifiedEmail;
        private String accessToken;
        
        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getPictureUrl() { return pictureUrl; }
        public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
        
        public Boolean getVerifiedEmail() { return verifiedEmail; }
        public void setVerifiedEmail(Boolean verifiedEmail) { this.verifiedEmail = verifiedEmail; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        
        @Override
        public String toString() {
            return "GoogleUserInfo{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", verifiedEmail=" + verifiedEmail +
                '}';
        }
    }
}
