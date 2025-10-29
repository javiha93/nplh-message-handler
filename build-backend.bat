@echo off
echo ========================================
echo Building Backend Only
echo ========================================

echo.
echo Building Backend...
echo ========================================
cd /d "%~dp0"
cd npl-message-handler-back
call mvn clean compile package
if %errorlevel% neq 0 (
    echo ERROR: Backend build failed
    pause
    exit /b 1
)

echo.
echo ========================================
echo ✅ BACKEND BUILD COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo JAR file is in: npl-message-handler-back/target/
echo.
pause
