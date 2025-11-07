# SavedMessages Module Documentation

## Overview
The SavedMessages module has been reorganized into a dedicated folder structure under `src/components/savedMessages/` to improve code organization and maintainability.

## Folder Structure

```
src/components/savedMessages/
├── index.ts                    # Module exports
├── services/
│   └── SavedMessagesService.ts # Service logic
└── components/
    ├── MessageSidebar.tsx      # Sidebar component
    └── MessageViewModal.tsx    # Modal for viewing messages
```

## Key Features

### 1. **Timestamp Management**
- **Creation Timestamp**: Messages have a `timestamp` field when created
- **Send Timestamp**: Messages get a `sentTimestamp` field when sent
- **Display Logic**: Only sent timestamp is shown in the UI (with seconds precision)
- **Empty State**: Messages show no date until they are sent

### 2. **Service Architecture**
- **Singleton Pattern**: Single instance of SavedMessagesService
- **Event-Driven**: Uses listeners for reactive updates
- **State Management**: Centralized message state with immutable updates

### 3. **Component Structure**
- **MessageSidebar**: org.example.Main sidebar component with drag-and-drop functionality
- **MessageViewModal**: Full-screen modal for detailed message viewing
- **Responsive Design**: Resizable sidebar with proper constraints

## API Reference

### SavedMessage Interface
```typescript
interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;           // Creation time
  sentTimestamp?: Date;      // Send time (optional)
  responses?: string[];
}
```

### Key Methods

#### SavedMessagesService
- `addMessage()`: Creates a new saved message
- `sendMessage()`: Sends a message and updates sentTimestamp
- `clearMessageResponses()`: Clears responses and sentTimestamp
- `clearAllResponses()`: Clears all responses and sentTimestamps
- `reorderMessages()`: Reorders messages via drag-and-drop

## Usage Examples

### Displaying Timestamps
```tsx
// In MessageSidebar
{message.sentTimestamp ? `Enviado: ${formatTimestamp(message.sentTimestamp)}` : ''}

// In MessageViewModal
{message.sentTimestamp && (
  <span className="text-gray-500">Enviado: {formatTimestamp(message.sentTimestamp)}</span>
)}
```

### Formatting Timestamps
```typescript
const formatTimestamp = (timestamp: Date) => {
  return new Date(timestamp).toLocaleString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
};
```

## Migration Guide

### From Old Structure
The following files have been moved:
- `src/services/SavedMessagesService.ts` → `src/components/savedMessages/services/SavedMessagesService.ts`
- `src/components/messageGenerator/MessageSidebar.tsx` → `src/components/savedMessages/components/MessageSidebar.tsx`
- `src/components/messageGenerator/MessageViewModal.tsx` → `src/components/savedMessages/components/MessageViewModal.tsx`

### Import Updates
```typescript
// Old import
import { SavedMessagesService, SavedMessage } from '../services/SavedMessagesService';

// New import
import { SavedMessagesService, SavedMessage } from '../components/savedMessages';
```

## Features

### 1. **Empty Timestamp Display**
- Messages show no timestamp when created
- Timestamp appears only after sending with format: "Enviado: DD/MM/YY HH:MM:SS"

### 2. **Automatic Timestamp Management**
- `sentTimestamp` is automatically set when `sendMessage()` is called
- Clearing responses also clears the `sentTimestamp`
- Format includes seconds for precise tracking

### 3. **Backward Compatibility**
- All existing functionality preserved
- Service interface remains unchanged
- Components work seamlessly with existing hooks

## Testing

To test the timestamp functionality:
1. Create a new message (should show no timestamp)
2. Send the message (should show "Enviado: [timestamp with seconds]")
3. Clear responses (should hide timestamp again)
4. Send again (should show new timestamp)

## Future Enhancements

Potential improvements for the module:
- Add message status indicators
- Implement message categories
- Add search/filter functionality
- Export/import message collections
- Message templates

## Dependencies

The module depends on:
- `MessageService` for sending messages
- `react-beautiful-dnd` for drag-and-drop
- `lucide-react` for icons
- Native browser APIs for timestamps
