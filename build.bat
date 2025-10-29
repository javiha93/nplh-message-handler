@echo off
echo ========================================
echo Building NPLH Message Handler Project
echo ========================================

echo.
echo [1/3] Building Backend...
echo ========================================
cd /d "%~dp0"
cd npl-message-handler-back
call mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Backend build failed
    pause
    exit /b 1
)
echo Backend build completed successfully!

echo.
echo [2/3] Building Frontend...
echo ========================================
cd ..\nplh-message-handler-ui
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
echo Frontend build completed successfully!

echo.
echo [3/3] Building Client...
echo ========================================
cd ..\nplh-message-handler-client
call mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Client build failed
    pause
    exit /b 1
)
echo Client build completed successfully!

echo.
echo ========================================
echo ✅ ALL BUILDS COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo You can now run the applications:
echo - Backend: run-backend.bat
echo - Frontend: run-frontend.bat
echo - Or both: run-all.bat
echo.
pause
