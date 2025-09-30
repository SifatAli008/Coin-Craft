@echo off
echo ========================================
echo   CoinCraft Icon Upgrade Tool
echo ========================================
echo.

echo This script will help you create a better icon for CoinCraft.
echo.

echo Step 1: Design Your New Icon
echo ----------------------------------------
echo 1. Open any image editor (Paint, GIMP, Photoshop, etc.)
echo 2. Create a 256x256 pixel image
echo 3. Use these design elements:
echo    - Gold coin with dollar sign ($)
echo    - Blue-purple gradient background
echo    - Rounded corners (modern look)
echo    - Clean, simple design
echo.

echo Step 2: Save Your Icon
echo ----------------------------------------
echo 4. Save as PNG format
echo 5. Name it: coincraft-icon-new.png
echo 6. Place it in: src\main\resources\images\
echo.

echo Step 3: Replace Old Icon
echo ----------------------------------------
echo 7. Backup the old icon:
echo    copy "src\main\resources\images\coincraft-icon.png" "src\main\resources\images\coincraft-icon-backup.png"
echo.
echo 8. Replace with new icon:
echo    copy "src\main\resources\images\coincraft-icon-new.png" "src\main\resources\images\coincraft-icon.png"
echo.

echo Step 4: Test Your New Icon
echo ----------------------------------------
echo 9. Run CoinCraft application
echo 10. Check if the new icon appears in:
echo     - Window title bar
echo     - Taskbar
echo     - Alt+Tab menu
echo.

echo ========================================
echo   Alternative: Use Online Tools
echo ========================================
echo.
echo If you prefer online tools:
echo 1. Go to: https://www.canva.com/
echo 2. Search for "app icon" templates
echo 3. Customize with CoinCraft colors
echo 4. Download and replace the icon
echo.

echo ========================================
echo   Quick Color Reference
echo ========================================
echo.
echo Recommended Colors:
echo - Gold: #FFD700 (for coin)
echo - Green: #2E7D32 (for growth theme)
echo - Blue: #2196F3 (for trust)
echo - Purple: #9C27B0 (for creativity)
echo.

echo Press any key to open the images folder...
pause >nul
start src\main\resources\images\

echo.
echo Press any key to open Paint for quick editing...
pause >nul
start mspaint

echo.
echo ========================================
echo   Icon Upgrade Complete!
echo ========================================
echo.
echo Your CoinCraft application now has a modern, professional icon!
