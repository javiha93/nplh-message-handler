import { useState, useEffect } from 'react';
import { MessageList, SavedMessage, messageListsService } from '../components/savedMessages/services/MessageListsService';

export interface UseMessageListsReturn {
  // State
  lists: MessageList[];
  activeList: MessageList | null;
  activeListId: string | null;
  savedMessages: SavedMessage[];
  isSendingAll: boolean;

  // List management
  createList: (name: string, description?: string, color?: string) => MessageList;
  updateList: (listId: string, updates: { name?: string; description?: string; color?: string }) => void;
  deleteList: (listId: string) => void;
  duplicateList: (listId: string, newName?: string) => MessageList;
  setActiveList: (listId: string) => void;

  // Message management
  addMessage: (content: string, host: string, messageType: string, messageControlId?: string, listId?: string) => SavedMessage;
  removeMessage: (messageId: string, listId?: string) => void;
  moveMessage: (messageId: string, fromListId: string, toListId: string) => void;
  sendMessage: (message: SavedMessage, listId?: string) => Promise<void>;
  sendAllMessages: (listId?: string) => Promise<void>;
  clearAllResponses: (listId?: string) => void;
  clearMessageResponses: (messageId: string, listId?: string) => void;
  reorderMessages: (startIndex: number, endIndex: number, listId?: string) => void;

  // Import/Export
  exportList: (listId: string) => void;
  exportAllLists: () => void;
  importList: () => void;
}

export const useMessageLists = (): UseMessageListsReturn => {
  const [lists, setLists] = useState<MessageList[]>([]);
  const [activeListId, setActiveListId] = useState<string | null>(null);
  const [isSendingAll, setIsSendingAll] = useState(false);

  const activeList = lists.find(list => list.id === activeListId) || null;
  const savedMessages = activeList?.messages || [];

  // Subscribe to service updates
  useEffect(() => {
    const unsubscribe = messageListsService.subscribe((newLists, newActiveListId) => {
      setLists(newLists);
      setActiveListId(newActiveListId);
    });

    return unsubscribe;
  }, []);

  // List management functions
  const createList = (name: string, description?: string, color?: string): MessageList => {
    return messageListsService.createList(name, description, color);
  };

  const updateList = (listId: string, updates: { name?: string; description?: string; color?: string }): void => {
    messageListsService.updateList(listId, updates);
  };

  const deleteList = (listId: string): void => {
    messageListsService.deleteList(listId);
  };

  const duplicateList = (listId: string, newName?: string): MessageList => {
    return messageListsService.duplicateList(listId, newName);
  };

  const setActiveListHandler = (listId: string): void => {
    messageListsService.setActiveList(listId);
  };

  // Message management functions
  const addMessage = (
    content: string, 
    host: string, 
    messageType: string, 
    messageControlId?: string, 
    listId?: string
  ): SavedMessage => {
    return messageListsService.addMessage(content, host, messageType, messageControlId, listId);
  };

  const removeMessage = (messageId: string, listId?: string): void => {
    messageListsService.removeMessage(messageId, listId);
  };

  const moveMessage = (messageId: string, fromListId: string, toListId: string): void => {
    messageListsService.moveMessage(messageId, fromListId, toListId);
  };

  const sendMessage = async (message: SavedMessage, listId?: string): Promise<void> => {
    try {
      await messageListsService.sendMessage(message, listId);
    } catch (error) {
      console.error('Error sending message:', error);
      throw error;
    }
  };

  const sendAllMessages = async (listId?: string): Promise<void> => {
    const targetMessages = listId 
      ? lists.find(l => l.id === listId)?.messages || []
      : savedMessages;

    if (targetMessages.length === 0) {
      throw new Error('No hay mensajes para enviar');
    }

    setIsSendingAll(true);
    try {
      for (const message of targetMessages) {
        await messageListsService.sendMessage(message, listId);
      }
    } catch (error) {
      console.error('Error sending all messages:', error);
      throw error;
    } finally {
      setIsSendingAll(false);
    }
  };

  const clearAllResponses = (listId?: string): void => {
    messageListsService.clearAllResponses(listId);
  };
  const clearMessageResponses = (messageId: string, listId?: string): void => {
    messageListsService.clearMessageResponses(messageId, listId);
  };

  const reorderMessages = (startIndex: number, endIndex: number, listId?: string): void => {
    messageListsService.reorderMessages(startIndex, endIndex, listId);
  };

  // Import/Export functions
  const exportList = (listId: string): void => {
    try {
      const exportData = messageListsService.exportList(listId);
      const blob = new Blob([exportData], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      const list = lists.find(l => l.id === listId);
      a.href = url;
      a.download = `${list?.name || 'lista'}_${new Date().toISOString().split('T')[0]}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error exporting list:', error);
      throw new Error('Error al exportar la lista');
    }
  };

  const exportAllLists = (): void => {
    try {
      const exportData = messageListsService.exportAllLists();
      const blob = new Blob([exportData], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `todas_las_listas_${new Date().toISOString().split('T')[0]}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error exporting all lists:', error);
      throw new Error('Error al exportar todas las listas');
    }
  };

  const importList = (): void => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = async (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (!file) return;

      try {
        const text = await file.text();
        const importData = JSON.parse(text);
        
        if (importData.lists && Array.isArray(importData.lists)) {
          // Import multiple lists
          for (const listData of importData.lists) {
            const newList = messageListsService.createList(
              listData.name || 'Lista Importada',
              listData.description,
              listData.color
            );

            if (listData.messages && Array.isArray(listData.messages)) {
              for (const msgData of listData.messages) {
                messageListsService.addMessage(
                  msgData.content,
                  msgData.host,
                  msgData.messageType,
                  msgData.messageControlId,
                  newList.id
                );
              }
            }
          }
          alert(`${importData.lists.length} listas importadas exitosamente.`);
        } else {
          // Import single list
          const newList = messageListsService.createList(
            importData.listInfo?.name || 'Lista Importada',
            importData.listInfo?.description,
            importData.listInfo?.color
          );

          if (importData.messages && Array.isArray(importData.messages)) {
            for (const msgData of importData.messages) {
              messageListsService.addMessage(
                msgData.content,
                msgData.host,
                msgData.messageType,
                msgData.messageControlId,
                newList.id
              );
            }
          }

          alert(`Lista "${newList.name}" importada exitosamente con ${importData.messages?.length || 0} mensajes.`);
        }
      } catch (error) {
        console.error('Error importing:', error);
        alert('Error al importar. Verifica que el archivo sea válido.');
      }
    };
    input.click();
  };

  return {
    // State
    lists,
    activeList,
    activeListId,
    savedMessages,
    isSendingAll,

    // List management
    createList,
    updateList,
    deleteList,
    duplicateList,
    setActiveList: setActiveListHandler,

    // Message management
    addMessage,
    removeMessage,
    moveMessage,
    sendMessage,
    sendAllMessages,
    clearAllResponses,
    clearMessageResponses,
    reorderMessages,

    // Import/Export
    exportList,
    exportAllLists,
    importList
  };
};
