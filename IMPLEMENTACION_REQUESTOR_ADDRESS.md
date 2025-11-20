# Resumen de Implementación: requestorAddress en WSClient

## ✅ Cambios Realizados

### 1. **WSClient.java** - Cliente WebService
**Ubicación**: `nplh-message-handler-client-server/src/main/java/org/example/client/WSClient.java`

**Nuevas características:**
- ✅ Detecta automáticamente si `connection.requestorAddress` está configurado
- ✅ Intenta hacer bind a la IP si existe en el sistema local
- ✅ Añade headers HTTP estándar: `X-Forwarded-For`, `X-Real-IP`, `X-Originating-IP`
- ✅ Soporte para proxy HTTP/SOCKS (opcional)
- ✅ Logs informativos sobre las opciones disponibles
- ✅ Diagnóstico de interfaces de red al inicializar

### 2. **ProxyHelper.java** - Utilidad de Proxy
**Ubicación**: `nplh-message-handler-client-server/src/main/java/org/example/utils/ProxyHelper.java`

**Funcionalidades:**
- ✅ `createConnectionWithSourceIP()`: Intenta crear conexión con IP específica
- ✅ `isLocalIP()`: Verifica si una IP existe en las interfaces de red locales
- ✅ `printNetworkInterfaces()`: Lista todas las IPs disponibles en el sistema
- ✅ `createConnectionWithSOCKSProxy()`: Soporte para proxy SOCKS
- ✅ Documentación extensa sobre limitaciones de Java

### 3. **REQUESTOR_ADDRESS_GUIDE.md** - Guía Completa
**Ubicación**: `REQUESTOR_ADDRESS_GUIDE.md`

**Contenido:**
- 📖 Explicación del problema técnico (Java no puede hacer IP spoofing real)
- 🔧 4 soluciones diferentes con pros/contras
- 💻 Ejemplos de código para cada solución
- 📊 Tabla comparativa de soluciones
- 🎯 Recomendaciones según el caso de uso

---

## 🎯 Cómo Funciona

### Escenario 1: IP está en el sistema local ✅

Si configuras `requestorAddress` con una IP que **existe** en tu máquina:

```java
connection.setRequestorAddress("192.168.1.50"); // IP configurada en tu PC
```

**Resultado**: El código intentará hacer **bind** a esa IP, la conexión TCP vendrá realmente desde esa IP.

**Logs esperados**:
```
✅ IP 192.168.1.50 encontrada en interfaz: Ethernet
🔧 Intentando bind a IP local: 192.168.1.50
```

### Escenario 2: IP NO está en el sistema ⚠️

Si configuras una IP que **no existe** en tu máquina:

```java
connection.setRequestorAddress("10.0.0.100"); // IP ficticia
```

**Resultado**: El código añadirá **headers HTTP** con esa IP. El servidor DEBE leer estos headers para detectarla.

**Logs esperados**:
```
⚠️ IP 10.0.0.100 no encontrada en interfaces locales
📧 Headers de IP de origen añadidos: 10.0.0.100
```

**Headers enviados**:
```
X-Forwarded-For: 10.0.0.100
X-Real-IP: 10.0.0.100
X-Originating-IP: 10.0.0.100
```

---

## 🔧 Configuración Paso a Paso

### Paso 1: Configurar requestorAddress

En tu código o base de datos, establece el valor:

```java
Connection connection = new Connection();
connection.setRequestorAddress("192.168.1.100");

WSClient client = new WSClient(hostName, hostType, connection, irisService);
```

### Paso 2: (Opcional) Crear IP Alias en tu Sistema

**Solo si quieres que la IP TCP real sea diferente**

**Windows PowerShell (como Administrador):**
```powershell
# Ver interfaces disponibles
Get-NetAdapter

# Añadir IP a la interfaz
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24

# Verificar
Get-NetIPAddress | Where-Object {$_.IPAddress -eq "192.168.1.100"}
```

**Linux:**
```bash
# Añadir IP
sudo ip addr add 192.168.1.100/24 dev eth0

# Verificar
ip addr show eth0
```

### Paso 3: Verificar en Logs

Cuando inicies el cliente, verás:

