# CoinCraft - Interactive Gamified Financial Literacy Platform

## Overview

CoinCraft is an educational application that teaches children (ages 7-14) financial literacy through an engaging, gamified experience. Players explore "Treasure Town" and complete quests to learn essential money management skills including budgeting, saving, banking, investing, and social responsibility.

CoinCraft makes learning money fun for kids through a gamified experience. It uses interactive lessons, real-world tasks, and game elements like points, badges, quests, leaderboards, and avatars. Kids learn budgeting, saving, banking, investing, and social responsibility in an engaging, practical way.

## Features

### Core Features (MVP)
- **Role-Based Dashboards**: Separate interfaces for Children, Parents, and Administrators
- **Interactive Child Dashboard**: Avatar display, SmartCoin balance, progress tracking, gamified UI
- **Parent Management Portal**: Task validation, family analytics, child monitoring tools, shop management
- **Shop Management System**: Parents create products, children purchase with SmartCoins
  - Real image upload support for products
  - Complete CRUD operations (Create, Read, Update, Delete)
  - Firebase integration with local storage fallback
  - Unified product card design across dashboards
- **Task System**: Comprehensive chore tracking with real-time parent validation
  - Task assignment by parents
  - Completion evidence submission by children
  - Approval/rejection workflow with feedback
  - Task status tracking (Pending, Awaiting Approval, Approved, Rejected)
- **Level-Based Learning**: 10 progressive levels covering different financial topics
- **Gamification Elements**: Badges, achievements, leaderboards, daily streaks, pixel-themed UI
- **Avatar Customization**: 2D character customization with unlockable items and animations
- **Audio System**: Background music, sound effects, and audio feedback
- **Cross-Platform Support**: Windows launcher with automatic JavaFX setup

### Key Characters (NPCs)
- **Elder Pennywise**: Wise mentor and guide
- **Greta the Goalkeeper**: Savings goals expert
- **Captain Coinbeard**: Earning and chores leader
- **Bella the Banker**: Banking fundamentals teacher
- **Scarlet Shield**: Digital safety protector
- And 5 more specialized characters...

## Technical Architecture

### Technology Stack
- **Frontend**: JavaFX 19+ for rich desktop UI with modern styling
- **Backend**: Java 17+ with threading for real-time features
- **Database**: Firebase Firestore with REST API integration
- **Storage**: Firebase Storage with local fallback for images
- **Authentication**: Firebase Auth with Google OAuth support
- **Build Tool**: Gradle 8.5
- **Testing**: JUnit 5
- **Messaging**: WebSocket bridge for real-time notifications

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/coincraft/
│   │       ├── CoinCraftApplication.java    # Main application entry
│   │       ├── audio/                       # Audio system
│   │       │   ├── SoundManager.java
│   │       │   └── CentralizedMusicManager.java
│   │       ├── models/                      # Data models
│   │       │   ├── User.java, Task.java, Badge.java
│   │       │   ├── Product.java             # Shop product model
│   │       │   ├── Avatar.java, AvatarItem.java
│   │       │   └── UserRole.java, ValidationStatus.java
│   │       ├── services/                    # Business logic
│   │       │   ├── FirebaseService.java     # Main Firebase service
│   │       │   ├── FirebaseAuthService.java
│   │       │   ├── FirestoreService.java    # Firestore CRUD operations
│   │       │   ├── FirebaseStorageService.java
│   │       │   ├── FirebaseDataManager.java
│   │       │   └── RewardService.java
│   │       └── ui/                          # User interface
│   │           ├── dashboards/              # Role-based dashboards
│   │           │   ├── ChildDashboard.java  # Task panel, shop browsing
│   │           │   ├── ParentDashboard.java # Task validation, shop management
│   │           │   └── AdminDashboard.java
│   │           ├── components/              # UI components
│   │           │   ├── child/               # Child-specific components
│   │           │   │   ├── TaskCardList.java
│   │           │   │   ├── TaskCompletionDialog.java
│   │           │   │   └── ShopPage.java
│   │           │   ├── parent/              # Parent-specific components
│   │           │   │   ├── AddProductDialog.java
│   │           │   │   ├── EditProductDialog.java
│   │           │   │   └── ParentSidebar.java
│   │           │   ├── shared/              # Shared components
│   │           │   │   └── ProductCard.java # Unified product card
│   │           │   └── theme/               # Pixel-themed components
│   │           ├── routing/                 # Navigation system
│   │           └── LoginScreen.java, RegistrationScreen.java
│   └── resources/
│       ├── styles/                          # CSS styling
│       ├── images/                          # Visual assets & GIFs
│       ├── sounds/                          # Audio files (WAV, MP3)
│       ├── fonts/                           # Custom pixel fonts
│       └── firebase-config.json             # Firebase configuration
└── test/
    └── java/                                # Unit tests
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle 8.5+ (wrapper included)
- JavaFX 19+ (included in dependencies)

