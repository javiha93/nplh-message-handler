@echo off
echo ========================================
echo Starting NPLH Message Handler (New Window)
echo ========================================

cd /d "%~dp0"

echo Starting server in a new window...
echo The server window will open separately.
echo.

REM Start the server in a new command window
start "NPLH Message Handler Server" cmd /c "start-portable.bat"

echo.
echo ✅ Server is starting in a new window
echo.
echo To check if it's running:
echo - Run: check-server.bat
echo - Or open: http://localhost:8084
echo.

pause
