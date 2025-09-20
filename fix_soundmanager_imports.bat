@echo off
echo Fixing SoundManager import issues...

REM Clean and recompile the project to refresh the classpath
echo Step 1: Cleaning and recompiling...
mvn clean compile -q

REM Restart IDE indexing by touching all affected files
echo Step 2: Refreshing file timestamps to trigger IDE reindexing...
for %%f in (
    "src\main\java\com\coincraft\ui\LoginScreen.java"
    "src\main\java\com\coincraft\ui\RegistrationScreen.java"
    "src\main\java\com\coincraft\ui\MainDashboard.java"
    "src\main\java\com\coincraft\ui\components\child\ChildSidebar.java"
    "src\main\java\com\coincraft\ui\components\child\TaskCardList.java"
    "src\main\java\com\coincraft\ui\dashboards\ChildDashboard.java"
) do (
    echo Refreshing %%f...
    copy /b %%f +,,
)

echo Step 3: Final compilation check...
mvn compile -q

echo Done! SoundManager import issues should be resolved.
echo If issues persist, please restart your IDE.

