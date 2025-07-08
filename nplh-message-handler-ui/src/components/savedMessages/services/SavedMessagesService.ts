import { messageService, SendMessageRequest } from '../../../services/MessageService';
import { hostService } from '../../../services/HostService';

// Interface matching the backend ClientMessageResponse
export interface ClientMessageResponse {
  message: string;
  receiveTime: string; // LocalDateTime comes as ISO string from backend
}

export interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;
  sentTimestamp?: Date;
  comment?: string; // New field for user comments
  responses?: ClientMessageResponse[]; // Changed from string[] to ClientMessageResponse[]
}

export class SavedMessagesService {
  private static instance: SavedMessagesService;
  private messages: SavedMessage[] = [];
  private listeners: ((messages: SavedMessage[]) => void)[] = [];
  static getInstance(): SavedMessagesService {
    if (!SavedMessagesService.instance) {
      SavedMessagesService.instance = new SavedMessagesService();
      SavedMessagesService.instance.initializeMigrations();
    }
    return SavedMessagesService.instance;
  }

  subscribe(listener: (messages: SavedMessage[]) => void): () => void {
    this.listeners.push(listener);
    listener(this.messages); // Initial call
    
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }

  private notifyListeners(): void {
    this.listeners.forEach(listener => listener(this.messages));
  }

  getMessages(): SavedMessage[] {
    return [...this.messages];
  }

  addMessage(
    content: string,
    host: string,
    messageType: string,
    messageControlId?: string
  ): SavedMessage {
    const newMessage: SavedMessage = {
      id: Date.now().toString(),
      content,
      host,
      messageType,
      messageControlId,
      timestamp: new Date()
    };

    this.messages.push(newMessage);
    this.notifyListeners();
    return newMessage;
  }

  removeMessage(id: string): void {
    this.messages = this.messages.filter(msg => msg.id !== id);
    this.notifyListeners();
  }

  updateMessageResponses(controlId: string, responses: ClientMessageResponse[]): void {
    console.log(`updateMessageResponses called with controlId: ${controlId}`, responses);
    console.log('Current messages before update:', this.messages.map(m => ({ id: m.id, controlId: m.messageControlId })));
    
    this.messages = this.messages.map(msg => 
      msg.messageControlId === controlId 
        ? { ...msg, responses: responses }
        : msg
    );
    
    console.log('Updated messages:', this.messages.map(m => ({ id: m.id, controlId: m.messageControlId, hasResponses: !!m.responses })));
    this.notifyListeners();
    console.log('Listeners notified');
  }
  clearAllResponses(): void {
    this.messages = this.messages.map(msg => ({
      ...msg,
      responses: undefined,
      sentTimestamp: undefined
    }));
    this.notifyListeners();
  }

  clearMessageResponses(messageId: string): void {
    this.messages = this.messages.map(msg => 
      msg.id === messageId 
        ? { ...msg, responses: undefined, sentTimestamp: undefined }
        : msg
    );
    this.notifyListeners();
  }

  reorderMessages(startIndex: number, endIndex: number): void {
    const result = Array.from(this.messages);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    this.messages = result;
    this.notifyListeners();
  }  async sendMessage(savedMessage: SavedMessage): Promise<ClientMessageResponse[]> {
    // Migrar el host si es necesario para mantener la compatibilidad
    const migratedHost = hostService.migrateHost(savedMessage.host);
    
    const request: SendMessageRequest = {
      message: savedMessage.content,
      hostName: migratedHost, // Usar el host migrado
      messageType: savedMessage.messageType,
      controlId: savedMessage.messageControlId
    };

    const responses = await messageService.sendMessage(request);
    
    // Update the message with responses and sent timestamp
    // También actualizar el host si fue migrado
    this.messages = this.messages.map(msg => 
      msg.id === savedMessage.id 
        ? { 
            ...msg, 
            host: migratedHost, // Actualizar al host migrado
            responses, 
            sentTimestamp: new Date() 
          }
        : msg
    );
    this.notifyListeners();
    
    return responses;
  }

  async sendAllMessages(): Promise<void> {
    if (this.messages.length === 0) {
      throw new Error('No hay mensajes guardados para enviar.');
    }

    for (const savedMessage of this.messages) {
      await this.sendMessage(savedMessage);
    }
  }
  updateMessageContent(messageId: string, newContent: string): void {
    this.messages = this.messages.map(msg => 
      msg.id === messageId 
        ? { ...msg, content: newContent }
        : msg
    );
    this.notifyListeners();
  }

  updateMessageComment(messageId: string, comment: string): void {
    this.messages = this.messages.map(msg => 
      msg.id === messageId 
        ? { ...msg, comment: comment.trim() || undefined }
        : msg
    );
    this.notifyListeners();
  }

  updateMessageControlId(messageId: string, controlId: string): void {
    this.messages = this.messages.map(msg => 
      msg.id === messageId 
        ? { ...msg, messageControlId: controlId.trim() || undefined }
        : msg
    );
    this.notifyListeners();
  }

  // Export/Import functionality
  exportMessages(): string {
    const exportData = {
      version: '1.0',
      exportDate: new Date().toISOString(),
      messages: this.messages.map(msg => ({
        ...msg,
        timestamp: msg.timestamp.toISOString(),
        sentTimestamp: msg.sentTimestamp?.toISOString()
      }))
    };
    return JSON.stringify(exportData, null, 2);
  }

  importMessages(jsonData: string): { success: boolean; message: string; importedCount?: number } {
    try {
      const importData = JSON.parse(jsonData);
      
      // Validate the import data structure
      if (!importData.messages || !Array.isArray(importData.messages)) {
        return { success: false, message: 'Formato de archivo inválido: no se encontraron mensajes' };
      }      // Convert imported messages to proper format
      const importedMessages: SavedMessage[] = importData.messages.map((msg: any) => ({
        ...msg,
        id: Date.now().toString() + Math.random().toString(36).substr(2, 9), // Generate new unique ID
        timestamp: new Date(msg.timestamp),
        sentTimestamp: msg.sentTimestamp ? new Date(msg.sentTimestamp) : undefined,
        responses: undefined // Clear responses for imported messages
      }));

      // Add imported messages to existing ones
      this.messages = [...this.messages, ...importedMessages];
      this.notifyListeners();

      return { 
        success: true, 
        message: `${importedMessages.length} mensajes importados exitosamente`,
        importedCount: importedMessages.length
      };
    } catch (error) {
      return { success: false, message: 'Error al procesar el archivo: formato JSON inválido' };
    }
  }

  clearAllMessages(): void {
    this.messages = [];
    this.notifyListeners();
  }

  // Migrate all saved messages to use current host names
  migrateHostsInSavedMessages(): void {
    let hasChanges = false;
    
    this.messages = this.messages.map(msg => {
      const migratedHost = hostService.migrateHost(msg.host);
      if (migratedHost !== msg.host) {
        console.log(`Migrando mensaje ${msg.id}: ${msg.host} → ${migratedHost}`);
        hasChanges = true;
        return { ...msg, host: migratedHost };
      }
      return msg;
    });
    
    if (hasChanges) {
      this.notifyListeners();
      console.log('Migración de hosts en mensajes guardados completada');
    }
  }

  // Initialize migrations on service start
  initializeMigrations(): void {
    // Wait a bit for hostService to be initialized
    setTimeout(() => {
      this.migrateHostsInSavedMessages();
    }, 1000);
  }
}

export const savedMessagesService = SavedMessagesService.getInstance();
