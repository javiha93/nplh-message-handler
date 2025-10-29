@echo off
echo ========================================
echo Starting NPLH Message Handler (Keep Open)
echo ========================================

cd /d "%~dp0"

REM Start the server and keep window open
call start-portable.bat

echo.
echo ========================================
echo Script finished. Window will remain open.
echo ========================================
echo.
echo If the server is running, you can:
echo - Check status with: check-server.bat
echo - Open browser at: http://localhost:8084
echo - Stop server with Ctrl+C (if still running)
echo.

cmd /k
