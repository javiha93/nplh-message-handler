@echo off
echo ========================================
echo  Buscar JARs Generados
echo ========================================
echo.

cd /d "d:\Repos\Tools\nplh-message-handler\npl-message-handler-back"

echo 📂 Buscando en: %CD%\target\
echo.

if exist "target" (
    echo ✅ Directorio target existe
    echo.
    
    echo 📋 Todos los archivos en target\:
    dir target\ /b 2>nul
    echo.
    
    echo 📦 JARs encontrados:
    for %%i in ("target\*.jar") do (
        echo ✅ %%i
        echo    Tamaño: %%~zi bytes
        echo    Fecha: %%~ti
        echo    Ruta completa: %CD%\target\%%~nxi
        echo.
    )
    
    REM Verificar si hay JARs
    dir target\*.jar >nul 2>&1
    if !errorlevel! neq 0 (
        echo ❌ No se encontraron JARs en target\
        echo.
        echo Necesitas ejecutar el build primero:
        echo   .\build-fat-jar.bat
    )
    
) else (
    echo ❌ Directorio target no existe
    echo.
    echo Necesitas ejecutar el build primero:
    echo   .\build-fat-jar.bat
)

echo.
echo ========================================
echo  Ubicaciones de JARs
echo ========================================
echo.
echo 1. JAR Spring Boot (recomendado):
echo    %CD%\target\nplh-message-handler.jar
echo.
echo 2. JAR con Assembly Plugin:
echo    %CD%\target\npl-message-handler-back-1.0-SNAPSHOT-jar-with-dependencies.jar
echo.
echo 3. Copia en D:\Test:
echo    D:\Test\nplh-message-handler.jar
echo.

pause
