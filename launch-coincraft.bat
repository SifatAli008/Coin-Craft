@echo off
echo ==============================================
echo CoinCraft Launcher - JavaFX Auto-Setup
echo ==============================================
echo.


echo Checking for JavaFX...
REM Prefer user-provided JavaFX 21 path
if exist "D:\openjfx-21.0.8_windows-x64_bin-sdk\javafx-sdk-21.0.8\lib" (
    echo JavaFX found at D:\openjfx-21.0.8_windows-x64_bin-sdk\javafx-sdk-21.0.8\lib
    set JAVAFX_PATH=D:\openjfx-21.0.8_windows-x64_bin-sdk\javafx-sdk-21.0.8\lib
) else if exist "D:\javafx-sdk-25\lib" (
    echo JavaFX found at D:\javafx-sdk-25\lib
    set JAVAFX_PATH=D:\javafx-sdk-25\lib
) else if exist "javafx-sdk\lib" (
    echo JavaFX found at javafx-sdk\lib
    set JAVAFX_PATH=javafx-sdk\lib
) else (
    echo JavaFX not found in expected locations.
    echo Please ensure JavaFX SDK is installed.
    pause
    exit /b 1
)

echo Starting CoinCraft with JavaFX...
echo.
set MAIN_CLASS=com.coincraft.CoinCraftApplication
REM Build Maven dependencies classpath
for /f "delims=" %%i in ('mvn dependency:build-classpath -Dmdep.outputFile=classpath.tmp -q') do set MAVEN_CLASSPATH=%%i
if exist classpath.tmp (
    set /p MAVEN_CLASSPATH=<classpath.tmp
    del classpath.tmp
)
set CLASSPATH=target\classes;%MAVEN_CLASSPATH%

REM Prefer Java 23+ for JavaFX 25 (class file version 67); Java 21 for JavaFX 21
set "JAVA23=C:\Program Files\Java\jdk-23\bin\java.exe"
set "JAVA22=C:\Program Files\Java\jdk-22\bin\java.exe"
set "JAVA21=C:\Program Files\Java\jdk-21\bin\java.exe"

set "SELECTED_JAVA="
if exist "%JAVA23%" set "SELECTED_JAVA=%JAVA23%"
if "%SELECTED_JAVA%"=="" if exist "%JAVA22%" set "SELECTED_JAVA=%JAVA22%"
if "%SELECTED_JAVA%"=="" if exist "%JAVA21%" set "SELECTED_JAVA=%JAVA21%"
if "%SELECTED_JAVA%"=="" set "SELECTED_JAVA=java"

echo Using Java: "%SELECTED_JAVA%"
echo JavaFX path: "%JAVAFX_PATH%"
echo Running command...
"%SELECTED_JAVA%" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "%CLASSPATH%" %MAIN_CLASS%

echo.
echo ==============================================
echo CoinCraft session ended
echo ==============================================
pause
