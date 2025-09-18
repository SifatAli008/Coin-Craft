# CoinCraft - Interactive Gamified Financial Literacy Platform

## Overview

CoinCraft is an educational application that teaches children (ages 7-14) financial literacy through an engaging, gamified experience. Players explore "Treasure Town" and complete quests to learn essential money management skills including budgeting, saving, banking, investing, and social responsibility.

CoinCraft makes learning money fun for kids through a gamified experience. It uses interactive lessons, real-world tasks, and game elements like points, badges, quests, leaderboards, and avatars. Kids learn budgeting, saving, banking, investing, and social responsibility in an engaging, practical way.

## Features

### Core Features (MVP)
- **Interactive Dashboard**: Avatar display, SmartCoin balance, progress tracking
- **Level-Based Learning**: 10 progressive levels covering different financial topics
- **Gamification Elements**: Badges, achievements, leaderboards, daily streaks
- **Task System**: Chore tracking with partner validation
- **Avatar Customization**: 2D character customization with unlockable items

### Key Characters (NPCs)
- **Elder Pennywise**: Wise mentor and guide
- **Greta the Goalkeeper**: Savings goals expert
- **Captain Coinbeard**: Earning and chores leader
- **Bella the Banker**: Banking fundamentals teacher
- **Scarlet Shield**: Digital safety protector
- And 5 more specialized characters...

## Technical Architecture

### Technology Stack
- **Frontend**: JavaFX 19+ for rich desktop UI
- **Backend**: Java 17+ with threading for real-time features
- **Database**: Firebase Firestore (mock implementation for MVP)
- **Authentication**: Firebase Auth (mock implementation for MVP)
- **Build Tool**: Maven
- **Testing**: JUnit 5

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/coincraft/
│   │       ├── CoinCraftApplication.java    # Main application entry
│   │       ├── models/                      # Data models
│   │       │   ├── User.java
│   │       │   ├── Task.java
│   │       │   ├── Badge.java
│   │       │   └── Avatar.java
│   │       ├── services/                    # Business logic
│   │       │   └── FirebaseService.java
│   │       └── ui/                          # User interface
│   │           ├── MainDashboard.java
│   │           └── components/
│   └── resources/
│       ├── styles/
│       │   └── coincraft-styles.css
│       └── images/                          # Visual assets
└── test/
    └── java/                                # Unit tests
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- JavaFX 19+ (included in dependencies)

### Installation and Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/SifatAli008/Coin-Craft.git
   cd Coin-Craft
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

### Development Mode

For development, the application runs in mock mode with:
- Simulated Firebase authentication
- Sample user data and tasks
- Mock leaderboard data

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

## User Roles

- **Child (7-14)**: Primary player, completes tasks and learns
- **Parent/Guardian**: Validates real-world tasks, monitors progress
- **Teacher/Educator**: Classroom integration and group challenges
- **Admin**: Content management and system administration

## Development Timeline (MVP)

This MVP was developed as a 2-3 day demonstration focusing on:
- ✅ Core project structure and models
- ✅ Main dashboard with all components
- ✅ Firebase integration (mock implementation)
- ✅ Basic UI components and styling
- ⏳ Level 1 implementation (Budget Basics)
- ⏳ Partner validation system
- ⏳ Basic admin interface

## Future Enhancements

- Real Firebase integration
- Mobile app versions (iOS/Android)
- Advanced multiplayer features
- Content management system
- Analytics and reporting
- Additional languages support

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
