@echo off
echo ========================================
echo    CoinCraft Firebase Setup Script
echo ========================================
echo.

echo Checking Firebase configuration...

REM Check if firebase-config.json exists
if exist "src\main\resources\firebase-config.json" (
    echo ✅ Firebase config file found
) else (
    echo ❌ Firebase config file not found
    echo Please download firebase-config.json from Firebase Console
    echo and place it in src\main\resources\
    pause
    exit /b 1
)

REM Check if service account file exists
if exist "src\main\resources\firebase-service-account.json" (
    echo ✅ Service account file found
) else (
    echo ⚠️ Service account file not found
    echo.
    echo To enable Firebase Admin SDK features:
    echo 1. Go to Firebase Console → Project Settings → Service Accounts
    echo 2. Generate new private key
    echo 3. Download the JSON file
    echo 4. Rename it to firebase-service-account.json
    echo 5. Place it in src\main\resources\
    echo.
    echo The application will work without this file using REST API mode.
    echo.
)

echo.
echo Testing Firebase connection...
echo.

REM Compile and test Firebase connection
echo Compiling project...
call mvn clean compile -q

if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful
echo.

echo Running Firebase connection test...
echo.

REM Run the application to test Firebase connection
echo Starting CoinCraft to test Firebase connection...
echo Look for Firebase initialization messages in the console.
echo.

call mvn javafx:run -Djavafx.args="--test-firebase"

echo.
echo ========================================
echo    Firebase Setup Complete
echo ========================================
echo.
echo Next steps:
echo 1. Check Firebase Console to verify data is being saved
echo 2. Test user registration and login
echo 3. Verify Firestore collections are created
echo 4. Check Firebase Storage for uploaded files
echo.
echo For detailed setup instructions, see firebase-setup-guide.md
echo.
pause
