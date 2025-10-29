@echo off
echo ========================================
echo Starting Frontend on Port 8084
echo ========================================

echo.
echo Starting Frontend...
echo ========================================
cd /d "%~dp0"
cd nplh-message-handler-ui
call npm run preview

echo.
echo Frontend is running on: http://localhost:8084/
echo.
pause
