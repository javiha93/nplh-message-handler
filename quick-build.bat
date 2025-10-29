@echo off
echo ========================================
echo  Quick JAR Build - Backend Only
echo ========================================
echo.

cd /d "d:\Repos\Tools\nplh-message-handler"

echo 🔧 Construyendo JAR del backend...
echo.

REM Instalar cliente
echo Paso 1/2: Instalando cliente...
cd nplh-message-handler-client
call mvn clean install -DskipTests -q
if !errorlevel! neq 0 (
    echo ❌ Error instalando cliente
    pause
    exit /b 1
)

REM Construir backend
echo Paso 2/2: Construyendo backend...
cd ..\npl-message-handler-back
call mvn clean package -DskipTests -q
if !errorlevel! neq 0 (
    echo ❌ Error construyendo backend
    pause
    exit /b 1
)

echo.
echo ✅ JAR construido exitosamente!

REM Copiar a D:\Test
if exist "target\nplh-message-handler.jar" (
    echo 📂 Copiando JAR a D:\Test...
    copy "target\nplh-message-handler.jar" "D:\Test\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ JAR copiado a D:\Test\nplh-message-handler.jar
    )
)

echo.
echo 🚀 Listo para ejecutar:
echo   cd D:\Test
echo   java -jar nplh-message-handler.jar
echo.

pause
