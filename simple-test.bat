@echo off
echo ========================================
echo Simple Diagnostic Tool
echo ========================================
echo.

REM Stay in the script directory
cd /d "%~dp0"

echo 1. Current directory:
echo %CD%
echo.

echo 2. Files in current directory:
dir /b
echo.

echo 3. Checking dist folder:
if exist "dist" (
    echo ✅ dist folder found
    echo Contents:
    dir dist /b
) else (
    echo ❌ dist folder NOT found
)
echo.

echo 4. Checking Node.js:
node --version 2>nul
if %errorlevel% equ 0 (
    echo ✅ Node.js works
) else (
    echo ❌ Node.js not working
)
echo.

echo 5. Checking Python:
python --version 2>nul
if %errorlevel% equ 0 (
    echo ✅ Python works
) else (
    echo ❌ Python not working
)
echo.

echo 6. Checking port 8084:
netstat -an 2>nul | findstr :8084
if %errorlevel% equ 0 (
    echo ⚠️ Port 8084 in use
) else (
    echo ✅ Port 8084 available
)
echo.

echo ========================================
echo Diagnosis complete
echo ========================================
echo.
echo Press any key to continue...
pause >nul

echo.
echo Want to try Python server? (Y/N)
set /p choice=

if /i "%choice%"=="Y" (
    if exist "dist" (
        echo Starting Python server on port 8084...
        echo Open http://localhost:8084 in your browser
        echo Press Ctrl+C to stop
        echo.
        cd dist
        python -m http.server 8084
    ) else (
        echo Cannot start - dist folder missing
        pause
    )
)
