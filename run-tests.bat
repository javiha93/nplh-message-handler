@echo off
REM Script para ejecutar los tests del proyecto

cd /d "%~dp0nplh-message-handler-tests"

echo ====================================
echo Ejecutando tests con JUnit 5...
echo ====================================
echo.

mvn clean test

echo.
echo ====================================
echo Generando reporte de coverage...
echo ====================================
echo.

mvn jacoco:report

if exist "target\site\jacoco\index.html" (
    echo.
    echo ====================================
    echo Reporte de coverage generado:
    echo %CD%\target\site\jacoco\index.html
    echo ====================================
    echo.
    echo ¿Desea abrir el reporte? (S/N)
    set /p OPEN_REPORT=
    if /i "%OPEN_REPORT%"=="S" (
        start "" "target\site\jacoco\index.html"
    )
)

pause
