@echo off
echo ========================================
echo Starting NPLH Message Handler (Python)
echo ========================================
echo.
echo This version uses Python's built-in HTTP server
echo Starting server on port 8084...
echo Open your browser at: http://localhost:8084
echo.
echo Press Ctrl+C to stop the server
echo ========================================

cd /d "%~dp0"
echo Current directory: %CD%
echo.

REM Check if dist folder exists
echo [1/3] Checking dist folder...
if not exist "dist" (
    echo ERROR: 'dist' folder not found in current directory
    echo Current directory: %CD%
    echo Please make sure the 'dist' folder is in the same directory as this script
    pause
    exit /b 1
)
echo ✅ dist folder found

REM Check if Python is installed
echo [2/3] Checking Python installation...
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Python not found, trying python3...
    python3 --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo ERROR: Python is not installed or not in PATH
        echo Please install Python from https://www.python.org/
        pause
        exit /b 1
    ) else (
        set PYTHON_CMD=python3
        echo ✅ Python3 found: 
        python3 --version
    )
) else (
    set PYTHON_CMD=python
    echo ✅ Python found: 
    python --version
)

echo [3/3] Starting Python HTTP Server...
echo.
echo Server will start on: http://localhost:8084
echo Serving files from: %CD%\dist
echo.
echo Opening browser automatically...
start http://localhost:8084
echo.

cd dist
echo Starting server now...
%PYTHON_CMD% -m http.server 8084

echo.
echo ========================================
echo Server stopped
echo ========================================
pause
