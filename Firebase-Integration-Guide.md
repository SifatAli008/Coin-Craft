# CoinCraft Firebase Integration Guide

## Overview
CoinCraft uses Firebase for comprehensive data storage, authentication, and real-time features. This guide covers the complete Firebase setup and usage.

## Firebase Services Used

### 1. Firebase Authentication
- **Email/Password Authentication**: Standard user registration and login
- **Google OAuth**: One-click Google sign-in integration
- **User Management**: Secure user sessions and token management

### 2. Cloud Firestore
- **User Profiles**: Complete user data storage
- **Tasks**: Task management and progress tracking
- **Messages**: Real-time messaging between parents and children
- **Leaderboards**: Global user rankings
- **Badges**: Achievement system

### 3. Firebase Security Rules
- **Parent-Child Relationships**: Secure data access based on family connections
- **Role-Based Access**: Different permissions for children, parents, teachers, and admins
- **Data Validation**: Automatic validation of user and task data

## Current Firebase Configuration

### Project Details
- **Project ID**: `coincraft-5a5d9`
- **Auth Domain**: `coincraft-5a5d9.firebaseapp.com`
- **Database URL**: `https://coincraft-5a5d9-default-rtdb.asia-southeast1.firebasedatabase.app`
- **Storage Bucket**: `coincraft-5a5d9.appspot.com`

### Collections Structure
```
/users/{userId}
  - userId: string
  - name: string
  - username: string
  - email: string
  - role: enum (CHILD, PARENT, TEACHER, ADMIN)
  - age: number
  - level: number
  - smartCoinBalance: number
  - experiencePoints: number
  - parentId: string (for children)
  - createdAt: timestamp
  - lastLogin: timestamp

/tasks/{taskId}
  - taskId: string
  - description: string
  - assignedBy: string (parent/teacher ID)
  - assignedTo: string (child ID)
  - completed: boolean
  - rewardCoins: number
  - validationStatus: enum (PENDING, APPROVED, REJECTED)
  - type: enum (SAVINGS, SPENDING, INVESTING, etc.)
  - difficultyLevel: number
  - deadline: timestamp
  - createdAt: timestamp
  - completedAt: timestamp
  - completionNotes: string

/messages/{messageId}
  - messageId: string
  - conversationId: string (parentId_childId)
  - senderId: string
  - senderName: string
  - recipientId: string
  - recipientName: string
  - content: string
  - timestamp: timestamp

/badges/{badgeId}
  - badgeId: string
  - name: string
  - description: string
  - category: enum
  - level: enum
  - requirements: object
  - iconUrl: string

/leaderboard/{userId}
  - userId: string
  - rank: number
  - smartCoinBalance: number
  - level: number
  - lastUpdated: timestamp
```

## Implementation Details

### FirebaseDataManager
The `FirebaseDataManager` class provides high-level operations:

```java
// Save user data
CompletableFuture<Boolean> saveUserAsync(User user)

// Load user data with caching
CompletableFuture<User> loadUserAsync(String userId)

// Save task
CompletableFuture<Boolean> saveTaskAsync(Task task)

// Load user tasks
CompletableFuture<List<Task>> loadUserTasksAsync(String userId)

// Update user progress
CompletableFuture<Boolean> updateUserProgressAsync(String userId, int coins, int xp)

// Complete task and award rewards
CompletableFuture<Boolean> completeTaskAsync(Task task, String notes)

// Get leaderboard
CompletableFuture<List<User>> getLeaderboardAsync(int limit)
```

### Key Features
1. **Automatic Caching**: Reduces Firebase calls and improves performance
2. **Offline Support**: Works without internet connection
3. **Data Validation**: Ensures data integrity before saving
4. **Async Operations**: Non-blocking database operations
5. **Error Handling**: Comprehensive error management
6. **Real-time Sync**: Automatic data synchronization

## Security Rules

### User Access
- Users can read/write their own data
- Parents can read their children's data
- Teachers can read students' data
- Admins have full access

### Data Validation
- User age must be between 7-100
- User role must be valid enum
- Task data must have required fields
- Automatic timestamp management

## Setup Instructions

### 1. Firebase Console Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select project `coincraft-5a5d9`
3. Enable Authentication (Email/Password + Google)
4. Create Firestore database
5. Set up Storage bucket
6. Deploy security rules

### 2. Authentication Configuration
1. Enable Email/Password authentication
2. Configure Google OAuth with your OAuth credentials
3. Add authorized domains
4. Set up OAuth consent screen

### 3. Firestore Setup
1. Create database in production mode
2. Choose location (asia-southeast1)
3. Deploy security rules from `firestore.rules`
4. Set up indexes for queries

### 4. Testing
Run the Firebase test utility:
```java
FirebaseTestUtil.runFirebaseTests();
```

## Monitoring and Analytics

### Firebase Console Monitoring
- **Authentication**: User sign-ups, sign-ins, and failures
- **Firestore**: Read/write operations and errors
- **Performance**: Database query performance
- **Security**: Security rule violations

### Application Logging
- Connection status monitoring
- Data operation success/failure rates
- Cache hit/miss statistics
- Error tracking and debugging

## Troubleshooting

### Common Issues
1. **Authentication Failures**: Check OAuth configuration
2. **Permission Denied**: Verify security rules
3. **Network Errors**: Check internet connection
4. **Data Sync Issues**: Clear cache and retry

### Debug Mode
Enable debug logging by setting log level to FINE:
```java
Logger.getLogger("com.coincraft.services").setLevel(Level.FINE);
```

## Performance Optimization

### Caching Strategy
- User data cached for 5 minutes
- Tasks cached for 2 minutes
- Automatic cache invalidation
- Manual cache clearing available

### Query Optimization
- Use indexes for complex queries
- Limit query results
- Batch operations when possible
- Offline-first approach

## Future Enhancements

### Planned Features
1. **Real-time Listeners**: Live data updates
2. **Batch Operations**: Bulk data operations
3. **Advanced Analytics**: User behavior tracking
4. **Push Notifications**: Task reminders and achievements
5. **File Storage**: Avatar images and documents

### Scalability
- Automatic scaling with Firebase
- Global CDN for performance
- Multi-region deployment ready
- Enterprise-grade security

## Support and Maintenance

### Regular Tasks
1. Monitor Firebase usage and costs
2. Update security rules as needed
3. Review and optimize queries
4. Backup critical data
5. Update Firebase SDK versions

### Emergency Procedures
1. Switch to offline mode if needed
2. Restore from backups if data corruption
3. Contact Firebase support for critical issues
4. Implement fallback mechanisms

---

**Last Updated**: December 2024  
**Firebase Project**: coincraft-5a5d9  
**Version**: 1.0.0

