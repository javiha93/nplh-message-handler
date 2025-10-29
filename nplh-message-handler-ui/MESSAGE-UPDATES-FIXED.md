# Actualización de Mensajes - Sistema Arreglado

## ¿Qué se ha arreglado?

He configurado el sistema para que cuando el **backend** envíe actualizaciones de respuestas de mensajes, el **frontend** se actualice automáticamente.

## Cómo funciona

### 1. **Backend → Frontend**
```java
// El backend envía esto:
String powerShellCommand = String.format(
    "Invoke-RestMethod -Uri 'http://localhost:8084/api/ui/messages/update-responses' " +
    "-Method POST -ContentType 'application/json' -Body $body",
    base64Json
);
```

### 2. **Frontend recibe y procesa**
- **Vite server** (puerto 8084) recibe la llamada POST
- **MessageUpdateService** detecta la actualización
- **SavedMessagesService** actualiza los mensajes
- **UI** se re-renderiza automáticamente

## Para probar el sistema

### 1. **Ejecutar ambos servicios:**
```bash
# Terminal 1: Frontend
cd nplh-message-handler-ui
npm run dev

# Terminal 2: Backend  
cd D:\Test
java -jar nplh-message-handler.jar
```

### 2. **Verificar en la consola del navegador:**
```javascript
// Cargar script de prueba
var script = document.createElement('script');
script.src = '/test-message-updates.js';
document.head.appendChild(script);

// Verificar estado
checkServiceStatus();

// Enviar actualización de prueba
testMessageUpdate();
```

### 3. **Probar con curl:**
```bash
curl -X POST http://localhost:8084/api/ui/messages/update-responses \
  -H "Content-Type: application/json" \
  -d '{
    "controlId": "TEST123",
    "responses": [
      {"message": "Respuesta 1", "receiveTime": "2025-01-15T10:30:00Z"},
      {"message": "Respuesta 2", "receiveTime": "2025-01-15T10:31:00Z"}
    ]
  }'
```

## Archivos modificados

### ✅ **Frontend:**
- `vite.config.ts` - Endpoint HTTP para recibir actualizaciones
- `messageUpdateService.ts` - Servicio mejorado con polling y eventos
- `test-message-updates.js` - Script de prueba

### ✅ **Backend:**
- El código existente de PowerShell debería funcionar sin cambios

## Debugging

### **Si no funciona:**

1. **Verificar frontend corriendo:**
   ```bash
   curl http://localhost:8084
   ```

2. **Verificar logs de Vite:**
   - Buscar "Received message update for controlId"

3. **Verificar en consola del navegador:**
   ```javascript
   // Ver si el servicio está activo
   console.log(window.updateMessageResponses);
   
   // Ver actualizaciones globales
   console.log(globalThis.messageUpdates);
   ```

4. **Verificar red en DevTools:**
   - Tab Network → Filter "update-responses"
   - Debería mostrar 200 OK

## Flujo completo

```
Backend Java
    ↓ (PowerShell HTTP POST)
Vite Server (puerto 8084)
    ↓ (procesa /api/ui/messages/update-responses)
MessageUpdateService
    ↓ (polling + eventos)
SavedMessagesService
    ↓ (updateMessageResponses)
UI Components
    ↓ (re-render automático)
Usuario ve nuevas respuestas ✅
```

## ¡Ya está listo!

El sistema debería actualizar automáticamente las respuestas de los mensajes cuando el backend las envíe.
