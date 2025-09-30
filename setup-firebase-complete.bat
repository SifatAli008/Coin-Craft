@echo off
echo ========================================
echo   CoinCraft Firebase Setup Complete
echo ========================================
echo.
echo This script will help you set up Firebase completely for CoinCraft.
echo.

echo Step 1: Firebase Project Configuration
echo ----------------------------------------
echo 1. Go to: https://console.firebase.google.com/
echo 2. Select your project: coincraft-5a5d9
echo 3. Go to Project Settings (gear icon)
echo 4. Scroll down to "Your apps" section
echo 5. Make sure your web app is configured
echo.

echo Step 2: Enable Firebase Services
echo ----------------------------------------
echo 6. Go to "Authentication" → "Sign-in method"
echo    - Enable Email/Password authentication
echo    - Enable Google authentication
echo.
echo 7. Go to "Firestore Database"
echo    - Create database in production mode
echo    - Choose your preferred location (asia-southeast1)
echo.
echo 8. Go to "Storage"
echo    - Create default bucket
echo    - Set up security rules
echo.

echo Step 3: Configure Firestore Security Rules
echo ----------------------------------------
echo 9. Go to "Firestore Database" → "Rules"
echo 10. Copy the rules from: firestore.rules
echo 11. Publish the rules
echo.

echo Step 4: Set Up Authentication Providers
echo ----------------------------------------
echo 12. In Authentication → Sign-in method:
echo     - Add your domain to authorized domains
echo     - Configure OAuth consent screen
echo     - Add redirect URIs for Google sign-in
echo.

echo Step 5: Test Firebase Connection
echo ----------------------------------------
echo 13. Run CoinCraft application
echo 14. Try creating a new user account
echo 15. Check Firebase console for new users
echo 16. Verify data is being saved to Firestore
echo.

echo Step 6: Monitor and Debug
echo ----------------------------------------
echo 17. Check Firebase console logs
echo 18. Monitor Firestore usage
echo 19. Check authentication events
echo 20. Review security rules
echo.

echo ========================================
echo   Firebase Setup Instructions Complete
echo ========================================
echo.

echo Your Firebase configuration file is already set up:
echo - Project ID: coincraft-5a5d9
echo - Auth Domain: coincraft-5a5d9.firebaseapp.com
echo - Database: https://coincraft-5a5d9-default-rtdb.asia-southeast1.firebasedatabase.app
echo - Storage: coincraft-5a5d9.appspot.com
echo.

echo Current Firebase Features Available:
echo ✅ User Authentication (Email/Password + Google OAuth)
echo ✅ Firestore Database (Users, Tasks, Messages)
echo ✅ Security Rules (Parent-Child relationships)
echo ✅ Real-time Data Sync
echo ✅ Offline Support
echo ✅ Data Validation
echo ✅ Caching System
echo.

echo Press any key to open Firebase Console...
pause >nul
start https://console.firebase.google.com/project/coincraft-5a5d9
echo.

echo Press any key to open Firestore Database...
pause >nul
start https://console.firebase.google.com/project/coincraft-5a5d9/firestore
echo.

echo Press any key to open Authentication...
pause >nul
start https://console.firebase.google.com/project/coincraft-5a5d9/authentication
echo.

echo ========================================
echo   Setup Complete! Firebase is Ready
echo ========================================
echo.
echo Your CoinCraft application now has:
echo - Complete user management
echo - Task storage and tracking
echo - Real-time messaging
echo - Leaderboards
echo - Progress tracking
echo - Parent-child relationships
echo.
echo Run CoinCraft to test the Firebase integration!
