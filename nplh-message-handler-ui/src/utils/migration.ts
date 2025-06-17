import { savedMessagesService } from '../components/savedMessages/services/SavedMessagesService';
import { messageListsService } from '../components/savedMessages/services/MessageListsService';

export const migrateToMultipleListsService = (): void => {
  try {
    // Get existing messages from the old service
    const existingMessages = savedMessagesService.getMessages();
    
    if (existingMessages.length === 0) {
      console.log('No messages to migrate');
      return;
    }

    // Check if we already have lists (migration already done)
    const existingLists = messageListsService.getLists();
    const defaultList = existingLists.find(list => list.name === 'Default' || list.name === 'Mensajes Migrados');
    
    if (defaultList && defaultList.messages.length > 0) {
      console.log('Migration already completed');
      return;
    }

    // Create or use the default list for migration
    let targetList = defaultList;
    if (!targetList) {
      targetList = messageListsService.createList(
        'Mensajes Migrados',
        'Mensajes importados del sistema anterior',
        '#3B82F6'
      );
    }

    // Migrate each message
    let migratedCount = 0;
    for (const message of existingMessages) {
      try {
        messageListsService.addMessage(
          message.content,
          message.host,
          message.messageType,
          message.messageControlId,
          targetList.id
        );
        migratedCount++;
      } catch (error) {
        console.error('Error migrating message:', message.id, error);
      }
    }

    // Clear the old service to avoid confusion
    try {
      savedMessagesService.clearAllMessages();
    } catch (error) {
      console.warn('Could not clear old messages service:', error);
    }

    console.log(`Successfully migrated ${migratedCount} messages to the new lists system`);
    
    // Show a user-friendly notification
    if (migratedCount > 0) {
      const notification = document.createElement('div');
      notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #10B981;
        color: white;
        padding: 12px 16px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 1000;
        font-family: system-ui, -apple-system, sans-serif;
        font-size: 14px;
        max-width: 300px;
      `;
      notification.textContent = `✅ ${migratedCount} mensajes migrados al nuevo sistema de listas`;
      
      document.body.appendChild(notification);
      
      setTimeout(() => {
        if (notification.parentNode) {
          notification.parentNode.removeChild(notification);
        }
      }, 5000);
    }

  } catch (error) {
    console.error('Error during migration:', error);
    
    // Show error notification
    const notification = document.createElement('div');
    notification.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: #EF4444;
      color: white;
      padding: 12px 16px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      z-index: 1000;
      font-family: system-ui, -apple-system, sans-serif;
      font-size: 14px;
      max-width: 300px;
    `;
    notification.textContent = '❌ Error al migrar mensajes. Consulta la consola para más detalles.';
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
      if (notification.parentNode) {
        notification.parentNode.removeChild(notification);
      }
    }, 5000);
  }
};

// Auto-execute migration when this module is imported
migrateToMultipleListsService();
