package com.coincraft.ui.routing;

import com.coincraft.models.User;
import com.coincraft.ui.dashboards.AdminDashboard;
import com.coincraft.ui.dashboards.BaseDashboard;
import com.coincraft.ui.dashboards.ChildDashboard;
import com.coincraft.ui.dashboards.ParentDashboard;

import javafx.scene.Parent;

/**
 * Router class that handles navigation between different role-based dashboards
 * Follows the game theme and maintains consistent user experience
 */
public class DashboardRouter {
    private static DashboardRouter instance;
    private User currentUser;
    private BaseDashboard currentDashboard;
    
    private DashboardRouter() {}
    
    public static DashboardRouter getInstance() {
        if (instance == null) {
            instance = new DashboardRouter();
        }
        return instance;
    }
    
    /**
     * Routes to the appropriate dashboard based on user role
     * @param user The authenticated user
     * @return The dashboard UI root
     */
    public Parent routeToDashboard(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Validate user role
        RoleGuard.validateUserAccess(user);
        
        this.currentUser = user;
        
        // Create appropriate dashboard based on role
        switch (user.getRole()) {
            case CHILD:
            case ELDER:
                currentDashboard = new ChildDashboard(user);
                break;
                
            case PARENT:
                currentDashboard = new ParentDashboard(user);
                break;
                
            case TEACHER:
                // Teachers use Parent dashboard with additional features
                currentDashboard = new ParentDashboard(user);
                break;
                
            case ADMIN:
                currentDashboard = new AdminDashboard(user);
                break;
                
            default:
                throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        }
        
        return currentDashboard.getRoot();
    }
    
    /**
     * Navigate to a specific section within the current dashboard
     * @param section The section identifier
     */
    public void navigateToSection(String section) {
        if (currentDashboard != null) {
            currentDashboard.navigateToSection(section);
        }
    }
    
    /**
     * Get the current user
     * @return Current authenticated user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get the current dashboard instance
     * @return Current dashboard
     */
    public BaseDashboard getCurrentDashboard() {
        return currentDashboard;
    }
    
    /**
     * Check if user has access to a specific feature
     * @param feature Feature identifier
     * @return true if user has access
     */
    public boolean hasAccess(String feature) {
        return RoleGuard.hasFeatureAccess(currentUser, feature);
    }
    
    /**
     * Logout and clear current session
     */
    public void logout() {
        // Clear Firebase authentication state
        com.coincraft.services.FirebaseService firebaseService = com.coincraft.services.FirebaseService.getInstance();
        if (firebaseService != null) {
            firebaseService.clearAuthState();
        }
        
        currentUser = null;
        currentDashboard = null;
    }
}
