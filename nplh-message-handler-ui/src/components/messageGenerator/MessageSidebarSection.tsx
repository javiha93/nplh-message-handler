import React, { useState } from 'react';
import { MessageSidebar, MessageViewModal, MessageEditModal, SavedMessage, savedMessagesService } from '../savedMessages';
import Snackbar from '../Snackbar';

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
  onClearMessageResponses: (messageId: string) => void;  onReorderMessages: (startIndex: number, endIndex: number) => void;
  onMessageClick: (message: SavedMessage) => void;
  onCloseMessageViewModal: () => void;
  onCloseSnackbar: () => void;
  onExportMessages?: () => void;
  onImportMessages?: () => void;
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
  onCloseSnackbar,
  onExportMessages,
  onImportMessages
}) => {
  // State for edit modal
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingMessage, setEditingMessage] = useState<SavedMessage | null>(null);

  // Handlers for edit functionality
  const handleEditMessage = (message: SavedMessage) => {
    setEditingMessage(message);
    setIsEditModalOpen(true);
  };

  const handleSaveEdit = (messageId: string, newContent: string) => {
    savedMessagesService.updateMessageContent(messageId, newContent);
    setIsEditModalOpen(false);
    setEditingMessage(null);
  };
  const handleSaveComment = (messageId: string, comment: string) => {
    savedMessagesService.updateMessageComment(messageId, comment);
  };

  const handleSaveControlId = (messageId: string, controlId: string) => {
    savedMessagesService.updateMessageControlId(messageId, controlId);
  };

  const handleCloseEditModal = () => {
    setIsEditModalOpen(false);
    setEditingMessage(null);
  };

  return (
    <>      <MessageSidebar
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
        onEditMessage={handleEditMessage}
        onExportMessages={onExportMessages}
        onImportMessages={onImportMessages}
      />

      <MessageViewModal
        isOpen={isMessageViewModalOpen}
        onClose={onCloseMessageViewModal}
        message={selectedMessage}
      />      <MessageEditModal
        isOpen={isEditModalOpen}
        onClose={handleCloseEditModal}
        message={editingMessage}
        onSave={handleSaveEdit}
        onSaveComment={handleSaveComment}
        onSaveControlId={handleSaveControlId}
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