### Installation and Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/SifatAli008/Coin-Craft.git
   cd Coin-Craft
   ```

2. **Build the project**
   ```bash
   gradlew build
   ```

3. **Run the application**
   
   **Option A: Using the launcher (Recommended for Windows)**
   ```bash
   launch-coincraft.bat
   ```
   
   **Option B: Using Gradle**
   ```bash
   gradlew run
   ```

4. **Run tests**
   ```bash
   gradlew test
   ```

### Development Mode

For development, the application runs in mock mode with:
- Simulated Firebase authentication with role-based access
- Sample user data, tasks, and family structures
- Mock leaderboard and analytics data
- Automated launcher with JavaFX environment detection
- Audio system with sample sound effects and music

### WebSocket Bridge (Realtime Messaging)

A lightweight WebSocket bridge is included at `server/ws-bridge/` for multi-device realtime messaging.

Deploy locally:

```bash
cd server/ws-bridge
npm install
npm start   # listens on ws://localhost:8123
```

Docker:

```bash
docker build -t coincraft-ws server/ws-bridge
docker run -d --name coincraft-ws -p 8123:8123 coincraft-ws
```

App configuration:

- Env: `COINCRAFT_WS_URL=ws://YOUR_SERVER:8123`
- Or JVM arg: `-Dcoincraft.ws.url=ws://YOUR_SERVER:8123`

The app will connect to this URL for realtime messaging events.

## Game Progression

### Level Structure
1. **Treasure Chest Awakens** - Budget basics (needs vs wants)
2. **Earners' Guild** - Chore tracking and income
3. **Goal Garden** - Savings goals and delayed gratification
4. **Budget Workshop** - Advanced budgeting
5. **Bank Vault** - Banking fundamentals
6. **Shield of Safety** - Digital safety
7. **Investment Island** - Investment basics
8. **Inflation Fog** - Understanding inflation
9. **Emergency Fortress** - Emergency funds
10. **Giving Tree & Village Hall** - Social responsibility

### Progression System
- **SmartCoins**: Virtual currency earned through tasks
- **Badges**: Achievement recognition system
- **Levels**: Unlock new content and features
- **Avatar Items**: Customization rewards
- **Daily Streaks**: Consistency rewards

## Task Assignment Workflow

### Task Creation & Assignment
Parents can create and assign tasks to their children:
1. **Create Task**: Parents define task details, rewards, and deadlines
2. **Assign to Child**: Task appears in child's task dashboard
3. **Task Visibility**: Children see all assigned tasks with clear status indicators

### Task Completion Process
Children complete tasks and submit evidence:
1. **View Tasks**: See all assigned tasks with details and rewards
2. **Complete Task**: Click "Complete Task" button
3. **Submit Evidence**: Provide completion notes and proof
4. **Awaiting Review**: Task status changes to "Awaiting Approval"

### Parent Validation
Parents review and approve/reject completed tasks:
1. **Review Queue**: See all tasks awaiting approval
2. **Approve Tasks**: Award SmartCoins and mark as complete
3. **Reject Tasks**: Provide feedback for improvement
4. **Re-submission**: Children can resubmit rejected tasks

