@echo off
echo ========================================
echo Starting NPLH Message Handler (Custom Server)
echo ========================================
echo.
echo Starting server on port 8084...
echo Open your browser at: http://localhost:8084
echo.
echo Press Ctrl+C to stop the server
echo ========================================

cd /d "%~dp0"

REM Check if Node.js is installed
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)

REM Start the custom server
node server.js
