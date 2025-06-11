import { messageService, SendMessageRequest } from '../../../services/MessageService';

export interface SavedMessage {
  id: string;
  content: string;
  host: string;
  messageType: string;
  messageControlId?: string;
  timestamp: Date;
  responses?: string[];
}

export class SavedMessagesService {
  private static instance: SavedMessagesService;
  private messages: SavedMessage[] = [];
  private listeners: ((messages: SavedMessage[]) => void)[] = [];

  static getInstance(): SavedMessagesService {
    if (!SavedMessagesService.instance) {
      SavedMessagesService.instance = new SavedMessagesService();
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

  updateMessageResponses(controlId: string, responses: string[]): void {
    this.messages = this.messages.map(msg => 
      msg.messageControlId === controlId 
        ? { ...msg, responses: responses }
        : msg
    );
    this.notifyListeners();
  }

  clearAllResponses(): void {
    this.messages = this.messages.map(msg => ({
      ...msg,
      responses: undefined
    }));
    this.notifyListeners();
  }

  clearMessageResponses(messageId: string): void {
    this.messages = this.messages.map(msg => 
      msg.id === messageId 
        ? { ...msg, responses: undefined }
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
  }

  async sendMessage(savedMessage: SavedMessage): Promise<string[]> {
    const request: SendMessageRequest = {
      message: savedMessage.content,
      hostName: savedMessage.host,
      messageType: savedMessage.messageType,
      controlId: savedMessage.messageControlId
    };

    const responses = await messageService.sendMessage(request);
    
    // Update the message with responses
    this.messages = this.messages.map(msg => 
      msg.id === savedMessage.id 
        ? { ...msg, responses }
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
}

export const savedMessagesService = SavedMessagesService.getInstance();
