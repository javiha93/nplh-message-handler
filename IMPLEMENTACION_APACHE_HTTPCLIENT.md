# Implementación RequestorAddress con Apache HttpClient 5

## 📋 Resumen

Implementación completa de binding a IP personalizada usando **Apache HttpClient 5**, con arquitectura limpia separando responsabilidades en clases especializadas.

## 🏗️ Arquitectura

### Estructura de Clases

```
org.example.utils.http/
├── NetworkInterfaceValidator.java    - Validación de IPs disponibles
├── SourceIPConnectionSocketFactory   - Socket factory con bind a IP específica
├── HttpClientFactory.java            - Creación de CloseableHttpClient
└── HttpRequestExecutor.java          - Ejecución de peticiones HTTP/SOAP

org.example.utils/
└── ProxyHelper.java                  - Fachada de alto nivel

org.example.client/
└── WSClient.java                     - Cliente simplificado (mínima lógica)
```

## 📦 Clases Creadas

### 1. **NetworkInterfaceValidator** 
Valida IPs disponibles en el sistema.

**Métodos:**
- `isLocalIP(String ip)` - Verifica si la IP existe en las interfaces de red
- `printNetworkInterfaces()` - Lista todas las IPs disponibles (debug)

**Uso:**
```java
if (NetworkInterfaceValidator.isLocalIP("192.168.1.100")) {
    // IP disponible
}
```

---

### 2. **SourceIPConnectionSocketFactory**
Implementa `PlainConnectionSocketFactory` de Apache HttpClient para bindear sockets a IP específica.

**Constructor:**
```java
new SourceIPConnectionSocketFactory("192.168.1.100")
```

**Funcionalidad:**
- Sobreescribe `createSocket()` para hacer bind antes de conectar
- Valida que la IP exista usando `NetworkInterfaceValidator`
- Lanza `IOException` si la IP no está disponible

---

### 3. **HttpClientFactory**
Factory para crear instancias de `CloseableHttpClient`.

**Métodos:**
- `createDefaultClient()` - Cliente HTTP estándar
- `createClientWithSourceIP(String sourceIP)` - Cliente con bind a IP específica
- `closeClient(CloseableHttpClient)` - Cierre seguro de clientes

**Configuración:**
- Connection timeout: 5 segundos
- Socket timeout: 30 segundos
- Pool size: 50 conexiones totales, 10 por ruta

**Ejemplo:**
```java
CloseableHttpClient client = HttpClientFactory.createClientWithSourceIP("192.168.1.100");
try {
    // Usar cliente
} finally {
    HttpClientFactory.closeClient(client);
}
```

---

### 4. **HttpRequestExecutor**
Ejecuta peticiones HTTP usando Apache HttpClient.

**Métodos:**
- `sendPost(...)` - POST genérico con headers y body personalizados
- `sendSoapPost(...)` - POST SOAP con configuración específica

**Características:**
- Gestión automática de Content-Type
- Headers personalizables
- Retorna respuesta como String
- Logging detallado

**Ejemplo:**
```java
String response = HttpRequestExecutor.sendSoapPost(
    client,
    "http://server/service",
    "urn:Action",
    headers,
    soapBody
);
```

---

### 5. **ProxyHelper** (Refactorizado)
Fachada simplificada para envío de peticiones SOAP con o sin IP personalizada.

**Métodos Públicos:**
```java
// SOAP con IP personalizada
String sendSoapWithSourceIP(url, sourceIP, soapAction, headers, body)

// SOAP estándar
String sendSoapStandard(url, soapAction, headers, body)

// POST genérico con IP personalizada
String sendPostWithSourceIP(url, sourceIP, headers, body, contentType)

// Utilidades
boolean isIPAvailable(String ip)
void printNetworkInterfaces()
```

**Gestión de recursos:**
- Cierra automáticamente los `CloseableHttpClient` en bloques `finally`
- Sin fugas de conexiones

---

### 6. **WSClient** (Modificado)
Cliente simplificado con **mínima lógica** de negocio.

**Cambios:**
```java
public class WSClient {
    private final String requestorAddress;
    
    public WSClient(..., Connection connection, ...) {
        this.requestorAddress = connection.getRequestorAddress();
        
        // Validación en constructor
        if (requestorAddress != null && !requestorAddress.isEmpty()) {
            if (ProxyHelper.isIPAvailable(requestorAddress)) {
                logger.info("✅ IP disponible");
            } else {
                logger.error("❌ IP NO disponible");
                ProxyHelper.printNetworkInterfaces();
            }
        }
    }
    
    public String send(String soapAction, String messageBody, String controlId) {
        try {
            String response;
            
            // Decisión simple: ¿IP personalizada o estándar?
            if (requestorAddress != null && !requestorAddress.isEmpty()) {
                response = ProxyHelper.sendSoapWithSourceIP(
                    targetUrl, requestorAddress, soapAction, headers, requestBody);
            } else {
                response = ProxyHelper.sendSoapStandard(
                    targetUrl, soapAction, headers, requestBody);
            }
            
            // Procesar respuesta...
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }
}
```

**Lógica en WSClient:** < 5 líneas  
**Toda la complejidad:** Delegada a clases especializadas

