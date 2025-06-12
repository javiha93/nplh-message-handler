// Re-export for backward compatibility and new structure
export { SavedMessagesService, savedMessagesService } from './services/SavedMessagesService';
export type { SavedMessage, ClientMessageResponse } from './services/SavedMessagesService';

// Component exports
export { default as MessageSidebar } from './components/MessageSidebar';
export { default as MessageViewModal } from './components/MessageViewModal';
export { default as MessageEditModal } from './components/MessageEditModal';
