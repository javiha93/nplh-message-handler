# SavedMessages Edit Functionality Documentation

## Overview
Esta documentación describe la funcionalidad de edición de mensajes guardados implementada en el módulo SavedMessages.

## Features Implemented

### 1. Message Edit Capability
- Los mensajes guardados que **NO** han sido enviados pueden ser editados
- Los mensajes enviados son de solo lectura para mantener la integridad de los datos
- Se muestra un indicador visual para distinguir entre mensajes editables y no editables

### 2. Edit Modal (`MessageEditModal`)
Un modal dedicado para la edición de mensajes con las siguientes características:

#### Visual Indicators
- **Editable Messages**: Título "Editar Mensaje" con textarea editable
- **Sent Messages**: Título "Ver Mensaje (Solo Lectura)" con contenido en modo lectura
- Badge "Enviado" para mensajes que ya fueron enviados

#### Editing Features
- Textarea con sintaxis de código (font-mono) para facilitar la edición de XML/mensajes
- Auto-focus en el campo de edición al abrir el modal
- Detección de cambios con indicador visual
- Shortcuts de teclado:
  - `Ctrl+S`: Guardar cambios
  - `Esc`: Cerrar modal

#### UI Elements
- Botón "Resetear" para descartar cambios
- Botón "Guardar" (solo habilitado si hay cambios)
- Notificación de cambios no guardados
- Footer con acciones principales

### 3. Service Updates (`SavedMessagesService`)

#### New Methods
```typescript
updateMessageContent(messageId: string, newContent: string): void
```
- Actualiza el contenido de un mensaje específico
- Notifica a todos los listeners de los cambios

#### Enhanced Message Interface
```typescript
export interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;
  sentTimestamp?: Date; // Nueva propiedad para fecha de envío
  responses?: string[];
}
```

### 4. UI Integration

#### MessageSidebar Updates
- Nuevo botón "Editar" (icono `Edit`) en mensajes no enviados
- El botón solo aparece si `onEditMessage` está definido y el mensaje no tiene `sentTimestamp`
- Iconografía consistente con el resto de botones de acción

#### MessageSidebarSection Updates
- Estado local para manejar el modal de edición
- Handlers para abrir/cerrar el modal y guardar cambios
- Integración completa con el sistema de modals existente

### 5. Timestamp Behavior
- **Fecha de creación** (`timestamp`): Se mantiene como antes
- **Fecha de envío** (`sentTimestamp`): Se establece solo cuando se envía el mensaje
- **Display Logic**: 
  - Sin enviar: No muestra fecha
  - Enviado: Muestra "Enviado: DD/MM/YY HH:MM:SS"

## Usage Flow

### Editing a Message
1. Usuario ve mensaje no enviado en sidebar
2. Click en botón "Editar" (icono lápiz)
3. Modal se abre con contenido editable
4. Usuario modifica el contenido
5. Guarda con `Ctrl+S` o botón "Guardar"
6. Modal se cierra, cambios se persisten

### Viewing a Sent Message
1. Usuario ve mensaje enviado (con fecha de envío)
2. Click en mensaje abre el modal de vista/edición
3. Modal muestra contenido en modo de solo lectura
4. Indica claramente que el mensaje no puede editarse

## File Structure

```
src/components/savedMessages/
├── components/
│   ├── MessageSidebar.tsx          # Botón de edición agregado
│   ├── MessageViewModal.tsx        # Muestra fecha de envío
│   └── MessageEditModal.tsx        # Nuevo modal de edición
├── services/
│   └── SavedMessagesService.ts     # Método updateMessageContent agregado
└── index.ts                        # Exporta MessageEditModal
```

## Technical Details

### State Management
- Edición se maneja localmente en `MessageSidebarSection`
- Cambios se persisten a través del `SavedMessagesService`
- Reactivity automática a través del sistema de listeners existente

### Security & Data Integrity
- Mensajes enviados son inmutables (no pueden editarse)
- Validación en UI y lógica de negocio
- Preservación de metadatos originales (timestamp, host, messageType, etc.)

### Performance
- Modal de edición se carga solo cuando es necesario
- Sin impacto en el rendering de la lista de mensajes
- Detección de cambios optimizada

## Future Enhancements

### Possible Improvements
1. **Auto-save**: Guardar cambios automáticamente cada cierto tiempo
2. **Version History**: Mantener historial de cambios de contenido
3. **Validation**: Validar formato XML antes de guardar
4. **Diff View**: Mostrar diferencias entre versión original y editada
5. **Bulk Edit**: Permitir edición de múltiples mensajes
6. **Edit Permissions**: Control de acceso basado en roles

### Code Organization
- Considerar extraer lógica de edición a un hook personalizado (`useMessageEdit`)
- Implementar validación de contenido en el servicio
- Agregar tests unitarios para la funcionalidad de edición
