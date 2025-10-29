@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  Maven Auto-Installer
echo ========================================
echo.

REM Verificar si Maven ya está instalado
where mvn >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ Maven ya está instalado:
    mvn --version
    pause
    exit /b 0
)

echo Maven no encontrado. Procediendo con la instalación...
echo.

REM Crear directorio para Maven
set "MAVEN_HOME=C:\apache-maven"
set "MAVEN_VERSION=3.9.6"
set "MAVEN_URL=https://dlcdn.apache.org/maven/maven-3/!MAVEN_VERSION!/binaries/apache-maven-!MAVEN_VERSION!-bin.zip"

echo 📂 Creando directorio: !MAVEN_HOME!
if not exist "!MAVEN_HOME!" mkdir "!MAVEN_HOME!"

echo.
echo 📥 Descargando Maven !MAVEN_VERSION!...
echo URL: !MAVEN_URL!

REM Descargar Maven usando PowerShell
powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '!MAVEN_URL!' -OutFile 'C:\maven.zip' }"

if !errorlevel! neq 0 (
    echo ❌ Error descargando Maven
    pause
    exit /b 1
)

echo ✅ Maven descargado exitosamente
echo.

echo 📦 Extrayendo Maven...
powershell -Command "& { Expand-Archive -Path 'C:\maven.zip' -DestinationPath 'C:\' -Force }"

if !errorlevel! neq 0 (
    echo ❌ Error extrayendo Maven
    pause
    exit /b 1
)

REM Mover archivos a la ubicación correcta
move "C:\apache-maven-!MAVEN_VERSION!" "!MAVEN_HOME!" >nul 2>&1

REM Limpiar archivo zip
del "C:\maven.zip" >nul 2>&1

echo ✅ Maven extraído exitosamente
echo.

echo 🔧 Configurando variables de entorno...

REM Agregar Maven al PATH del sistema
setx MAVEN_HOME "!MAVEN_HOME!" /M >nul 2>&1
setx PATH "%PATH%;!MAVEN_HOME!\bin" /M >nul 2>&1

echo ✅ Variables de entorno configuradas
echo.

echo ========================================
echo  Verificación de instalación
echo ========================================
echo.

REM Verificar instalación
set "PATH=%PATH%;!MAVEN_HOME!\bin"

echo Verificando Maven...
"!MAVEN_HOME!\bin\mvn.cmd" --version

if !errorlevel! equ 0 (
    echo.
    echo ✅ Maven instalado exitosamente!
    echo.
    echo Ubicación: !MAVEN_HOME!
    echo PATH actualizado: !MAVEN_HOME!\bin
    echo.
    echo IMPORTANTE: 
    echo - Cierra y reabre la terminal para que los cambios surtan efecto
    echo - O usa la ruta completa: "!MAVEN_HOME!\bin\mvn.cmd"
) else (
    echo ❌ Error en la instalación de Maven
)

echo.
pause
