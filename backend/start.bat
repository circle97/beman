@echo off
echo ========================================
echo BeMan Backend Startup Script
echo ========================================

set JAVA_HOME=D:\software\jdk\install
set MAVEN_HOME=D:\software\maven\apache-maven-3.8.6
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo MAVEN_HOME: %MAVEN_HOME%

echo.
echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not properly installed or configured!
    pause
    exit /b 1
)

echo.
echo Checking Maven installation...
mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not properly installed or configured!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Building project...
echo ========================================
mvn clean install
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build successful! Starting Spring Boot application...
echo ========================================
echo Note: Make sure MySQL is running on localhost:3306
echo Database: beman
echo Username: root
echo Password: Lq@986455353
echo.
mvn spring-boot:run

pause 