// Re-export for backward compatibility and new structure
export { SavedMessagesService, savedMessagesService } from './services/SavedMessagesService';
export type { SavedMessage, ClientMessageResponse } from './services/SavedMessagesService';

// New message lists service exports
export { MessageListsService, messageListsService } from './services/MessageListsService';
export type { MessageList } from './services/MessageListsService';

// Component exports
export { default as MessageSidebar } from './components/MessageSidebar';
export { default as MultiListMessageSidebar } from './components/MultiListMessageSidebar';
export { default as MessageViewModal } from './components/MessageViewModal';
export { default as MessageEditModal } from './components/MessageEditModal';
