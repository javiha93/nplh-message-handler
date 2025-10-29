@echo off
setlocal enabledelayedexpansion

echo ========================================
echo NPLH Message Handler - Diagnostic Tool
echo ========================================
echo.

REM Force the script to stay in its own directory
cd /d "%~dp0"

echo Current directory: %CD%
echo Current time: %date% %time%
echo.

echo ========================================
echo STEP 1: Checking Environment
echo ========================================

echo [1.1] Testing basic commands...
echo Computing current directory...
set "CURRENT_DIR=%CD%"
echo Current directory variable: !CURRENT_DIR!

echo.
echo [1.2] Node.js check:
where node >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Node.js found in PATH
    echo Node.js version:
    node --version 2>nul
    if !errorlevel! neq 0 (
        echo ❌ Node.js found but cannot execute
    )
) else (
    echo ❌ Node.js not found in PATH
    echo Current PATH:
    echo %PATH%
)

echo.
echo [1.3] NPM check:
where npm >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ NPM found in PATH
    echo NPM version:
    npm --version 2>nul
    if !errorlevel! neq 0 (
        echo ❌ NPM found but cannot execute
    )
) else (
    echo ❌ NPM not found in PATH
)

echo.
echo [1.4] Current directory contents:
if exist "*" (
    echo Files and folders in current directory:
    dir /b 2>nul
) else (
    echo No files found or directory access error
)

echo.
echo ========================================
echo STEP 2: Checking dist folder
echo ========================================

echo [2.1] Checking if dist folder exists...
if exist "dist" (
    echo ✅ dist folder exists
    echo.
    echo [2.2] Contents of dist folder:
    dir "dist" /b 2>nul
    if !errorlevel! neq 0 (
        echo ❌ Cannot list dist folder contents
    )
    
    echo.
    echo [2.3] Checking for index.html:
    if exist "dist\index.html" (
        echo ✅ index.html found
        echo File details:
        dir "dist\index.html" 2>nul | findstr index.html
    ) else (
        echo ❌ index.html NOT found in dist folder
    )
    
    echo.
    echo [2.4] Checking for assets folder:
    if exist "dist\assets" (
        echo ✅ assets folder found
        echo Assets contents:
        dir "dist\assets" /b 2>nul
    ) else (
        echo ❌ assets folder NOT found
    )
) else (
    echo ❌ dist folder NOT found
    echo.
    echo You need to copy the dist folder to this directory.
    echo This script is looking in: !CURRENT_DIR!
)

echo.
echo ========================================
echo STEP 3: Network and Port Testing
echo ========================================

echo [3.1] Checking netstat availability:
netstat -h >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ netstat command available
    echo.
    echo [3.2] Current connections on port 8084:
    netstat -ano 2>nul | findstr :8084
    if !errorlevel! neq 0 (
        echo ✅ Port 8084 appears to be available
    ) else (
        echo ⚠️  Port 8084 is in use
    )
) else (
    echo ❌ netstat command not available
)

echo.
echo ========================================
echo STEP 4: Testing HTTP Server Options
echo ========================================

echo [4.1] Testing npx availability:
where npx >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ npx command found
    echo.
    echo [4.2] Testing npx http-server:
    timeout 5 >nul
    npx http-server --version >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ npx http-server works
    ) else (
        echo ❌ npx http-server failed
    )
) else (
    echo ❌ npx command not found
)

echo.
echo [4.3] Testing Python as alternative:
where python >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Python found
    python --version 2>nul
) else (
    echo ❌ Python not found
    where python3 >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ Python3 found
        python3 --version 2>nul
    ) else (
        echo ❌ Python3 not found either
    )
)

echo.
echo ========================================
echo STEP 5: Quick Server Test
echo ========================================

set /p "test_choice=Do you want to test starting a server? (Y/N): "
if /i "!test_choice!"=="Y" (
    echo.
    echo Testing server startup...
    echo This will try to start for 10 seconds then stop
    echo.
    
    if exist "dist" (
        echo Starting Python server test...
        cd dist
        timeout 10 python -m http.server 8084 >nul 2>&1
        cd ..
        echo Test completed.
    ) else (
        echo Cannot test - dist folder missing
    )
)

echo.
echo ========================================
echo DIAGNOSIS SUMMARY
echo ========================================

echo Current directory: !CURRENT_DIR!
if exist "dist" (
    echo ✅ dist folder: Present
) else (
    echo ❌ dist folder: Missing
)

where node >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Node.js: Available
) else (
    echo ❌ Node.js: Missing
)

where python >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Python: Available
) else (
    where python3 >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ Python3: Available
    ) else (
        echo ❌ Python: Missing
    )
)

echo.
echo ========================================
echo Diagnostic completed - Press any key to exit
echo ========================================
pause >nul
exit /b 0
