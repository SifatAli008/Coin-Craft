@echo off
echo ========================================
echo   CoinCraft Google OAuth Setup
echo ========================================
echo.
echo This script will help you set up Google OAuth for CoinCraft.
echo.
echo Step 1: Go to Google Cloud Console
echo ----------------------------------------
echo 1. Open your browser and go to: https://console.cloud.google.com/
echo 2. Create a new project or select an existing one
echo 3. Enable the following APIs:
echo    - Google+ API
echo    - People API
echo.
echo Step 2: Create OAuth Credentials
echo ----------------------------------------
echo 4. Go to "Credentials" in the left sidebar
echo 5. Click "Create Credentials" → "OAuth 2.0 Client ID"
echo 6. Choose "Desktop Application"
echo 7. Add these redirect URIs:
echo    - http://localhost:8888/Callback
echo    - http://localhost:8889/Callback
echo    - http://localhost:8890/Callback
echo    - http://localhost:9000/Callback
echo    - http://localhost:9001/Callback
echo.
echo Step 3: Configure CoinCraft
echo ----------------------------------------
echo 8. Copy your Client ID and Client Secret
echo 9. Edit the file: src\main\resources\google-oauth-config.properties
echo 10. Replace the DISABLED_ values with your actual credentials
echo.
echo Example configuration:
echo google.oauth.client.id=123456789-abcdefghijklmnop.apps.googleusercontent.com
echo google.oauth.client.secret=GOCSPX-abcdefghijklmnopqrstuvwx
echo.
echo Step 4: Test the Setup
echo ----------------------------------------
echo 11. Run CoinCraft again
echo 12. Try the Google Sign-In button
echo.
echo ========================================
echo   Setup Instructions Complete
echo ========================================
echo.
echo Press any key to open Google Cloud Console...
pause >nul
start https://console.cloud.google.com/
echo.
echo Press any key to open the configuration file...
pause >nul
notepad src\main\resources\google-oauth-config.properties