### Task Status System
- **Pending**: Task assigned but not yet completed
- **Awaiting Approval**: Task completed, waiting for parent review
- **Approved**: Task approved, SmartCoins awarded
- **Rejected**: Task needs improvement, feedback provided
- **Auto-Approved**: Automatic approval for certain task types

## Shop Management System

### For Parents (Merchants)
Parents can manage a personalized shop for their children:
- **Add Products**: Create custom rewards with name, description, price, and image
- **Edit Products**: Update product details, pricing, and images anytime
- **Toggle Status**: Activate or deactivate products from the shop
- **Delete Products**: Remove products with confirmation dialog
- **Image Support**: Upload real images or use emojis for product icons
- **Firebase Integration**: Products automatically sync with cloud storage

### For Children (Adventurers)
Children can browse and purchase from their parent's shop:
- **Browse Products**: View all active products in a modern grid layout
- **Purchase Items**: Buy products using earned SmartCoins
- **Visual Feedback**: See product images, descriptions, and prices
- **Real-Time Updates**: Shop reflects parent changes immediately
- **Balance Tracking**: SmartCoin balance updates after purchases

### Product Card Features
- **Large Product Images**: 150x150px containers with 120x120px image display
- **Inline Title & Status**: Product name and active status side by side
- **Modern Design**: Clean white cards with subtle shadows and rounded corners
- **Responsive Buttons**: Different actions based on dashboard (parent vs child)
- **Unified UI/UX**: Consistent design across both dashboards

## User Roles

- **Child (7-14)**: Primary player, completes tasks and learns
- **Parent/Guardian**: Validates real-world tasks, monitors progress
- **Teacher/Educator**: Classroom integration and group challenges
- **Admin**: Content management and system administration

## Development Timeline (MVP)

This MVP has been significantly expanded with comprehensive features:
- ✅ Core project structure and models
- ✅ Role-based dashboard system (Child, Parent, Admin)
- ✅ Firebase integration with Firestore REST API
- ✅ Shop management system with product CRUD operations
- ✅ Task assignment workflow with parent validation
- ✅ Comprehensive UI component library with modern styling
- ✅ Audio system with sound effects and background music
- ✅ Parent validation and task management system
- ✅ Child-focused gamified interface with avatars and badges
- ✅ Family analytics and monitoring tools
- ✅ Settings and configuration management
- ✅ Pixel-themed custom UI components
- ✅ Image upload and display for products
- ✅ Cross-platform launcher with JavaFX auto-setup
- ✅ WebSocket bridge for real-time messaging

## Recent Updates

### Version 2.0 - Shop Management System (October 2025)
- ✨ Complete shop management for parents
- ✨ Product CRUD with Firebase/Firestore integration
- ✨ Real image upload and display support
- ✨ Enhanced product cards with larger images (150x150px)
- ✨ Inline title and status layout
- ✨ Edit and delete product dialogs
- ✨ Confirmation dialogs for destructive actions
- ✨ Unified ProductCard component

### Version 1.5 - Task Assignment Workflow (October 2025)
- ✨ Task assignment by parents to children
- ✨ Task completion with evidence submission
- ✨ Parent review and approval system
- ✨ Task status tracking (Pending, Awaiting Approval, Approved, Rejected)
- ✨ Enhanced task panel UI with modern styling
- ✨ Removed quest terminology, switched to task-focused language

## Future Enhancements

- Full Firebase Admin SDK integration
- Mobile app versions (iOS/Android)
- Advanced multiplayer features
- Content management system for educators
- Enhanced analytics and reporting dashboards
- Additional languages support
- Marketplace for sharing custom content
- Integration with real banking APIs (future phase)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## Design Principles

- **Age-Appropriate**: Different UI complexity for ages 7-10 vs 11-14
- **Engaging**: Colorful, animated, story-driven experience
- **Educational**: Real financial concepts with practical application
- **Safe**: Secure, privacy-focused, partner-validated tasks
- **Accessible**: Support for different learning styles and abilities

## License

This project is part of an educational demonstration. See LICENSE file for details.

## Contact

For questions about this project, please refer to the documentation or create an issue in the repository.
