package com.coincraft.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.StorageOptions;

/**
 * Firebase Storage service for handling file uploads and downloads
 * Manages avatar images, game assets, and user-generated content
 */
public class FirebaseStorageService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseStorageService.class.getName());
    
    private static Storage storage;
    private static Bucket bucket;
    private static boolean initialized = false;
    
    // Storage paths
    private static final String AVATAR_IMAGES_PATH = "avatars/";
    private static final String GAME_ASSETS_PATH = "game-assets/";
    private static final String USER_UPLOADS_PATH = "user-uploads/";
    private static final String BADGE_IMAGES_PATH = "badges/";
    
    /**
     * Initialize Firebase Storage
     */
    public static void initialize() {
        try {
            if (initialized && storage != null) {
                LOGGER.info("Firebase Storage already initialized");
                return;
            }
            
            // Initialize Storage using default credentials
            storage = StorageOptions.getDefaultInstance().getService();
            bucket = storage.get("coincraft-5a5d9.firebasestorage.app");
            
            if (bucket == null) {
                LOGGER.warning("Storage bucket not found. Check bucket name and permissions.");
                return;
            }
            
            initialized = true;
            LOGGER.info("🔥 Firebase Storage initialized successfully");
            
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize Firebase Storage: " + e.getMessage());
        }
    }
    
    /**
     * Upload avatar image for a user
     */
    public static String uploadAvatarImage(String userId, File imageFile) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            String fileName = userId + "_avatar_" + System.currentTimeMillis() + ".png";
            String blobName = AVATAR_IMAGES_PATH + fileName;
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();
            
            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                Blob blob = storage.create(blobInfo, fileInputStream);
                LOGGER.info("Avatar image uploaded successfully: " + blobName);
                return getPublicUrl(blobName);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to upload avatar image: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Upload avatar image from byte array
     */
    public static String uploadAvatarImage(String userId, byte[] imageData) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            String fileName = userId + "_avatar_" + System.currentTimeMillis() + ".png";
            String blobName = AVATAR_IMAGES_PATH + fileName;
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();
            
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
                Blob blob = storage.create(blobInfo, inputStream);
                LOGGER.info("Avatar image uploaded successfully: " + blobName);
                return getPublicUrl(blobName);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to upload avatar image: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Upload badge image
     */
    public static String uploadBadgeImage(String badgeId, byte[] imageData) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            String fileName = badgeId + "_badge.png";
            String blobName = BADGE_IMAGES_PATH + fileName;
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();
            
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
                Blob blob = storage.create(blobInfo, inputStream);
                LOGGER.info("Badge image uploaded successfully: " + blobName);
                return getPublicUrl(blobName);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to upload badge image: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Upload game asset (sounds, images, etc.)
     */
    public static String uploadGameAsset(String assetName, File assetFile) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            String blobName = GAME_ASSETS_PATH + assetName;
            
            // Determine content type based on file extension
            String contentType = getContentType(assetName);
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
            
            try (FileInputStream fileInputStream = new FileInputStream(assetFile)) {
                Blob blob = storage.create(blobInfo, fileInputStream);
                LOGGER.info("Game asset uploaded successfully: " + blobName);
                return getPublicUrl(blobName);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to upload game asset: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Delete a file from storage
     */
    public static boolean deleteFile(String blobName) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            boolean deleted = storage.delete(blobId);
            
            if (deleted) {
                LOGGER.info("File deleted successfully: " + blobName);
            } else {
                LOGGER.warning("File not found or already deleted: " + blobName);
            }
            
            return deleted;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to delete file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a signed URL for private file access
     */
    public static String getSignedUrl(String blobName, long durationMinutes) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            Blob blob = storage.get(blobId);
            
            if (blob == null) {
                LOGGER.warning("File not found: " + blobName);
                return null;
            }
            
            String signedUrl = blob.signUrl(durationMinutes, TimeUnit.MINUTES, 
                SignUrlOption.httpMethod(HttpMethod.GET)).toString();
            
            LOGGER.info("Signed URL generated for: " + blobName);
            return signedUrl;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to generate signed URL: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get public URL for a file
     */
    private static String getPublicUrl(String blobName) {
        return "https://firebasestorage.googleapis.com/v0/b/coincraft-5a5d9.firebasestorage.app/o/" +
               blobName.replace("/", "%2F") + "?alt=media";
    }
    
    /**
     * Get content type based on file extension
     */
    private static String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        
        switch (extension) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "mp4":
                return "video/mp4";
            case "json":
                return "application/json";
            default:
                return "application/octet-stream";
        }
    }
    
    /**
     * Check if Firebase Storage is initialized
     */
    public static boolean isInitialized() {
        return initialized && storage != null;
    }
    
    /**
     * Get Firebase Storage connection status
     */
    public static String getConnectionStatus() {
        if (!initialized) {
            return "❌ Not Initialized";
        }
        
        if (storage != null && bucket != null) {
            return "✅ Firebase Storage Connected";
        } else {
            return "⚠️ Partial Initialization";
        }
    }
    
    /**
     * List files in a specific path
     */
    public static java.util.List<String> listFiles(String path) {
        java.util.List<String> files = new java.util.ArrayList<>();
        
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            Bucket bucket = storage.get("coincraft-5a5d9.firebasestorage.app");
            
            for (Blob blob : bucket.list().iterateAll()) {
                if (blob.getName().startsWith(path)) {
                    files.add(blob.getName());
                }
            }
            
            LOGGER.info("Listed " + files.size() + " files in path: " + path);
            
        } catch (Exception e) {
            LOGGER.severe("Failed to list files: " + e.getMessage());
        }
        
        return files;
    }
    
    /**
     * Get file metadata
     */
    public static Blob getFileInfo(String blobName) {
        try {
            if (!initialized || storage == null) {
                throw new IllegalStateException("Firebase Storage not initialized");
            }
            
            BlobId blobId = BlobId.of("coincraft-5a5d9.firebasestorage.app", blobName);
            Blob blob = storage.get(blobId);
            
            return blob;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to get file info: " + e.getMessage());
            return null;
        }
    }
}
