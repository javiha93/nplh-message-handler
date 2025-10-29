@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  NPLH Backend - Build Fat JAR
echo ========================================
echo.

REM Cambiar al directorio del backend
cd /d "d:\Repos\Tools\nplh-message-handler\npl-message-handler-back"

echo Directorio actual: %CD%
echo.

REM Verificar Maven
where mvn >nul 2>&1
if !errorlevel! neq 0 (
    echo ❌ Maven no encontrado en PATH
    pause
    exit /b 1
)

echo ✅ Maven encontrado
echo.

REM Mostrar opciones
echo ========================================
echo  Opciones de construcción
echo ========================================
echo.
echo 1. JAR estándar de Spring Boot (recomendado)
echo 2. JAR con assembly plugin (todas las dependencias)
echo 3. JAR sin frontend (solo backend)
echo 4. Limpiar y reconstruir todo
echo 5. Salir
echo.

set /p "choice=Selecciona una opción (1-5): "

if "!choice!"=="1" (
    echo.
    echo 🚀 Construyendo JAR estándar de Spring Boot...
    echo.
    
    REM Instalar cliente primero
    echo Paso 1: Instalando cliente...
    cd ..\nplh-message-handler-client
    mvn clean install -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error instalando cliente
        pause
        exit /b 1
    )
    
    echo Paso 2: Construyendo backend...
    cd ..\npl-message-handler-back
    mvn clean package -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo backend
        pause
        exit /b 1
    )
    
) else if "!choice!"=="2" (
    echo.
    echo 🚀 Construyendo JAR con Assembly Plugin...
    echo.
    
    REM Instalar cliente
    cd ..\nplh-message-handler-client
    mvn clean install -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error instalando cliente
        pause
        exit /b 1
    )
    
    REM Construir con assembly
    cd ..\npl-message-handler-back
    mvn clean compile assembly:single
    if !errorlevel! neq 0 (
        echo ❌ Error con assembly
        pause
        exit /b 1
    )
    
) else if "!choice!"=="3" (
    echo.
    echo 🚀 Construyendo JAR sin frontend...
    echo.
    
    cd ..\nplh-message-handler-client
    mvn clean install -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error instalando cliente
        pause
        exit /b 1
    )
    
    cd ..\npl-message-handler-back
    mvn clean package -DskipTests -Dspring-boot.repackage.skip=false
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo backend
        pause
        exit /b 1
    )
    
) else if "!choice!"=="4" (
    echo.
    echo 🧹 Limpiando y reconstruyendo todo...
    echo.
    
    REM Limpiar todo
    cd ..\nplh-message-handler-client
    mvn clean
    cd ..\npl-message-handler-back
    mvn clean
    
    REM Reconstruir
    cd ..\nplh-message-handler-client
    mvn clean install -DskipTests
    cd ..\npl-message-handler-back
    mvn clean package -DskipTests
    
) else if "!choice!"=="5" (
    echo.
    echo 👋 Saliendo...
    exit /b 0
    
) else (
    echo.
    echo ❌ Opción no válida
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Verificando JAR generado
echo ========================================
echo.

REM Buscar JARs generados
echo 📂 Buscando JARs en: %CD%\target\
echo.

if exist "target\nplh-message-handler.jar" (
    echo ✅ JAR Spring Boot encontrado: target\nplh-message-handler.jar
    for %%i in ("target\nplh-message-handler.jar") do (
        echo    Tamaño: %%~zi bytes
        echo    Fecha: %%~ti
        echo    Ruta completa: %CD%\target\nplh-message-handler.jar
    )
)

REM Buscar JAR con dependencias del Assembly Plugin
if exist "target\*-jar-with-dependencies.jar" (
    echo ✅ JAR con dependencias encontrado:
    for %%i in ("target\*-jar-with-dependencies.jar") do (
        echo    Archivo: %%i
        echo    Tamaño: %%~zi bytes
        echo    Ruta completa: %CD%\target\%%i
    )
)

REM Mostrar todos los JARs en target
echo.
echo 📋 Todos los JARs en target\:
dir target\*.jar /b 2>nul
if !errorlevel! neq 0 (
    echo    (No se encontraron JARs)
)
    
    REM Copiar a D:\Test
    echo.
    echo 📂 Copiando JAR a D:\Test...
    
    REM Copiar el JAR principal
    if exist "target\nplh-message-handler.jar" (
        copy "target\nplh-message-handler.jar" "D:\Test\" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ JAR copiado a D:\Test\nplh-message-handler.jar
        ) else (
            echo ❌ Error copiando JAR principal
        )
    )
    
    REM Copiar JAR con dependencias si existe
    for %%i in ("target\*-jar-with-dependencies.jar") do (
        copy "%%i" "D:\Test\nplh-message-handler-with-dependencies.jar" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ JAR con dependencias copiado a D:\Test\nplh-message-handler-with-dependencies.jar
        )
    )
    
) else (
    echo ❌ JAR no encontrado en target\
    echo.
    echo Archivos en target\:
    dir target\ /b 2>nul
)

echo.
echo ========================================
echo  Instrucciones de uso
echo ========================================
echo.
echo Para ejecutar el JAR:
echo   java -jar nplh-message-handler.jar
echo.
echo Para ejecutar con configuración específica:
echo   java -jar nplh-message-handler.jar --spring.config.location=application.properties
echo.
echo Para ejecutar en puerto específico:
echo   java -jar nplh-message-handler.jar --server.port=8080
echo.

pause
