@echo off
echo ========================================
echo Starting NPLH Message Handler (Python)
echo ========================================
echo.
echo Starting server on port 8084...
echo Open your browser at: http://localhost:8084
echo.
echo Press Ctrl+C to stop the server
echo ========================================

cd /d "%~dp0"

REM Check if Python is installed
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Python is not installed or not in PATH
    echo Please install Python from https://www.python.org/
    pause
    exit /b 1
)

REM Start the server using Python
cd dist
python -m http.server 8084
