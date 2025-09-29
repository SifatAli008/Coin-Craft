package com.coincraft.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.coincraft.models.Task;
import com.coincraft.models.MessageData;
import com.coincraft.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Firestore database service using REST API
 * Handles CRUD operations for users, tasks, and badges
 */
public class FirestoreService {
    private static final Logger LOGGER = Logger.getLogger(FirestoreService.class.getName());
    
    private final FirebaseConfig config;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String idToken; // For authentication
    
    public FirestoreService(FirebaseConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    
    /**
     * Save user data to Firestore
     */
    public boolean saveUser(User user) {
        try {
            String url = config.getFirestoreUrl() + "/users/" + user.getUserId();
            
            Map<String, Object> userData = convertUserToFirestore(user);
            Map<String, Object> document = new HashMap<>();
            document.put("fields", userData);
            
            String jsonBody = objectMapper.writeValueAsString(document);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .patch(body) // Use PATCH to create or update
                    .addHeader("Authorization", "Bearer " + idToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    LOGGER.info("User saved successfully: " + user.getUserId());
                    return true;
                } else {
                    LOGGER.warning("Failed to save user: " + response.code() + " - " + response.message());
                    return false;
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error saving user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load user data from Firestore
     */
    public User loadUser(String userId) {
        try {
            String url = config.getFirestoreUrl() + "/users/" + userId;
            
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + idToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> document = objectMapper.readValue(responseBody, Map.class);
                    
                    if (document.containsKey("fields")) {
                        Map<String, Object> fields = (Map<String, Object>) document.get("fields");
                        User user = convertFirestoreToUser(fields);
                        LOGGER.info("User loaded successfully: " + userId);
                        return user;
                    }
                } else if (response.code() == 404) {
                    LOGGER.info("User not found: " + userId);
                    return null;
                } else {
                    LOGGER.warning("Failed to load user: " + response.code());
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error loading user: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Save task data to Firestore
     */
    public boolean saveTask(Task task) {
        try {
            String url = config.getFirestoreUrl() + "/tasks/" + task.getTaskId();
            
            Map<String, Object> taskData = convertTaskToFirestore(task);
            Map<String, Object> document = new HashMap<>();
            document.put("fields", taskData);
            
            String jsonBody = objectMapper.writeValueAsString(document);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .patch(body)
                    .addHeader("Authorization", "Bearer " + idToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    LOGGER.info("Task saved successfully: " + task.getTaskId());
                    return true;
                } else {
                    LOGGER.warning("Failed to save task: " + response.code());
                    return false;
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error saving task: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load tasks for a specific user
     */
    public List<Task> loadUserTasks(String userId) {
        try {
            String url = config.getFirestoreUrl() + "/tasks";
            
            // Add query to filter by user ID (simplified for MVP)
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + idToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                    
                    List<Task> tasks = new ArrayList<>();
                    if (result.containsKey("documents")) {
                        List<Map<String, Object>> documents = (List<Map<String, Object>>) result.get("documents");
                        
                        for (Map<String, Object> doc : documents) {
                            if (doc.containsKey("fields")) {
                                Map<String, Object> fields = (Map<String, Object>) doc.get("fields");
                                Task task = convertFirestoreToTask(fields);
                                if (task != null) {
                                    tasks.add(task);
                                }
                            }
                        }
                    }
                    
                    LOGGER.info("Loaded " + tasks.size() + " tasks for user: " + userId);
                    return tasks;
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error loading tasks: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get leaderboard data (top users by SmartCoin balance)
     */
    public List<User> getLeaderboard(int limit) {
        try {
            String url = config.getFirestoreUrl() + "/users";
            
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + idToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                    
                    List<User> users = new ArrayList<>();
                    if (result.containsKey("documents")) {
                        List<Map<String, Object>> documents = (List<Map<String, Object>>) result.get("documents");
                        
                        for (Map<String, Object> doc : documents) {
                            if (doc.containsKey("fields")) {
                                Map<String, Object> fields = (Map<String, Object>) doc.get("fields");
                                User user = convertFirestoreToUser(fields);
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                    
                    // Sort by SmartCoin balance (descending) and limit results
                    users.sort((a, b) -> Integer.compare(b.getSmartCoinBalance(), a.getSmartCoinBalance()));
                    List<User> leaderboard = users.stream().limit(limit).toList();
                    
                    // Set ranks
                    for (int i = 0; i < leaderboard.size(); i++) {
                        leaderboard.get(i).setLeaderboardRank(i + 1);
                    }
                    
                    LOGGER.info("Loaded leaderboard with " + leaderboard.size() + " users");
                    return leaderboard;
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error loading leaderboard: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    // Helper methods for Firestore conversion
    private Map<String, Object> convertUserToFirestore(User user) {
        Map<String, Object> fields = new HashMap<>();
        
        addStringField(fields, "userId", user.getUserId());
        addStringField(fields, "name", user.getName());
        addStringField(fields, "username", user.getUsername());
        addStringField(fields, "role", user.getRole().name());
        addIntegerField(fields, "age", user.getAge());
        addIntegerField(fields, "level", user.getLevel());
        addIntegerField(fields, "smartCoinBalance", user.getSmartCoinBalance());
        addIntegerField(fields, "dailyStreaks", user.getDailyStreaks());
        addIntegerField(fields, "leaderboardRank", user.getLeaderboardRank());
        
        if (user.getLastLogin() != null) {
            addStringField(fields, "lastLogin", user.getLastLogin().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (user.getCreatedAt() != null) {
            addStringField(fields, "createdAt", user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        return fields;
    }
    
    private User convertFirestoreToUser(Map<String, Object> fields) {
        try {
            User user = new User();
            
            user.setUserId(getStringValue(fields, "userId"));
            user.setName(getStringValue(fields, "name"));
            user.setUsername(getStringValue(fields, "username"));
            user.setAge(getIntegerValue(fields, "age"));
            user.setLevel(getIntegerValue(fields, "level"));
            user.setSmartCoinBalance(getIntegerValue(fields, "smartCoinBalance"));
            user.setDailyStreaks(getIntegerValue(fields, "dailyStreaks"));
            user.setLeaderboardRank(getIntegerValue(fields, "leaderboardRank"));
            
            String roleStr = getStringValue(fields, "role");
            if (roleStr != null) {
                user.setRole(com.coincraft.models.UserRole.valueOf(roleStr));
            }
            
            String lastLoginStr = getStringValue(fields, "lastLogin");
            if (lastLoginStr != null) {
                user.setLastLogin(LocalDateTime.parse(lastLoginStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            String createdAtStr = getStringValue(fields, "createdAt");
            if (createdAtStr != null) {
                user.setCreatedAt(LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            return user;
            
        } catch (Exception e) {
            LOGGER.severe("Error converting Firestore data to User: " + e.getMessage());
            return null;
        }
    }
    
    private Map<String, Object> convertTaskToFirestore(Task task) {
        Map<String, Object> fields = new HashMap<>();
        
        addStringField(fields, "taskId", task.getTaskId());
        addStringField(fields, "description", task.getDescription());
        addStringField(fields, "assignedBy", task.getAssignedBy());
        addBooleanField(fields, "completed", task.isCompleted());
        addIntegerField(fields, "rewardCoins", task.getRewardCoins());
        addStringField(fields, "validationStatus", task.getValidationStatus().name());
        addStringField(fields, "type", task.getType().name());
        addIntegerField(fields, "difficultyLevel", task.getDifficultyLevel());
        
        if (task.getDeadline() != null) {
            addStringField(fields, "deadline", task.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (task.getCreatedAt() != null) {
            addStringField(fields, "createdAt", task.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (task.getCompletedAt() != null) {
            addStringField(fields, "completedAt", task.getCompletedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (task.getCompletionNotes() != null) {
            addStringField(fields, "completionNotes", task.getCompletionNotes());
        }
        
        return fields;
    }
    
    private Task convertFirestoreToTask(Map<String, Object> fields) {
        try {
            Task task = new Task();
            
            task.setTaskId(getStringValue(fields, "taskId"));
            task.setDescription(getStringValue(fields, "description"));
            task.setAssignedBy(getStringValue(fields, "assignedBy"));
            task.setCompleted(getBooleanValue(fields, "completed"));
            task.setRewardCoins(getIntegerValue(fields, "rewardCoins"));
            task.setDifficultyLevel(getIntegerValue(fields, "difficultyLevel"));
            task.setCompletionNotes(getStringValue(fields, "completionNotes"));
            
            String validationStatusStr = getStringValue(fields, "validationStatus");
            if (validationStatusStr != null) {
                task.setValidationStatus(com.coincraft.models.ValidationStatus.valueOf(validationStatusStr));
            }
            
            String typeStr = getStringValue(fields, "type");
            if (typeStr != null) {
                task.setType(com.coincraft.models.TaskType.valueOf(typeStr));
            }
            
            // Parse dates
            String deadlineStr = getStringValue(fields, "deadline");
            if (deadlineStr != null) {
                task.setDeadline(LocalDateTime.parse(deadlineStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            String createdAtStr = getStringValue(fields, "createdAt");
            if (createdAtStr != null) {
                task.setCreatedAt(LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            String completedAtStr = getStringValue(fields, "completedAt");
            if (completedAtStr != null) {
                task.setCompletedAt(LocalDateTime.parse(completedAtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            return task;
            
        } catch (Exception e) {
            LOGGER.severe("Error converting Firestore data to Task: " + e.getMessage());
            return null;
        }
    }
    
    // Helper methods for Firestore field types
    private void addStringField(Map<String, Object> fields, String key, String value) {
        if (value != null) {
            Map<String, String> field = new HashMap<>();
            field.put("stringValue", value);
            fields.put(key, field);
        }
    }
    
    private void addIntegerField(Map<String, Object> fields, String key, Integer value) {
        if (value != null) {
            Map<String, String> field = new HashMap<>();
            field.put("integerValue", value.toString());
            fields.put(key, field);
        }
    }
    
    private void addBooleanField(Map<String, Object> fields, String key, Boolean value) {
        if (value != null) {
            Map<String, Boolean> field = new HashMap<>();
            field.put("booleanValue", value);
            fields.put(key, field);
        }
    }
    
    private String getStringValue(Map<String, Object> fields, String key) {
        if (fields.containsKey(key)) {
            Map<String, Object> field = (Map<String, Object>) fields.get(key);
            return (String) field.get("stringValue");
        }
        return null;
    }
    
    private Integer getIntegerValue(Map<String, Object> fields, String key) {
        if (fields.containsKey(key)) {
            Map<String, Object> field = (Map<String, Object>) fields.get(key);
            String value = (String) field.get("integerValue");
            return value != null ? Integer.parseInt(value) : 0;
        }
        return 0;
    }
    
    private Boolean getBooleanValue(Map<String, Object> fields, String key) {
        if (fields.containsKey(key)) {
            Map<String, Object> field = (Map<String, Object>) fields.get(key);
            return (Boolean) field.get("booleanValue");
        }
        return false;
    }
    
    /**
     * Get all users from Firestore
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String url = config.getFirestoreUrl() + "/users";
            
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + idToken)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> documents = (List<Map<String, Object>>) responseMap.get("documents");
                    
                    if (documents != null) {
                        for (Map<String, Object> doc : documents) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> fields = (Map<String, Object>) doc.get("fields");
                            User user = convertFirestoreToUser(fields);
                            if (user != null) {
                                users.add(user);
                            }
                        }
                    }
                    
                    LOGGER.info("Successfully loaded " + users.size() + " users from Firestore");
                } else {
                    LOGGER.warning("Failed to load users from Firestore: " + response.code());
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error loading users from Firestore: " + e.getMessage());
        }
        
        return users;
    }

    // ===================== Messaging (REST) =====================
    public boolean saveMessage(MessageData message) {
        try {
            String url = config.getFirestoreUrl() + "/messages/" + message.getMessageId();
            Map<String, Object> fields = new HashMap<>();
            addStringField(fields, "messageId", message.getMessageId());
            addStringField(fields, "conversationId", message.getConversationId());
            addStringField(fields, "senderId", message.getSenderId());
            addStringField(fields, "senderName", message.getSenderName());
            addStringField(fields, "recipientId", message.getRecipientId());
            addStringField(fields, "recipientName", message.getRecipientName());
            addStringField(fields, "content", message.getContent());
            if (message.getTimestamp() != null) {
                addStringField(fields, "timestamp", message.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            Map<String, Object> document = new HashMap<>();
            document.put("fields", fields);

            String jsonBody = objectMapper.writeValueAsString(document);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Authorization", "Bearer " + idToken)
                .build();
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            LOGGER.severe("Error saving message: " + e.getMessage());
            return false;
        }
    }

    public List<MessageData> loadConversation(String conversationId, int limit) {
        List<MessageData> messages = new ArrayList<>();
        try {
            String url = config.getFirestoreUrl() + "/messages";
            Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + idToken)
                .build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                    if (result.containsKey("documents")) {
                        List<Map<String, Object>> docs = (List<Map<String, Object>>) result.get("documents");
                        for (Map<String, Object> doc : docs) {
                            Map<String, Object> fields = (Map<String, Object>) doc.get("fields");
                            if (fields == null) continue;
                            String cid = getStringValue(fields, "conversationId");
                            if (conversationId.equals(cid)) {
                                MessageData m = new MessageData();
                                m.setMessageId(getStringValue(fields, "messageId"));
                                m.setConversationId(cid);
                                m.setSenderId(getStringValue(fields, "senderId"));
                                m.setSenderName(getStringValue(fields, "senderName"));
                                m.setRecipientId(getStringValue(fields, "recipientId"));
                                m.setRecipientName(getStringValue(fields, "recipientName"));
                                m.setContent(getStringValue(fields, "content"));
                                String ts = getStringValue(fields, "timestamp");
                                if (ts != null) {
                                    m.setTimestamp(LocalDateTime.parse(ts, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                                }
                                messages.add(m);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error loading conversation: " + e.getMessage());
        }
        // Sort by timestamp ascending
        messages.sort((a,b) -> a.getTimestamp() != null && b.getTimestamp() != null ? a.getTimestamp().compareTo(b.getTimestamp()) : 0);
        // Limit
        if (messages.size() > limit) {
            return new ArrayList<>(messages.subList(messages.size() - limit, messages.size()));
        }
        return messages;
    }
    
    /**
     * Get all tasks from Firestore
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            String url = config.getFirestoreUrl() + "/tasks";
            
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + idToken)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> documents = (List<Map<String, Object>>) responseMap.get("documents");
                    
                    if (documents != null) {
                        for (Map<String, Object> doc : documents) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> fields = (Map<String, Object>) doc.get("fields");
                            Task task = convertFirestoreToTask(fields);
                            if (task != null) {
                                tasks.add(task);
                            }
                        }
                    }
                    
                    LOGGER.info("Successfully loaded " + tasks.size() + " tasks from Firestore");
                } else {
                    LOGGER.warning("Failed to load tasks from Firestore: " + response.code());
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error loading tasks from Firestore: " + e.getMessage());
        }
        
        return tasks;
    }
}
