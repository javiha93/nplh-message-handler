# Script PowerShell para gestionar IPs Alias
# Ejecutar como Administrador

param(
    [Parameter(Mandatory=$false)]
    [string]$Action = "list",
    
    [Parameter(Mandatory=$false)]
    [string]$IPAddress = "",
    
    [Parameter(Mandatory=$false)]
    [string]$InterfaceName = "Ethernet"
)

function Show-Menu {
    Write-Host ""
    Write-Host "=================================" -ForegroundColor Cyan
    Write-Host "  Gestor de IPs Alias para WSClient" -ForegroundColor Cyan
    Write-Host "=================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Listar IPs actuales" -ForegroundColor Green
    Write-Host "2. Añadir IP alias" -ForegroundColor Green
    Write-Host "3. Eliminar IP alias" -ForegroundColor Green
    Write-Host "4. Listar interfaces de red" -ForegroundColor Green
    Write-Host "5. Verificar IP específica" -ForegroundColor Green
    Write-Host "6. Salir" -ForegroundColor Yellow
    Write-Host ""
}

function List-CurrentIPs {
    Write-Host ""
    Write-Host "📡 IPs configuradas en el sistema:" -ForegroundColor Cyan
    Write-Host ""
    
    Get-NetIPAddress -AddressFamily IPv4 | 
        Where-Object {$_.IPAddress -ne "127.0.0.1"} |
        Format-Table -Property InterfaceAlias, IPAddress, PrefixLength -AutoSize
}

function List-Interfaces {
    Write-Host ""
    Write-Host "🔌 Interfaces de red disponibles:" -ForegroundColor Cyan
    Write-Host ""
    
    Get-NetAdapter | 
        Where-Object {$_.Status -eq "Up"} |
        Format-Table -Property Name, InterfaceDescription, Status, LinkSpeed -AutoSize
}

function Add-IPAlias {
    param(
        [string]$IP,
        [string]$Interface
    )
    
    if ([string]::IsNullOrEmpty($IP)) {
        $IP = Read-Host "Introduce la IP a añadir (ej: 192.168.1.100)"
    }
    
    if ([string]::IsNullOrEmpty($Interface)) {
        Write-Host ""
        Write-Host "Interfaces disponibles:"
        Get-NetAdapter | Where-Object {$_.Status -eq "Up"} | Select-Object -Property Name
        Write-Host ""
        $Interface = Read-Host "Nombre de la interfaz (ej: Ethernet)"
    }
    
    try {
        # Verificar si la IP ya existe
        $existing = Get-NetIPAddress -IPAddress $IP -ErrorAction SilentlyContinue
        if ($existing) {
            Write-Host "⚠️  La IP $IP ya está configurada" -ForegroundColor Yellow
            return
        }
        
        Write-Host ""
        Write-Host "➕ Añadiendo IP $IP a la interfaz $Interface..." -ForegroundColor Green
        
        # Añadir la IP (PrefixLength 24 = 255.255.255.0)
        New-NetIPAddress -InterfaceAlias $Interface -IPAddress $IP -PrefixLength 24 -ErrorAction Stop
        
        Write-Host "✅ IP añadida correctamente!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Ahora puedes usar en tu código:" -ForegroundColor Cyan
        Write-Host "  connection.setRequestorAddress(`"$IP`");" -ForegroundColor White
        Write-Host ""
        
    } catch {
        Write-Host "❌ Error al añadir IP: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
        Write-Host "Asegúrate de:" -ForegroundColor Yellow
        Write-Host "  1. Ejecutar PowerShell como Administrador" -ForegroundColor Yellow
        Write-Host "  2. Usar una IP válida para tu red" -ForegroundColor Yellow
        Write-Host "  3. Que la interfaz existe y está activa" -ForegroundColor Yellow
    }
}

function Remove-IPAlias {
    param(
        [string]$IP,
        [string]$Interface
    )
    
    if ([string]::IsNullOrEmpty($IP)) {
        $IP = Read-Host "Introduce la IP a eliminar"
    }
    
    try {
        Write-Host ""
        Write-Host "➖ Eliminando IP $IP..." -ForegroundColor Yellow
        
        Remove-NetIPAddress -IPAddress $IP -Confirm:$false -ErrorAction Stop
        
        Write-Host "✅ IP eliminada correctamente!" -ForegroundColor Green
        Write-Host ""
        
    } catch {
        Write-Host "❌ Error al eliminar IP: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Verify-IP {
    param([string]$IP)
    
    if ([string]::IsNullOrEmpty($IP)) {
        $IP = Read-Host "Introduce la IP a verificar"
    }
    
    Write-Host ""
    Write-Host "🔍 Verificando IP: $IP" -ForegroundColor Cyan
    
    $result = Get-NetIPAddress -IPAddress $IP -ErrorAction SilentlyContinue
    
    if ($result) {
        Write-Host "✅ La IP está configurada en:" -ForegroundColor Green
        $result | Format-Table -Property InterfaceAlias, IPAddress, AddressState -AutoSize
        
        Write-Host ""
        Write-Host "Puedes usar en WSClient:" -ForegroundColor Cyan
        Write-Host "  connection.setRequestorAddress(`"$IP`");" -ForegroundColor White
        Write-Host "  WSClient client = new WSClient(hostName, hostType, connection, irisService);" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host "❌ La IP NO está configurada en este sistema" -ForegroundColor Red
        Write-Host ""
        Write-Host "Opciones:" -ForegroundColor Yellow
        Write-Host "  1. Añadir esta IP como alias (opción 2 del menú)" -ForegroundColor Yellow
        Write-Host "  2. Usar headers HTTP (la IP puede ser cualquiera)" -ForegroundColor Yellow
        Write-Host ""
    }
}

