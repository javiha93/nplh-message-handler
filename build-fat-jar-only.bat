@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  NPLH Backend - JAR con Dependencias
echo ========================================
echo.

REM Definir posibles ubicaciones de Maven
set "MAVEN_LOCATIONS=C:\apache-maven\bin\mvn.cmd;C:\Program Files\Apache\maven\bin\mvn.cmd;C:\maven\bin\mvn.cmd"

echo 🔍 Buscando Maven...

REM Buscar Maven en ubicaciones comunes
for %%M in (%MAVEN_LOCATIONS%) do (
    if exist "%%M" (
        echo ✅ Maven encontrado en: %%M
        set "MVN_CMD=%%M"
        goto :found
    )
)

REM Si no se encuentra, verificar PATH
where mvn >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Maven encontrado en PATH
    set "MVN_CMD=mvn"
    goto :found
)

echo ❌ Maven no encontrado
echo.
echo Instala Maven primero ejecutando: install-maven.bat
echo.
pause
exit /b 1

:found
echo.
echo 🚀 Construyendo JAR con TODAS las dependencias...
echo Usando Maven: !MVN_CMD!
echo.

REM Navegar al directorio del cliente
cd /d "d:\Repos\Tools\nplh-message-handler\nplh-message-handler-client"

echo Paso 1/2: Instalando cliente...
call "!MVN_CMD!" clean install -DskipTests -q
if !errorlevel! neq 0 (
    echo ❌ Error instalando cliente
    pause
    exit /b 1
)

echo.
echo Paso 2/2: Construyendo JAR con Assembly Plugin...
cd ..\npl-message-handler-back
call "!MVN_CMD!" clean compile assembly:single -q
if !errorlevel! neq 0 (
    echo ❌ Error construyendo JAR con dependencias
    pause
    exit /b 1
)

echo.
echo ✅ JAR con dependencias construido exitosamente!
echo.

REM Buscar el JAR con dependencias
for %%i in ("target\*-jar-with-dependencies.jar") do (
    echo 📋 JAR con dependencias:
    echo    Archivo: %%~nxi
    echo    Tamaño: %%~zi bytes
    echo    Ruta: %CD%\target\%%~nxi
    echo.
    
    REM Copiar a D:\Test
    echo 📂 Copiando JAR a D:\Test...
    if not exist "D:\Test" mkdir "D:\Test"
    copy "%%i" "D:\Test\nplh-message-handler-fat.jar" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ JAR copiado a D:\Test\nplh-message-handler-fat.jar
    ) else (
        echo ⚠️  Error copiando JAR
    )
)

echo.
echo ========================================
echo  JAR con Dependencias Listo
echo ========================================
echo.
echo Para ejecutar:
echo   cd D:\Test
echo   java -jar nplh-message-handler-fat.jar
echo.
echo Este JAR incluye:
echo ✅ Todas las dependencias de Spring Boot
echo ✅ Driver JDBC de InterSystems
echo ✅ Cliente NPLH
echo ✅ Controladores REST
echo ✅ Todas las librerías externas
echo.
echo Tamaño aproximado: 80-150 MB
echo.

pause
