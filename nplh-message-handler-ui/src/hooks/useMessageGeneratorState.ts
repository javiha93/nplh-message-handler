import { useState } from 'react';
import { SavedMessage } from '../components/savedMessages';

export const useMessageGeneratorState = () => {
  const [isMessageViewModalOpen, setIsMessageViewModalOpen] = useState(false);
  const [selectedMessage, setSelectedMessage] = useState<SavedMessage | null>(null);

  const handleMessageClick = (message: SavedMessage) => {
    setSelectedMessage(message);
    setIsMessageViewModalOpen(true);
  };

  const closeMessageViewModal = () => {
    setIsMessageViewModalOpen(false);
    setSelectedMessage(null);
  };

  return {
    isMessageViewModalOpen,
    selectedMessage,
    handleMessageClick,
    closeMessageViewModal
  };
};
