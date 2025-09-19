package com.coincraft.ui.routing;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Security guard for role-based access control
 * Implements the least privilege principle
 */
public class RoleGuard {
    
    // Feature access mapping by role
    private static final Map<UserRole, Set<String>> ROLE_FEATURES = new HashMap<>();
    
    static {
        // Child features
        Set<String> childFeatures = new HashSet<>();
        childFeatures.add("view_avatar");
        childFeatures.add("customize_avatar");
        childFeatures.add("view_coins");
        childFeatures.add("view_level");
        childFeatures.add("complete_tasks");
        childFeatures.add("view_badges");
        childFeatures.add("view_leaderboard");
        childFeatures.add("daily_streak");
        childFeatures.add("request_mission"); // Level-gated
        childFeatures.add("group_messaging");
        ROLE_FEATURES.put(UserRole.CHILD, childFeatures);
        
        // Elder features (same as child but with additional privileges)
        Set<String> elderFeatures = new HashSet<>(childFeatures);
        elderFeatures.add("mentor_children");
        elderFeatures.add("advanced_tasks");
        ROLE_FEATURES.put(UserRole.ELDER, elderFeatures);
        
        // Parent features
        Set<String> parentFeatures = new HashSet<>();
        parentFeatures.add("create_child_accounts");
        parentFeatures.add("purchase_coins");
        parentFeatures.add("view_child_progress");
        parentFeatures.add("verify_tasks");
        parentFeatures.add("assign_tasks");
        parentFeatures.add("view_analytics");
        parentFeatures.add("manage_permissions");
        parentFeatures.add("system_messaging");
        parentFeatures.add("resource_center");
        ROLE_FEATURES.put(UserRole.PARENT, parentFeatures);
        
        // Teacher features (extends parent features)
        Set<String> teacherFeatures = new HashSet<>(parentFeatures);
        teacherFeatures.add("manage_classroom");
        teacherFeatures.add("create_group_challenges");
        teacherFeatures.add("view_class_analytics");
        teacherFeatures.add("educational_content");
        ROLE_FEATURES.put(UserRole.TEACHER, teacherFeatures);
        
        // Admin features (all features)
        Set<String> adminFeatures = new HashSet<>();
        adminFeatures.add("user_management");
        adminFeatures.add("content_management");
        adminFeatures.add("system_settings");
        adminFeatures.add("economy_management");
        adminFeatures.add("analytics_dashboard");
        adminFeatures.add("shop_management");
        adminFeatures.add("audit_logs");
        adminFeatures.add("support_feedback");
        adminFeatures.add("security_controls");
        // Add all other role features for admin
        adminFeatures.addAll(childFeatures);
        adminFeatures.addAll(parentFeatures);
        adminFeatures.addAll(teacherFeatures);
        ROLE_FEATURES.put(UserRole.ADMIN, adminFeatures);
    }
    
    /**
     * Validates if user has valid access to the system
     * @param user User to validate
     * @throws SecurityException if user is invalid
     */
    public static void validateUserAccess(User user) {
        if (user == null) {
            throw new SecurityException("User cannot be null");
        }
        
        if (user.getRole() == null) {
            throw new SecurityException("User role cannot be null");
        }
        
        if (!ROLE_FEATURES.containsKey(user.getRole())) {
            throw new SecurityException("Invalid user role: " + user.getRole());
        }
    }
    
    /**
     * Check if user has access to a specific feature
     * @param user User to check
     * @param feature Feature identifier
     * @return true if user has access
     */
    public static boolean hasFeatureAccess(User user, String feature) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        
        Set<String> roleFeatures = ROLE_FEATURES.get(user.getRole());
        if (roleFeatures == null) {
            return false;
        }
        
        return roleFeatures.contains(feature);
    }
    
    /**
     * Check if user has level-based access to a feature
     * @param user User to check
     * @param feature Feature identifier
     * @param requiredLevel Minimum level required
     * @return true if user has access
     */
    public static boolean hasLevelAccess(User user, String feature, int requiredLevel) {
        if (!hasFeatureAccess(user, feature)) {
            return false;
        }
        
        return user.getLevel() >= requiredLevel;
    }
    
    /**
     * Get user-friendly error message for denied access
     * @param user User attempting access
     * @param feature Feature being accessed
     * @return Friendly error message
     */
    public static String getAccessDeniedMessage(User user, String feature) {
        if (user == null) {
            return "Please log in to access this feature.";
        }
        
        // Level-gated features
        if ("request_mission".equals(feature)) {
            return "This feature unlocks at a higher level! Keep adventuring!";
        }
        
        // Role-based messages
        switch (user.getRole()) {
            case CHILD:
            case ELDER:
                return "This feature is for grown-ups only! Ask your parent or guardian.";
                
            case PARENT:
            case TEACHER:
                return "You do not have permission to view this section.";
                
            case ADMIN:
                return "This feature is temporarily unavailable.";
                
            default:
                return "Access denied.";
        }
    }
    
    /**
     * Get all features available to a user role
     * @param role User role
     * @return Set of available features
     */
    public static Set<String> getRoleFeatures(UserRole role) {
        return ROLE_FEATURES.getOrDefault(role, new HashSet<>());
    }
}
