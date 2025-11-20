# Guía para Simular IP de Origen (requestorAddress)

## ⚠️ Problema Técnico

Java **NO puede cambiar la IP de origen real** de una conexión TCP/IP sin:
1. Permisos de administrador del sistema operativo
2. Acceso a sockets RAW (no disponible en Java estándar)
3. Modificación de paquetes de red a bajo nivel

## 🔧 Soluciones Disponibles

### Solución 1: Headers HTTP (Implementada) ✅

**Estado**: Ya implementado en `WSClient.java`

**Cómo funciona**:
- El cliente añade headers HTTP: `X-Forwarded-For`, `X-Real-IP`, etc.
- El servidor DEBE estar configurado para leer estos headers

**Limitación**:
- ❌ Requiere que el servidor lea y confíe en estos headers
- ❌ No cambia la IP real de la conexión TCP

**Uso**:
```java
// Ya está implementado automáticamente
connection.setRequestorAddress("192.168.1.100");
WSClient client = new WSClient(hostName, hostType, connection, irisService);
```

---

### Solución 2: Proxy HTTP/SOCKS Externo 🔧

**Estado**: Código preparado, requiere configuración externa

#### Opción A: Usando HAProxy (Recomendado)

**1. Instalar HAProxy:**
```bash
# Windows (con Chocolatey)
choco install haproxy

# Linux
sudo apt-get install haproxy
```

**2. Configurar HAProxy** (`haproxy.cfg`):
```conf
global
    log 127.0.0.1 local0
    maxconn 4096

defaults
    log global
    mode http
    option httplog
    timeout connect 5000ms
    timeout client 50000ms
    timeout server 50000ms

frontend http_front
    bind *:8888
    default_backend http_back

backend http_back
    mode http
    # Añadir header X-Forwarded-For con IP personalizada
    http-request set-header X-Forwarded-For %[src]
    server server1 127.0.0.1:80 check
```

**3. Iniciar HAProxy:**
```bash
haproxy -f haproxy.cfg
```

**4. Descomentar código proxy en WSClient.java:**
```java
// En el constructor de WSClient
InetSocketAddress proxyAddr = new InetSocketAddress("127.0.0.1", 8888);
this.proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
```

#### Opción B: Usando Squid Proxy

**1. Instalar Squid:**
```bash
# Windows
choco install squid

# Linux
sudo apt-get install squid
```

**2. Configurar Squid** (`squid.conf`):
```conf
http_port 8888
acl localnet src 127.0.0.1
http_access allow localnet

# Añadir header personalizado
request_header_add X-Real-IP "192.168.1.100" all
```

**3. Reiniciar Squid:**
```bash
squid -k reconfigure
```

---

### Solución 3: IP Aliasing (Solo si tienes control del servidor) 🖥️

Si ambos cliente y servidor están en la misma máquina, puedes crear un alias de IP:

**Windows (PowerShell como Administrador):**
```powershell
# Añadir IP adicional a la interfaz de red
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.100 -PrefixLength 24

# Verificar
Get-NetIPAddress
```

**Linux:**
```bash
# Añadir IP adicional
sudo ip addr add 192.168.1.100/24 dev eth0

# Verificar
ip addr show
```

Luego el código intentará hacer bind a esa IP automáticamente.

---

### Solución 4: Proxy Python con Scapy (IP Spoofing Real) 🐍

Para hacer IP spoofing real, usa Python con Scapy:

**1. Instalar Python y Scapy:**
```bash
pip install scapy
```