---

## 🔧 Configuración

### 1. Añadir IP Alias (Windows)

```powershell
# Método 1: PowerShell
.\manage-ip-alias.ps1

# Método 2: Comando directo
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24
```

### 2. Configurar Connection

```java
Connection connection = new Connection();
connection.setRequestorAddress("192.168.1.100");
```

### 3. Usar WSClient

```java
WSClient client = new WSClient(hostName, hostType, connection, irisService);
String response = client.send(soapAction, message, controlId);
```

---

## ✅ Ventajas de esta Arquitectura

### 1. **Separación de Responsabilidades**
- **NetworkInterfaceValidator**: Solo validación de IPs
- **SourceIPConnectionSocketFactory**: Solo creación de sockets
- **HttpClientFactory**: Solo gestión de clientes HTTP
- **HttpRequestExecutor**: Solo ejecución de peticiones
- **ProxyHelper**: Fachada de alto nivel
- **WSClient**: Lógica de negocio mínima

### 2. **Reutilización**
Todas las clases son reutilizables en otros contextos:
```java
// Usar en otro cliente diferente
CloseableHttpClient client = HttpClientFactory.createClientWithSourceIP("10.0.0.5");
String response = HttpRequestExecutor.sendPost(client, url, headers, body, "application/json");
```

### 3. **Testabilidad**
Cada clase es independiente y fácil de testear:
```java
@Test
void testIPValidation() {
    assertTrue(NetworkInterfaceValidator.isLocalIP("127.0.0.1"));
}
```

### 4. **Mantenibilidad**
- Cambios en Apache HttpClient: solo tocar `HttpClientFactory` y `HttpRequestExecutor`
- Cambios en validación de IP: solo `NetworkInterfaceValidator`
- WSClient permanece estable

### 5. **Logging Estructurado**
Cada clase tiene su propio logger:
```
NetworkInterfaceValidator: Validaciones
SourceIPConnectionSocketFactory: Binding de sockets
HttpClientFactory: Creación de clientes
HttpRequestExecutor: Peticiones y respuestas
ProxyHelper: Operaciones de alto nivel
WSClient: Lógica de negocio
```

---

## 🚀 Ejemplo Completo

```java
// 1. Crear conexión
Connection connection = new Connection();
connection.setRequestorAddress("192.168.1.100");
connection.setWsName("/api/service");

// 2. Crear cliente
WSClient client = new WSClient("MyClient", HostType.WS, connection, irisService);

// 3. Enviar mensaje
String response = client.send("urn:MyAction", "<message>...</message>", "CTRL-001");

// Automáticamente:
// - Valida que 192.168.1.100 existe
// - Crea HttpClient con bind a esa IP
// - Envía petición SOAP
// - Retorna respuesta
// - Cierra recursos
```

---

## 📊 Comparación con Implementación Anterior

| Aspecto | Anterior (Manual Socket) | Nuevo (Apache HttpClient) |
|---------|-------------------------|--------------------------|
| **Implementación HTTP** | Manual (StringBuilder) | Apache HttpClient (probado) |
| **Gestión de conexiones** | Manual | Pool automático |
| **Timeouts** | Configuración compleja | API simple |
| **Errores HTTP** | Manejo manual | Excepciones tipadas |
| **Código en WSClient** | ~50 líneas | ~10 líneas |
| **Clases especializadas** | 0 | 5 |
| **Testabilidad** | Baja | Alta |
| **Mantenibilidad** | Media | Alta |

---

## 🔍 Debugging

### Ver IPs disponibles:
```java
ProxyHelper.printNetworkInterfaces();
```

**Salida:**
```
📡 Interfaces de red disponibles:
  Interface: Ethernet
    - IP: 192.168.1.10
    - IP: 192.168.1.100
  Interface: Wi-Fi
    - IP: 10.0.0.5
```

### Verificar IP específica:
```java
if (ProxyHelper.isIPAvailable("192.168.1.100")) {
    logger.info("✅ IP disponible");
} else {
    logger.error("❌ IP NO disponible");
}
```

---

## 📝 Notas Técnicas

### 1. **Apache HttpClient 5**
- Versión: 5.2.1
- Dependencia ya añadida en `pom.xml`
- API moderna con `CloseableHttpClient`

### 2. **Socket Binding**
- Se hace en `SourceIPConnectionSocketFactory.createSocket()`
- Bind antes de `connect()`: `socket.bind(new InetSocketAddress(sourceIP, 0))`
- Puerto 0 = sistema asigna puerto aleatorio disponible

### 3. **Connection Pooling**
- Max 50 conexiones totales
- Max 10 conexiones por ruta
- Reutilización automática de conexiones

### 4. **Gestión de Recursos**
- Patrón try-finally en todos los métodos de `ProxyHelper`
- Cierre automático de clientes HTTP
- Sin fugas de conexiones

---

## 🎯 Próximos Pasos

1. **Testing:** Añadir tests unitarios para cada clase
2. **Configuración:** Externalizar timeouts a properties
3. **Métricas:** Añadir métricas de rendimiento (Micrometer)
4. **SSL:** Soporte para HTTPS con certificados personalizados

---

**✅ Implementación completa y lista para usar**
