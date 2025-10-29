# Reagent Management Feature

## Overview
Esta nueva funcionalidad permite agregar reagents a las slides cuando se edita una slide en el generador de mensajes.

## Funcionalidades Implementadas

### 1. Tipos de Datos
- **Reagent Interface**: Nueva interfaz que define la estructura de un reagent
  - `expirationDateTime`: Fecha y hora de expiración (ISO string)
  - `intendedUseFlag`: Bandera de uso previsto
  - `lotNumber`: Número de lote
  - `lotSerialNumber`: Número de serie del lote
  - `catalogNumber`: Número de catálogo
  - `manufacturer`: Fabricante
  - `receivedDateTime`: Fecha y hora de recepción (ISO string)
  - `substanceName`: Nombre de la sustancia (requerido)
  - `substanceOtherName`: Nombre alternativo de la sustancia
  - `substanceType`: Tipo de sustancia (ej: Stain, Antibody, Chromogen, Fixative)

- **ReagentsList Interface**: Lista de reagents
- **Slide Interface**: Actualizada para incluir `reagents?: ReagentsList`

### 2. Gestión de Estado (FormStateService)
- **availableReagents**: Lista de reagents disponibles en el sistema
- **selectedSlideReagents**: Lista de reagents seleccionados para la slide actual

#### Métodos Agregados:
- `setAvailableReagents(reagents: Reagent[])`: Establece los reagents disponibles
- `addReagentToSlide(reagent: Reagent)`: Agrega un reagent a la slide seleccionada
- `removeReagentFromSlide(reagentId: string)`: Remueve un reagent de la slide
- `clearSlideReagents()`: Limpia todos los reagents de la slide
- `getAvailableReagents()`: Obtiene los reagents disponibles
- `getSelectedSlideReagents()`: Obtiene los reagents de la slide seleccionada
- `updateSelectedSlideWithReagents()`: Actualiza la slide con los reagents antes de enviar

### 3. Componente UI (ReagentManager)
Componente React que permite:
- **Ver reagents seleccionados**: Muestra los reagents actualmente asignados a la slide
- **Agregar reagents existentes**: Seleccionar de una lista de reagents disponibles
- **Crear nuevos reagents**: Formulario para crear reagents personalizados
- **Remover reagents**: Eliminar reagents de la slide

#### Campos del Formulario para Nuevo Reagent:
- Substance Name (requerido)
- Substance Other Name (opcional)
- Substance Type (opcional)
- Manufacturer (opcional)
- Lot Number (opcional)
- Lot Serial Number (opcional)
- Catalog Number (opcional)
- Intended Use Flag (opcional)
- Expiration Date/Time (opcional)
- Received Date/Time (opcional)

### 4. Integración en MessageOptions
El componente ReagentManager se muestra automáticamente cuando:
- Se está mostrando el selector de slides (`showSlideSelector` es true)
- Se ha seleccionado una slide (`selectedSlide` no es null)
- No se está usando el selector de entidades (`showEntitySelector` es false)

### 5. Reagents por Defecto
El sistema se inicializa con algunos reagents de ejemplo:
- **Hematoxylin & Eosin** (Stain) - H&E Stain
- **Anti-CD20 Antibody** (Antibody) - CD20 Monoclonal
- **DAB Chromogen** (Chromogen) - 3,3-Diaminobenzidine
- **Neutral Buffered Formalin** (Fixative) - NBF 10%

## Flujo de Uso

1. **Seleccionar Host y Message Type** que requiera slide selector
2. **Seleccionar Slide** usando el botón "Select Slide"
3. **Gestionar Reagents**: 
   - El componente ReagentManager aparece automáticamente
   - Usar "Add Reagent" para agregar reagents existentes o crear nuevos
   - Los reagents seleccionados se muestran en la lista
   - Usar "Remove" para eliminar reagents
4. **Generar Mensaje**: 
   - Al generar el mensaje, los reagents se incluyen automáticamente en la slide
   - El backend recibe la slide con la lista de reagents en `slide.reagents.reagentList`

## Estructura del Mensaje Enviado al Backend

```json
{
  "message": { ... },
  "messageType": "...",
  "slide": {
    "id": "slide-id",
    "entityName": "slide-name",
    "reagents": {
      "reagentList": [
        {
          "substanceName": "Hematoxylin & Eosin",
          "substanceOtherName": "H&E Stain",
          "substanceType": "Stain",
          "manufacturer": "Sigma-Aldrich",
          "lotNumber": "HE2024001",
          "lotSerialNumber": "SN-HE-001",
          "catalogNumber": "CAT-HE-2024",
          "intendedUseFlag": "Routine Staining",
          "expirationDateTime": "2025-12-31T23:59:59",
          "receivedDateTime": "2024-01-15T08:00:00"
        }
      ]
    }
  }
}
```

## Consideraciones Técnicas

- Los reagents se persisten en el estado mientras se mantiene la sesión
- Al cambiar de slide, los reagents se cargan automáticamente si la slide ya tenía reagents asignados
- La validación de reagents duplicados se maneja automáticamente
- El componente es responsive y se adapta al diseño existente
- Los reagents se envían al backend como parte del objeto slide

## Archivos Modificados

- `src/types/Message.ts`: Agregadas interfaces Reagent y ReagentsList
- `src/services/FormStateService.ts`: Agregada gestión de estado para reagents
- `src/components/messageGenerator/ReagentManager.tsx`: Nuevo componente UI
- `src/components/messageGenerator/MessageOptions.tsx`: Integración del ReagentManager
- `src/hooks/useMessageGenerator.ts`: Actualización de reagents antes de enviar mensaje
