import React from 'react';
import { MessageSidebar, MessageViewModal, SavedMessage } from '..';
import Snackbar from '../../Snackbar';

interface MessageSidebarSectionProps {
  // Sidebar state
  isSidebarOpen: boolean;
  savedMessages: SavedMessage[];
  isSendingAll: boolean;
  
  // Modal state
  isMessageViewModalOpen: boolean;
  selectedMessage: SavedMessage | null;
    // Snackbar state
  snackbar: {
    message: string;
    type: 'success' | 'error' | 'info' | 'warning';
    isVisible: boolean;
  };
  
  // Handlers
  onToggleSidebar: () => void;
  onRemoveMessage: (id: string) => void;
  onSendMessage: (message: SavedMessage) => void;
  onSendAllMessages: () => void;
  onClearAllResponses: () => void;
  onClearMessageResponses: (messageId: string) => void;
  onReorderMessages: (startIndex: number, endIndex: number) => void;
  onMessageClick: (message: SavedMessage) => void;
  onCloseMessageViewModal: () => void;
  onCloseSnackbar: () => void;
}

const MessageSidebarSection: React.FC<MessageSidebarSectionProps> = ({
  isSidebarOpen,
  savedMessages,
  isSendingAll,
  isMessageViewModalOpen,
  selectedMessage,
  snackbar,
  onToggleSidebar,
  onRemoveMessage,
  onSendMessage,
  onSendAllMessages,
  onClearAllResponses,
  onClearMessageResponses,
  onReorderMessages,
  onMessageClick,
  onCloseMessageViewModal,
  onCloseSnackbar
}) => {
  return (
    <>
      <MessageSidebar
        isOpen={isSidebarOpen}
        onClose={onToggleSidebar}
        savedMessages={savedMessages}
        onRemoveMessage={onRemoveMessage}
        onSendMessage={onSendMessage}
        onSendAllMessages={onSendAllMessages}
        onClearAllResponses={onClearAllResponses}
        onClearMessageResponses={onClearMessageResponses}
        onReorderMessages={onReorderMessages}
        isSendingAll={isSendingAll}
        onMessageClick={onMessageClick}
      />

      <MessageViewModal
        isOpen={isMessageViewModalOpen}
        onClose={onCloseMessageViewModal}
        message={selectedMessage}
      />

      <Snackbar
        message={snackbar.message}
        type={snackbar.type}
        isVisible={snackbar.isVisible}
        onClose={onCloseSnackbar}
      />
    </>
  );
};

export default MessageSidebarSection;
