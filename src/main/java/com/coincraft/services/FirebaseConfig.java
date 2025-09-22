package com.coincraft.services;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Firebase configuration loader for CoinCraft application
 */
public class FirebaseConfig {
    private static final Logger LOGGER = Logger.getLogger(FirebaseConfig.class.getName());
    
    private String apiKey;
    private String authDomain;
    private String databaseURL;
    private String projectId;
    private String storageBucket;
    private String messagingSenderId;
    private String appId;
    private String measurementId;
    
    public static FirebaseConfig loadFromResources() {
        try {
            InputStream configStream = FirebaseConfig.class.getResourceAsStream("/firebase-config.json");
            if (configStream == null) {
                throw new RuntimeException("Firebase config file not found in resources");
            }
            
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> configMap = mapper.readValue(configStream, Map.class);
            
            FirebaseConfig config = new FirebaseConfig();
            config.apiKey = (String) configMap.get("apiKey");
            config.authDomain = (String) configMap.get("authDomain");
            config.databaseURL = (String) configMap.get("databaseURL");
            config.projectId = (String) configMap.get("projectId");
            config.storageBucket = (String) configMap.get("storageBucket");
            config.messagingSenderId = (String) configMap.get("messagingSenderId");
            config.appId = (String) configMap.get("appId");
            config.measurementId = (String) configMap.get("measurementId");
            
            LOGGER.info("Firebase configuration loaded successfully for project: " + config.projectId);
            return config;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load Firebase configuration: " + e.getMessage());
            throw new RuntimeException("Firebase configuration loading failed", e);
        }
    }
    
    // Getters
    public String getApiKey() { return apiKey; }
    public String getAuthDomain() { return authDomain; }
    public String getDatabaseURL() { return databaseURL; }
    public String getProjectId() { return projectId; }
    public String getStorageBucket() { return storageBucket; }
    public String getMessagingSenderId() { return messagingSenderId; }
    public String getAppId() { return appId; }
    public String getMeasurementId() { return measurementId; }
    
    // Firebase REST API URLs
    public String getAuthUrl() {
        return "https://identitytoolkit.googleapis.com/v1/accounts";
    }
    
    public String getFirestoreUrl() {
        return "https://firestore.googleapis.com/v1/projects/" + projectId + "/databases/(default)/documents";
    }
    
    public String getStorageUrl() {
        return "https://firebasestorage.googleapis.com/v0/b/" + storageBucket.replace(".appspot.com", "") + "/o";
    }
    
    public String getRealtimeDatabaseUrl() {
        return databaseURL != null ? databaseURL : 
               "https://" + projectId + "-default-rtdb.asia-southeast1.firebasedatabase.app";
    }
}
