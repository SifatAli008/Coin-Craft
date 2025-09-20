@echo off
echo ==============================================
echo CoinCraft Parent Login Test
echo ==============================================
echo.

echo Checking for JavaFX...
if exist "D:\openjfx-21.0.8_windows-x64_bin-sdk\javafx-sdk-21.0.8\lib" (
    set JAVAFX_PATH=D:\openjfx-21.0.8_windows-x64_bin-sdk\javafx-sdk-21.0.8\lib
) else (
    echo JavaFX not found!
    pause
    exit /b 1
)

echo Compiling and running Parent Login Test...
set MAIN_CLASS=com.coincraft.ui.ParentLoginTest
set CLASSPATH=target\classes

REM Build Maven dependencies classpath
for /f "delims=" %%i in ('mvn dependency:build-classpath -Dmdep.outputFile=classpath.tmp -q') do set MAVEN_CLASSPATH=%%i
if exist classpath.tmp (
    set /p MAVEN_CLASSPATH=<classpath.tmp
    del classpath.tmp
)
set CLASSPATH=target\classes;%MAVEN_CLASSPATH%

set "JAVA21=C:\Program Files\Java\jdk-21\bin\java.exe"
if exist "%JAVA21%" (
    set "SELECTED_JAVA=%JAVA21%"
) else (
    set "SELECTED_JAVA=java"
)

echo Running Parent Login Test...
"%SELECTED_JAVA%" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "%CLASSPATH%" %MAIN_CLASS%

echo.
echo Test completed.
pause
