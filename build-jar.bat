@echo off
echo ========================================
echo  NPLH Backend - Build JAR with Dependencies
echo ========================================
echo.

REM Cambiar al directorio del backend
cd /d "d:\Repos\Tools\nplh-message-handler\npl-message-handler-back"

echo Directorio actual: %CD%
echo.

REM Verificar que Maven está disponible
mvn --version >nul 2>&1
if !errorlevel! neq 0 (
    echo ❌ Error: Maven no encontrado
    echo Asegurate de que Maven este instalado y en el PATH
    echo.
    pause
    exit /b 1
)

echo ✅ Maven disponible:
mvn --version | findstr "Apache Maven"
echo.

REM Limpiar builds anteriores
echo 🧹 Limpiando builds anteriores...
mvn clean
if !errorlevel! neq 0 (
    echo ❌ Error durante mvn clean
    pause
    exit /b 1
)

echo.
echo 🔧 Instalando dependencias del cliente...
cd ..\nplh-message-handler-client
mvn clean install -DskipTests
if !errorlevel! neq 0 (
    echo ❌ Error instalando cliente
    pause
    exit /b 1
)

echo.
echo 📦 Construyendo JAR del backend con todas las dependencias...
cd ..\npl-message-handler-back
mvn clean package -DskipTests
if !errorlevel! neq 0 (
    echo ❌ Error durante la construcción
    pause
    exit /b 1
)

echo.
echo ✅ JAR construido exitosamente!
echo.

REM Mostrar información del JAR generado
if exist "target\nplh-message-handler.jar" (
    echo 📋 Información del JAR:
    dir "target\nplh-message-handler.jar" | findstr nplh-message-handler.jar
    echo.
    
    REM Copiar el JAR a D:\Test
    echo 📂 Copiando JAR a D:\Test...
    copy "target\nplh-message-handler.jar" "D:\Test\"
    if !errorlevel! equ 0 (
        echo ✅ JAR copiado a D:\Test\nplh-message-handler.jar
    ) else (
        echo ❌ Error copiando JAR
    )
) else (
    echo ❌ JAR no encontrado en target\
)

echo.
echo ========================================
echo  Resumen
echo ========================================
echo.
echo JAR generado: target\nplh-message-handler.jar
echo JAR copiado a: D:\Test\nplh-message-handler.jar
echo.
echo Para ejecutar:
echo   cd D:\Test
echo   java -jar nplh-message-handler.jar
echo.

pause
