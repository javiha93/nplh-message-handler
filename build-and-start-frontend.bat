@echo off
echo ========================================
echo Build and Start Frontend
echo ========================================

echo.
echo [1/2] Building Frontend...
echo ========================================
cd /d "%~dp0"
cd nplh-message-handler-ui
call npm install
if %errorlevel% neq 0 (
    echo ERROR: Frontend dependencies installation failed
    pause
    exit /b 1
)

call npm run build
if %errorlevel% neq 0 (
    echo ERROR: Frontend build failed
    pause
    exit /b 1
)

echo.
echo [2/2] Starting Frontend...
echo ========================================
call npm run preview

echo.
echo Frontend is running on: http://localhost:8084/
echo.
pause
