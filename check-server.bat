@echo off
echo ========================================
echo Testing NPLH Message Handler Server
echo ========================================

echo Checking if server is running on port 8084...
echo.

REM Check if port 8084 is in use
netstat -an | findstr :8084
if %errorlevel% equ 0 (
    echo ✅ Port 8084 is in use - Server is likely running
    echo.
    echo Trying to access the server...
    curl -s -o nul -w "HTTP Status: %%{http_code}" http://localhost:8084/ 2>nul
    if %errorlevel% equ 0 (
        echo.
        echo ✅ Server is responding!
        echo 🌐 Open: http://localhost:8084
    ) else (
        echo.
        echo ⚠️ Port is in use but server is not responding
        echo This might be another application using port 8084
    )
) else (
    echo ❌ Port 8084 is not in use - Server is not running
    echo.
    echo To start the server, run: start-portable.bat
)

echo.
echo ========================================
pause
