@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  NPLH Backend - Build con Maven Local
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
echo Opciones:
echo 1. Ejecutar install-maven.bat para instalar Maven
echo 2. Instalar Maven manualmente desde: https://maven.apache.org/download.cgi
echo.
pause
exit /b 1

:found
echo.
echo 🚀 Construyendo JAR del backend...
echo Usando Maven: !MVN_CMD!
echo.

echo ========================================
echo  Opciones de construcción
echo ========================================
echo.
echo 1. JAR Spring Boot estándar (recomendado)
echo 2. JAR con Assembly Plugin (fat JAR)
echo 3. Ambos JARs
echo.

set /p "jar_choice=Selecciona opción (1-3): "

if "!jar_choice!"=="1" (
    set "BUILD_TYPE=spring-boot"
) else if "!jar_choice!"=="2" (
    set "BUILD_TYPE=assembly"
) else if "!jar_choice!"=="3" (
    set "BUILD_TYPE=both"
) else (
    echo ❌ Opción no válida, usando Spring Boot por defecto
    set "BUILD_TYPE=spring-boot"
)

echo.
echo 🔧 Tipo de construcción: !BUILD_TYPE!
echo.

REM Navegar al directorio del cliente
cd /d "d:\Repos\Tools\nplh-message-handler\nplh-message-handler-client"

echo Paso 1/2: Instalando cliente...
call "!MVN_CMD!" clean install -DskipTests
if !errorlevel! neq 0 (
    echo ❌ Error instalando cliente
    pause
    exit /b 1
)

echo.
echo Paso 2/2: Construyendo backend...
cd ..\npl-message-handler-back

if "!BUILD_TYPE!"=="spring-boot" (
    echo 🚀 Construyendo JAR Spring Boot...
    call "!MVN_CMD!" clean package -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo JAR Spring Boot
        pause
        exit /b 1
    )
) else if "!BUILD_TYPE!"=="assembly" (
    echo 🚀 Construyendo JAR con Assembly Plugin...
    call "!MVN_CMD!" clean compile assembly:single
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo JAR con Assembly
        pause
        exit /b 1
    )
) else if "!BUILD_TYPE!"=="both" (
    echo 🚀 Construyendo JAR Spring Boot...
    call "!MVN_CMD!" clean package -DskipTests
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo JAR Spring Boot
        pause
        exit /b 1
    )
    
    echo.
    echo 🚀 Construyendo JAR con Assembly Plugin...
    call "!MVN_CMD!" compile assembly:single
    if !errorlevel! neq 0 (
        echo ❌ Error construyendo JAR con Assembly
        pause
        exit /b 1
    )
)

echo.
echo ✅ JAR construido exitosamente!
echo.

REM Mostrar información de los JARs generados
echo 📋 JARs generados:
echo.

if exist "target\nplh-message-handler.jar" (
    echo ✅ JAR Spring Boot:
    for %%i in ("target\nplh-message-handler.jar") do (
        echo    Archivo: %%~nxi
        echo    Tamaño: %%~zi bytes
        echo    Ruta: %CD%\target\%%~nxi
    )
    echo.
)

if exist "target\*-jar-with-dependencies.jar" (
    echo ✅ JAR con dependencias (Assembly):
    for %%i in ("target\*-jar-with-dependencies.jar") do (
        echo    Archivo: %%~nxi
        echo    Tamaño: %%~zi bytes
        echo    Ruta: %CD%\target\%%~nxi
    )
    echo.
)

REM Mostrar todos los JARs
echo 📂 Todos los JARs en target\:
dir target\*.jar /b 2>nul
echo.

REM Copiar JARs a D:\Test
echo 📂 Copiando JARs a D:\Test...
if not exist "D:\Test" mkdir "D:\Test"

if exist "target\nplh-message-handler.jar" (
    copy "target\nplh-message-handler.jar" "D:\Test\" >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ JAR Spring Boot copiado a D:\Test\nplh-message-handler.jar
    )
)

if exist "target\*-jar-with-dependencies.jar" (
    for %%i in ("target\*-jar-with-dependencies.jar") do (
        copy "%%i" "D:\Test\nplh-message-handler-with-dependencies.jar" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ JAR con dependencias copiado a D:\Test\nplh-message-handler-with-dependencies.jar
        )
    )
)

echo.
echo ========================================
echo  Instrucciones de uso
echo ========================================
echo.
echo Para ejecutar JAR Spring Boot:
echo   cd D:\Test
echo   java -jar nplh-message-handler.jar
echo.
echo Para ejecutar JAR con dependencias:
echo   cd D:\Test
echo   java -jar nplh-message-handler-with-dependencies.jar
echo.
echo Para ejecutar con configuración específica:
echo   java -jar nplh-message-handler.jar --spring.config.location=application.properties
echo.
echo ========================================
echo  Diferencias entre JARs
echo ========================================
echo.
echo 🔹 JAR Spring Boot:
echo   - Método estándar de Spring Boot
echo   - Incluye todas las dependencias
echo   - Tamaño: ~50-100 MB
echo   - Recomendado para producción
echo.
echo 🔹 JAR con Assembly Plugin:
echo   - Método tradicional de Maven
echo   - Todas las dependencias desempaquetadas
echo   - Tamaño: puede ser mayor
echo   - Útil para debugging
echo.

pause