**2. Crear proxy script** (`spoof_proxy.py`):
```python
#!/usr/bin/env python3
from scapy.all import *
import sys

def send_spoofed_http(target_ip, target_port, source_ip, http_request):
    """Envía petición HTTP con IP de origen falseada"""
    
    # Crear paquete IP con IP de origen falseada
    ip_layer = IP(src=source_ip, dst=target_ip)
    
    # Crear paquete TCP
    tcp_layer = TCP(sport=RandShort(), dport=target_port, flags='S')
    
    # Crear paquete SYN
    syn = ip_layer / tcp_layer
    
    # Enviar y recibir SYN-ACK
    syn_ack = sr1(syn, timeout=2)
    
    if syn_ack:
        # Completar handshake TCP
        ack = ip_layer / TCP(sport=syn_ack[TCP].dport, 
                             dport=target_port, 
                             flags='A', 
                             seq=syn_ack[TCP].ack, 
                             ack=syn_ack[TCP].seq + 1)
        send(ack)
        
        # Enviar petición HTTP
        http_pkt = ip_layer / TCP(sport=syn_ack[TCP].dport, 
                                   dport=target_port, 
                                   flags='PA',
                                   seq=syn_ack[TCP].ack, 
                                   ack=syn_ack[TCP].seq + 1) / http_request
        
        response = sr1(http_pkt, timeout=2)
        return response
    
    return None

# Ejemplo de uso
if __name__ == "__main__":
    target = "127.0.0.1"
    port = 80
    spoof_ip = "192.168.1.100"
    
    http_req = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n"
    
    print(f"Enviando petición desde IP falseada: {spoof_ip}")
    response = send_spoofed_http(target, port, spoof_ip, http_req)
    
    if response:
        print("Respuesta recibida:", response.summary())
```

**3. Ejecutar como administrador:**
```bash
# Windows (PowerShell como Admin)
python spoof_proxy.py

# Linux
sudo python3 spoof_proxy.py
```

**IMPORTANTE**: IP Spoofing puede estar bloqueado por:
- Firewalls
- Routers con ingress filtering
- ISP
- Políticas de seguridad de red

---

## 📊 Comparación de Soluciones

| Solución | Cambia IP Real | Dificultad | Requiere Admin | Funciona Siempre |
|----------|---------------|------------|----------------|------------------|
| Headers HTTP | ❌ | Fácil | ❌ | ❌ (servidor debe leer) |
| Proxy HTTP | ❌ | Media | ❌ | ❌ (servidor debe leer) |
| IP Aliasing | ✅ | Media | ✅ | ✅ (si IP existe) |
| Scapy Python | ✅ | Alta | ✅ | ⚠️ (puede ser bloqueado) |

---

## 🎯 Recomendación

**Para desarrollo/testing local:**
1. Usar **Solución 3 (IP Aliasing)** - crear la IP en tu sistema
2. El código actual detectará automáticamente la IP y hará bind

**Para producción:**
1. Preferir **Solución 1 (Headers)** - configurar el servidor para leerlos
2. Es la solución más estándar y mantenible

**Si REALMENTE necesitas cambiar la IP TCP:**
1. Usar **Solución 4 (Scapy)** - pero requiere permisos de admin
2. Crear un servicio proxy en Python que reciba peticiones de Java

---

## 🔍 Verificar IP Detectada por el Servidor

**En el servidor (InterSystems IRIS/Caché):**
```objectscript
// Leer IP real
Set realIP = %request.GetCgiEnv("REMOTE_ADDR")

// Leer IP de headers
Set forwardedIP = %request.GetCgiEnv("HTTP_X_FORWARDED_FOR")
Set realIP2 = %request.GetCgiEnv("HTTP_X_REAL_IP")

// Usar la primera disponible
Set clientIP = $Select(
    forwardedIP'="": forwardedIP,
    realIP2'="": realIP2,
    1: realIP
)

Write "IP detectada: ", clientIP
```

---

## 📝 Código Actual en WSClient

El código actual ya implementa:
- ✅ Detección de `requestorAddress`
- ✅ Añadir headers HTTP con la IP deseada
- ✅ Logs informativos
- ✅ Preparado para usar proxy (comentado)

**Para activar proxy**, descomentar en el constructor:
```java
InetSocketAddress proxyAddr = new InetSocketAddress("127.0.0.1", 8888);
this.proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
```
