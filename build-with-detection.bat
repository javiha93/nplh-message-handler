@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  Maven JAR Builder with Detection
echo ========================================
echo.

cd /d "d:\Repos\Tools\nplh-message-handler"

echo 🔍 Detectando Maven...
echo.

REM Verificar Maven en PATH
where mvn >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Maven encontrado en PATH
    set "MVN_CMD=mvn"
    goto :build
)

REM Verificar wrapper de Maven
if exist "mvnw.cmd" (
    echo ✅ Maven wrapper encontrado
    set "MVN_CMD=mvnw.cmd"
    goto :build
)

REM Buscar Maven en ubicaciones comunes
set "MAVEN_LOCATIONS=C:\Program Files\Apache\maven C:\apache-maven C:\maven C:\Program Files\Maven"

for %%L in (%MAVEN_LOCATIONS%) do (
    if exist "%%L\bin\mvn.cmd" (
        echo ✅ Maven encontrado en: %%L
        set "MVN_CMD=%%L\bin\mvn.cmd"
        goto :build
    )
)

echo ❌ Maven no encontrado
echo.
echo Opciones:
echo 1. Instalar Maven desde: https://maven.apache.org/download.cgi
echo 2. Agregar Maven al PATH
echo 3. Usar Maven wrapper (mvnw.cmd)
echo.
echo ¿Quieres que intente buscar Maven manualmente?
set /p "search=Buscar Maven manualmente? (Y/N): "

if /i "!search!"=="Y" (
    echo.
    echo 🔍 Buscando Maven en el sistema...
    for /f "tokens=*" %%i in ('dir /s /b "mvn.cmd" 2^>nul') do (
        echo Encontrado: %%i
        set "MVN_CMD=%%i"
        goto :build
    )
    echo Maven no encontrado automáticamente
)

echo.
echo ❌ No se puede continuar sin Maven
pause
exit /b 1

:build
echo.
echo 🚀 Usando Maven: !MVN_CMD!
echo.

REM Construir cliente
echo Paso 1/2: Construyendo cliente...
cd nplh-message-handler-client
call "!MVN_CMD!" clean install -DskipTests
if !errorlevel! neq 0 (
    echo ❌ Error construyendo cliente
    pause
    exit /b 1
)

echo.
echo Paso 2/2: Construyendo backend...
cd ..\npl-message-handler-back
call "!MVN_CMD!" clean package -DskipTests
if !errorlevel! neq 0 (
    echo ❌ Error construyendo backend
    pause
    exit /b 1
)

echo.
echo ✅ JAR construido exitosamente!
echo.

REM Mostrar información del JAR
if exist "target\nplh-message-handler.jar" (
    echo 📋 Información del JAR:
    for %%i in ("target\nplh-message-handler.jar") do (
        echo    Archivo: %%i
        echo    Tamaño: %%~zi bytes
        echo    Fecha: %%~ti
    )
    
    REM Copiar a D:\Test
    echo.
    echo 📂 Copiando JAR a D:\Test...
    if not exist "D:\Test" mkdir "D:\Test"
    copy "target\nplh-message-handler.jar" "D:\Test\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ JAR copiado a D:\Test\nplh-message-handler.jar
    ) else (
        echo ⚠️  Error copiando JAR
    )
    
) else (
    echo ❌ JAR no encontrado en target\
    echo.
    echo Archivos en target\:
    dir target\ /b 2>nul
)

echo.
echo ========================================
echo  Instrucciones
echo ========================================
echo.
echo El JAR generado es un "fat JAR" que incluye:
echo - Spring Boot framework
echo - Todas las dependencias necesarias
echo - El cliente NPLH
echo - Controladores REST
echo.
echo Para ejecutar:
echo   cd D:\Test
echo   java -jar nplh-message-handler.jar
echo.
echo Para configurar conexión a IRIS:
echo   java -jar nplh-message-handler.jar --iris.host=tu-servidor --iris.port=1972
echo.

pause