# Verificar si se ejecuta como administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")

if (-not $isAdmin) {
    Write-Host ""
    Write-Host "⚠️  ADVERTENCIA: No estás ejecutando como Administrador" -ForegroundColor Yellow
    Write-Host "   Algunas operaciones pueden fallar" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   Para ejecutar como Administrador:" -ForegroundColor Cyan
    Write-Host "   1. Clic derecho en PowerShell" -ForegroundColor White
    Write-Host "   2. Seleccionar 'Ejecutar como administrador'" -ForegroundColor White
    Write-Host ""
}

# Menú principal
if ($Action -eq "list") {
    while ($true) {
        Show-Menu
        $choice = Read-Host "Selecciona una opción"
        
        switch ($choice) {
            "1" { List-CurrentIPs }
            "2" { Add-IPAlias -IP $IPAddress -Interface $InterfaceName }
            "3" { Remove-IPAlias -IP $IPAddress -Interface $InterfaceName }
            "4" { List-Interfaces }
            "5" { Verify-IP -IP $IPAddress }
            "6" { 
                Write-Host ""
                Write-Host "👋 Hasta luego!" -ForegroundColor Green
                Write-Host ""
                exit 
            }
            default { 
                Write-Host ""
                Write-Host "❌ Opción inválida" -ForegroundColor Red 
            }
        }
        
        Write-Host ""
        Read-Host "Presiona Enter para continuar"
        Clear-Host
    }
} 
elseif ($Action -eq "add") {
    Add-IPAlias -IP $IPAddress -Interface $InterfaceName
}
elseif ($Action -eq "remove") {
    Remove-IPAlias -IP $IPAddress -Interface $InterfaceName
}
elseif ($Action -eq "verify") {
    Verify-IP -IP $IPAddress
}
else {
    Write-Host "Uso:" -ForegroundColor Cyan
    Write-Host "  .\manage-ip-alias.ps1                              # Menú interactivo" -ForegroundColor White
    Write-Host "  .\manage-ip-alias.ps1 -Action add -IPAddress 192.168.1.100" -ForegroundColor White
    Write-Host "  .\manage-ip-alias.ps1 -Action remove -IPAddress 192.168.1.100" -ForegroundColor White
    Write-Host "  .\manage-ip-alias.ps1 -Action verify -IPAddress 192.168.1.100" -ForegroundColor White
    Write-Host ""
}