```
📡 Interfaces de red disponibles:
  Interface: Ethernet
    - IP: 127.0.0.1 (IPv4)
    - IP: 192.168.1.50 (IPv4)
    - IP: 192.168.1.100 (IPv4)  ← Tu IP añadida

⚠️ Cliente MyClient configurado con requestorAddress: 192.168.1.100
✅ IP 192.168.1.100 encontrada en interfaz: Ethernet
```

---

## 📊 Qué Esperar en el Servidor

### Si usas IP Real (con alias):
El servidor verá la conexión desde la IP configurada:
```
REMOTE_ADDR = 192.168.1.100  ✅
```

### Si usas Headers HTTP:
El servidor debe leer los headers:
```
REMOTE_ADDR = 127.0.0.1 o tu IP real
HTTP_X_FORWARDED_FOR = 192.168.1.100  ← Debes leer este
HTTP_X_REAL_IP = 192.168.1.100        ← O este
```

---

## 🚨 Limitaciones Importantes

### ❌ Java NO puede:
1. Cambiar la IP de origen de un socket TCP sin permisos de administrador
2. Hacer IP spoofing real sin librerías nativas (JNI/JNA)
3. Engañar al servidor si no lee los headers HTTP

### ✅ Java SÍ puede:
1. Hacer bind a una IP que exista en el sistema (solución implementada)
2. Añadir headers HTTP con la IP deseada (implementado)
3. Usar un proxy externo que haga el trabajo

---

## 🎓 Ejemplos de Uso

### Ejemplo 1: Testing con IPs locales

```java
// Crear alias 192.168.1.100 en tu sistema
// Luego:
connection.setRequestorAddress("192.168.1.100");
WSClient client = new WSClient("VTG", HostType.VTG, connection, irisService);

// El servidor verá REMOTE_ADDR = 192.168.1.100
```

### Ejemplo 2: Sin modificar el sistema (solo headers)

```java
// No crear alias, solo configurar
connection.setRequestorAddress("10.5.5.5");
WSClient client = new WSClient("LIS", HostType.LIS, connection, irisService);

// El servidor debe leer X-Forwarded-For = 10.5.5.5
```

### Ejemplo 3: Usando proxy externo

```java
// 1. Iniciar proxy HAProxy en puerto 8888
// 2. En WSClient.java, descomentar:
//    InetSocketAddress proxyAddr = new InetSocketAddress("127.0.0.1", 8888);
//    this.proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);

connection.setRequestorAddress("172.16.0.50");
WSClient client = new WSClient("DP600", HostType.DP600, connection, irisService);

// El proxy puede manipular la conexión
```

---

## 🐛 Troubleshooting

### Problema: El servidor no detecta la IP configurada

**Solución 1**: Verifica que la IP esté en tu sistema
```powershell
Get-NetIPAddress | Select-Object IPAddress
```

**Solución 2**: Activa logging debug
```java
// En logback.xml o log4j2.xml
<logger name="org.example.client.WSClient" level="DEBUG"/>
<logger name="org.example.utils.ProxyHelper" level="DEBUG"/>
```

**Solución 3**: Captura tráfico de red
```bash
# Wireshark o tcpdump para ver qué IP sale realmente
tcpdump -i any -n port 80
```

### Problema: "Address already in use"

La IP está siendo usada por otro proceso.

**Solución**:
```powershell
# Ver qué está usando la IP
netstat -ano | findstr :80
```

---

## 📚 Referencias

- **Código implementado**: `WSClient.java`, `ProxyHelper.java`
- **Guía completa**: `REQUESTOR_ADDRESS_GUIDE.md`
- **Diagrama de arquitectura**: `DIAGRAMA_ARQUITECTURA.md`

---

## ✨ Siguientes Pasos

1. **Probar con IP local**: Crear alias y verificar que funciona
2. **Configurar servidor**: Si es posible, hacer que lea headers X-Forwarded-For
3. **Testing**: Verificar en diferentes escenarios
4. **Documentar**: Qué IPs se usarán en producción

---

**Autor**: Sistema de Message Handler  
**Fecha**: Noviembre 2025  
**Versión**: 1.0
