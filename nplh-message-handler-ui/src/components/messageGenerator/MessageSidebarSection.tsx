import React, { useState } from 'react';
import { MessageViewModal, MessageEditModal, SavedMessage, savedMessagesService } from '../savedMessages';
import MultiListMessageSidebar from '../savedMessages/components/MultiListMessageSidebar';
import Snackbar from '../Snackbar';

interface MessageSidebarSectionProps {
  // Sidebar state
  isSidebarOpen: boolean;
    // Snackbar state
  snackbar: {
    message: string;
    type: 'success' | 'error' | 'info' | 'warning';
    isVisible: boolean;
  };
  
  // Handlers
  onToggleSidebar: () => void;
  onCloseSnackbar: () => void;
}

const MessageSidebarSection: React.FC<MessageSidebarSectionProps> = ({
  isSidebarOpen,
  snackbar,
  onToggleSidebar,
  onCloseSnackbar
}) => {
  // State for modals
  const [isMessageViewModalOpen, setIsMessageViewModalOpen] = useState(false);
  const [selectedMessage, setSelectedMessage] = useState<SavedMessage | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingMessage, setEditingMessage] = useState<SavedMessage | null>(null);

  // Handlers for message click
  const handleMessageClick = (message: SavedMessage) => {
    setSelectedMessage(message);
    setIsMessageViewModalOpen(true);
  };

  const handleCloseMessageViewModal = () => {
    setIsMessageViewModalOpen(false);
    setSelectedMessage(null);
  };

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
    <>
      <MultiListMessageSidebar
        isOpen={isSidebarOpen}
        onClose={onToggleSidebar}
        onMessageClick={handleMessageClick}
        onEditMessage={handleEditMessage}
      />

      <MessageViewModal
        isOpen={isMessageViewModalOpen}
        onClose={handleCloseMessageViewModal}
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
