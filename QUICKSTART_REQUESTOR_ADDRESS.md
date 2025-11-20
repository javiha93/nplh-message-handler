# 🚀 Quick Start - Usar requestorAddress

## ⚠️ IMPORTANTE: El servidor NO lee headers HTTP

Tu servidor **NO** puede leer headers HTTP como `X-Forwarded-For`, por lo tanto la **ÚNICA** forma de que detecte una IP diferente es hacer **bind real** a esa IP.

**Esto significa que la IP DEBE existir en tu sistema.**

---

## Configuración Requerida (Una sola vez) 🔧

### Paso 1: Añadir IP a tu sistema

**Windows - PowerShell como Administrador:**
```powershell
# Opción A: Usar el script incluido (RECOMENDADO)
.\manage-ip-alias.ps1

# Opción B: Comando manual
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24
```

**Linux:**
```bash
sudo ip addr add 192.168.1.100/24 dev eth0
```

### Paso 2: Verificar que la IP existe

```powershell
# Windows
Get-NetIPAddress -IPAddress 192.168.1.100

# Linux
ip addr show | grep 192.168.1.100
```

Deberías ver la IP listada. Si no, el bind fallará.

---

## Uso en tu Código ✨

```java
Connection connection = new Connection();
connection.setRequestorAddress("192.168.1.100"); // IP que añadiste al sistema

WSClient client = new WSClient("VTG", HostType.VTG, connection, irisService);
client.send("ProcessVTGEvent", messageBody);
```

**Resultado**:
- ✅ Socket hace bind a IP 192.168.1.100
- ✅ Conexión TCP sale desde esa IP
- ✅ El servidor ve `REMOTE_ADDR = 192.168.1.100`
- ✅ **FUNCIONA SIN CAMBIOS EN EL SERVIDOR**

---

## Cómo Funciona Técnicamente 🔧

El código ahora usa **sockets manuales** con bind:

```java
// Internamente, el código hace:
Socket socket = new Socket();
socket.bind(new InetSocketAddress("192.168.1.100", 0));
socket.connect(new InetSocketAddress("servidor", 80));

// Luego implementa HTTP sobre el socket
// POST /path HTTP/1.1
// Host: servidor
// Content-Type: text/xml
// ...
```

Esto garantiza que la IP TCP de origen sea la configurada.

---

## Verificar que Funciona 🔍

### En los Logs del Cliente

Busca estos mensajes al iniciar el cliente:

✅ **Si la IP existe en el sistema:**
```
✅ IP 192.168.1.100 encontrada en interfaz: Ethernet
🔧 Enviando mensaje con IP de origen: 192.168.1.100
🔧 Creando socket manual con bind a: 192.168.1.100
✅ Socket bindeado a IP: 192.168.1.100
✅ Mensaje enviado desde IP: 192.168.1.100
```

❌ **Si la IP NO existe:**
```
⚠️ IP 192.168.1.100 no encontrada en interfaces locales
❌ La IP 192.168.1.100 NO existe en este sistema. No se puede hacer bind.
❌ Debes añadir la IP primero usando: New-NetIPAddress o ip addr add
❌ Error usando IP de origen 192.168.1.100
```

### En el Servidor

El servidor verá la conexión viniendo desde la IP configurada:
```
REMOTE_ADDR = 192.168.1.100  ← Tu IP configurada
```

---

## Ejemplos Completos 📝

### Ejemplo 1: VTG con IP Personalizada

```java
// Configurar conexión
Connection vtgConnection = new Connection();
vtgConnection.setId("VTG-001");
vtgConnection.setWsName("/vtg");
vtgConnection.setRequestorAddress("10.5.5.100"); // ← IP deseada

// Crear cliente
WSClient vtgClient = new WSClient(
    "VTG", 
    HostType.VTG, 
    vtgConnection, 
    irisService
);

// Enviar mensaje
String response = vtgClient.send(
    "ProcessVTGEvent", 
    "<event>...</event>"
);
```

### Ejemplo 2: LIS con Múltiples IPs

```java
// Cliente 1 - Laboratorio A
Connection lab1 = new Connection();
lab1.setRequestorAddress("192.168.10.1");
WSClient client1 = new WSClient("LIS-Lab1", HostType.LIS, lab1, irisService);

// Cliente 2 - Laboratorio B
Connection lab2 = new Connection();
lab2.setRequestorAddress("192.168.10.2");
WSClient client2 = new WSClient("LIS-Lab2", HostType.LIS, lab2, irisService);

// El servidor verá diferentes IPs de origen
```

---

## Troubleshooting 🔧

### Problema: "La IP NO existe en este sistema"

**Causa**: No has añadido la IP a tu sistema.

**Solución**:
```powershell
# Ejecutar como Administrador
.\manage-ip-alias.ps1

# O manualmente:
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24
```

### Problema: "Address already in use"

**Causa**: La IP o puerto ya está siendo usado por otro proceso.

**Solución**:
```powershell
# Ver qué proceso usa el puerto
netstat -ano | findstr :80

# Matar proceso si es necesario
taskkill /PID <numero> /F
```

### Problema: No puedo añadir IP con PowerShell

**Verifica**:
1. ✅ PowerShell ejecutado como Administrador (clic derecho → "Ejecutar como administrador")
2. ✅ Nombre de interfaz correcto (usa `Get-NetAdapter` para ver el nombre exacto)
3. ✅ IP en rango válido de tu red (192.168.x.x, 10.x.x.x, etc.)

**Ver interfaces disponibles**:
```powershell
Get-NetAdapter | Where-Object {$_.Status -eq "Up"}
```

### Problema: Error "No route to host"

**Causa**: La IP no está en la misma red que el destino.

**Solución**: Usa una IP del mismo rango de red que el servidor destino.

---

## Documentación Completa 📚

- **Guía Técnica Completa**: `REQUESTOR_ADDRESS_GUIDE.md`
- **Resumen de Implementación**: `IMPLEMENTACION_REQUESTOR_ADDRESS.md`
- **Código Fuente**: 
  - `WSClient.java`
  - `ProxyHelper.java`

---

## Scripts Incluidos 🛠️

### manage-ip-alias.ps1

Script interactivo para gestionar IPs:

```powershell
# Ejecutar como Administrador
.\manage-ip-alias.ps1
```

**Funciones**:
- Listar IPs actuales
- Añadir IP alias
- Eliminar IP alias
- Verificar si IP existe

---

## Comandos Útiles 💡

```powershell
# Ver todas las IPs configuradas
Get-NetIPAddress -AddressFamily IPv4

# Ver interfaces de red
Get-NetAdapter

# Añadir IP
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24

# Eliminar IP
Remove-NetIPAddress -IPAddress 192.168.1.100

# Verificar IP específica
Get-NetIPAddress -IPAddress 192.168.1.100
```

---

## ¿Por qué Solo una Opción? 🤔

Antes había 2 opciones (headers HTTP vs bind real). Ahora solo hay **bind real** porque:

- ❌ Tu servidor **NO lee headers HTTP** como `X-Forwarded-For`
- ✅ El bind a IP es la **única forma** de que el servidor vea la IP correcta
- ✅ Garantiza que la IP TCP de origen sea la configurada
- ✅ No requiere cambios en el servidor

**Requisito**: La IP DEBE existir en las interfaces de red de tu sistema.

---

**¡Ya está todo listo! 🎉**

1. Añade la IP con `manage-ip-alias.ps1`
2. Usa `connection.setRequestorAddress("TU_IP")`
3. El servidor verá la conexión desde esa IP automáticamente
