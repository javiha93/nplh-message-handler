@echo off
echo ========================================
echo Opening Diagnostic in New Window
echo ========================================

cd /d "%~dp0"

echo Opening diagnostic tool in a new window...
echo The diagnostic window will stay open for you to read.

start "NPLH Diagnostic" cmd /k "simple-test.bat"

echo.
echo Diagnostic window opened.
echo Check the new window for results.
pause
