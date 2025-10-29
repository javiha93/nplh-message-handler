@echo off
echo ========================================
echo Building Frontend Only
echo ========================================

echo.
echo Building Frontend...
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
echo ========================================
echo ✅ FRONTEND BUILD COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo Build output is in: nplh-message-handler-ui/dist/
echo.
pause
