@echo off
echo ========================================
echo Starting NPLH Message Handler
echo ========================================
echo.
echo Starting server on port 8084...
echo Open your browser at: http://localhost:8084
echo.
echo Press Ctrl+C to stop the server
echo ========================================

cd /d "%~dp0"
echo Current directory: %CD%
echo.

REM Check if dist folder exists
echo [1/6] Checking dist folder...
if not exist "dist" (
    echo ERROR: 'dist' folder not found in current directory
    echo Current directory: %CD%
    echo Please make sure the 'dist' folder is in the same directory as this script
    dir /b
    pause
    exit /b 1
)
echo ✅ dist folder found

REM Check if Node.js is installed
echo [2/6] Checking Node.js installation...
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)
echo ✅ Node.js found: 
node --version

REM Check if port 8084 is already in use
echo [3/6] Checking if port 8084 is available...
netstat -an | findstr :8084 >nul
if %errorlevel% equ 0 (
    echo ⚠️  WARNING: Port 8084 might be in use by another process
    echo Processes using port 8084:
    netstat -ano | findstr :8084
    echo Attempting to continue anyway...
) else (
    echo ✅ Port 8084 is available
)

REM Install http-server if not already installed
echo [4/6] Checking http-server installation...
npm list -g http-server >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ http-server is already installed globally
) else (
    echo ⚠️  http-server not found globally, trying npx...
    npx http-server --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo Installing http-server globally...
        npm install -g http-server
        if %errorlevel% neq 0 (
            echo ERROR: Failed to install http-server globally
            echo This might be a permissions issue
            echo Trying to use npx instead...
        ) else (
            echo ✅ http-server installed successfully
        )
    ) else (
        echo ✅ http-server available via npx
    )
)

echo [5/6] Listing files in dist folder:
dir dist /b
echo.

echo [6/6] Starting HTTP Server...
echo ========================================
echo Server will start on: http://localhost:8084
echo Serving files from: %CD%\dist
echo.
echo If successful, the browser should open automatically
echo If not, manually open: http://localhost:8084
echo ========================================

REM Start the server with verbose output
echo Starting server now...
npx http-server dist -p 8084 -c-1 --cors -o
set SERVER_EXIT_CODE=%errorlevel%

echo.
echo ========================================
echo Server stopped with exit code: %SERVER_EXIT_CODE%
echo ========================================

if %SERVER_EXIT_CODE% neq 0 (
    echo ❌ There was an error starting the server.
    echo Common issues:
    echo - Port 8084 is already in use
    echo - Permissions issue  
    echo - Missing dist folder
    echo - Network configuration problem
    echo.
    echo Let's check what's on port 8084:
    netstat -ano | findstr :8084
) else (
    echo ✅ Server started successfully
)

echo.
echo Press any key to exit...
pause >nul
