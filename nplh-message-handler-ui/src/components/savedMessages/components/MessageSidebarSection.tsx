import React, { useState } from 'react';
import { MessageViewModal, MessageEditModal } from '..';
import { SavedMessage } from '../services/MessageListsService';
import MultiListMessageSidebar from './MultiListMessageSidebar';
import Snackbar from '../../Snackbar';
import { messageListsService } from '../services/MessageListsService';

interface MessageSidebarSectionProps {
  // Sidebar state
  isSidebarOpen: boolean;
  
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
  onMessageClick: (message: SavedMessage) => void;
  onCloseMessageViewModal: () => void;
  onCloseSnackbar: () => void;
}

const MessageSidebarSection: React.FC<MessageSidebarSectionProps> = ({
  isSidebarOpen,
  isMessageViewModalOpen,
  selectedMessage,
  snackbar,
  onToggleSidebar,
  onMessageClick,
  onCloseMessageViewModal,
  onCloseSnackbar
}) => {
  // State for edit modal
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingMessage, setEditingMessage] = useState<SavedMessage | null>(null);

  // Edit handlers
  const handleEditMessage = (message: SavedMessage) => {
    setEditingMessage(message);
    setIsEditModalOpen(true);
  };

  const handleSaveEdit = (messageId: string, newContent: string) => {
    messageListsService.updateMessageContent(messageId, newContent);
    setIsEditModalOpen(false);
    setEditingMessage(null);
  };
  
  const handleSaveComment = (messageId: string, comment: string) => {
    messageListsService.updateMessageComment(messageId, comment);
  };

  const handleSaveControlId = (messageId: string, controlId: string) => {
    messageListsService.updateMessageControlId(messageId, controlId);
  };

  const handleCloseEditModal = () => {
    setIsEditModalOpen(false);
    setEditingMessage(null);
  };  return (
    <>
      <MultiListMessageSidebar
        isOpen={isSidebarOpen}
        onClose={onToggleSidebar}
        onMessageClick={onMessageClick}
        onEditMessage={handleEditMessage}
      />

      <MessageViewModal
        isOpen={isMessageViewModalOpen}
        onClose={onCloseMessageViewModal}
        message={selectedMessage}
      />

      <MessageEditModal
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
