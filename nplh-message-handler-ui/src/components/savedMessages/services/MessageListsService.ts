import { messageService, SendMessageRequest } from '../../../services/MessageService';

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
  comment?: string;
  responses?: ClientMessageResponse[];
}

export interface MessageList {
  id: string;
  name: string;
  description?: string;
  color?: string;
  createdAt: Date;
  updatedAt: Date;
  messages: SavedMessage[];
}

export class MessageListsService {
  private static instance: MessageListsService;
  private lists: MessageList[] = [];
  private activeListId: string | null = null;
  private listeners: ((lists: MessageList[], activeListId: string | null) => void)[] = [];
  private readonly STORAGE_KEY = 'messageLists';

  static getInstance(): MessageListsService {
    if (!MessageListsService.instance) {
      MessageListsService.instance = new MessageListsService();
      MessageListsService.instance.loadFromStorage();
    }
    return MessageListsService.instance;
  }

  constructor() {
    // Create default list if none exists
    if (this.lists.length === 0) {
      this.createList('Default', 'Lista principal de mensajes', '#3B82F6');
    }
  }

  subscribe(listener: (lists: MessageList[], activeListId: string | null) => void): () => void {
    this.listeners.push(listener);
    listener(this.lists, this.activeListId); // Initial call
    
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }

  private notifyListeners(): void {
    this.saveToStorage();
    this.listeners.forEach(listener => listener(this.lists, this.activeListId));
  }

  private saveToStorage(): void {
    try {
      const data = {
        lists: this.lists.map(list => ({
          ...list,
          createdAt: list.createdAt.toISOString(),
          updatedAt: list.updatedAt.toISOString(),
          messages: list.messages.map(msg => ({
            ...msg,
            timestamp: msg.timestamp.toISOString(),
            sentTimestamp: msg.sentTimestamp?.toISOString()
          }))
        })),
        activeListId: this.activeListId
      };
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(data));
    } catch (error) {
      console.error('Error saving message lists to localStorage:', error);
    }
  }

  private loadFromStorage(): void {
    try {
      const data = localStorage.getItem(this.STORAGE_KEY);
      if (data) {
        const parsed = JSON.parse(data);
        this.lists = parsed.lists.map((list: any) => ({
          ...list,
          createdAt: new Date(list.createdAt),
          updatedAt: new Date(list.updatedAt),
          messages: list.messages.map((msg: any) => ({
            ...msg,
            timestamp: new Date(msg.timestamp),
            sentTimestamp: msg.sentTimestamp ? new Date(msg.sentTimestamp) : undefined
          }))
        }));
        this.activeListId = parsed.activeListId;
        
        // Ensure we have at least one list
        if (this.lists.length === 0) {
          this.createList('Default', 'Lista principal de mensajes', '#3B82F6');
        }
        
        // Ensure active list exists
        if (!this.activeListId || !this.lists.find(l => l.id === this.activeListId)) {
          this.activeListId = this.lists[0].id;
        }
      }
    } catch (error) {
      console.error('Error loading message lists from localStorage:', error);
      // Create default list on error
      this.createList('Default', 'Lista principal de mensajes', '#3B82F6');
    }
  }

  // List management
  createList(name: string, description?: string, color?: string): MessageList {
    const newList: MessageList = {
      id: Date.now().toString() + Math.random().toString(36).substr(2, 9),
      name: name.trim(),
      description: description?.trim(),
      color: color || '#3B82F6',
      createdAt: new Date(),
      updatedAt: new Date(),
      messages: []
    };

    this.lists.push(newList);
    
    // Set as active if it's the first list
    if (!this.activeListId) {
      this.activeListId = newList.id;
    }
    
    this.notifyListeners();
    return newList;
  }

  updateList(listId: string, updates: Partial<Pick<MessageList, 'name' | 'description' | 'color'>>): void {
    this.lists = this.lists.map(list => 
      list.id === listId 
        ? { ...list, ...updates, updatedAt: new Date() }
        : list
    );
    this.notifyListeners();
  }

  deleteList(listId: string): void {
    if (this.lists.length <= 1) {
      throw new Error('No se puede eliminar la única lista existente');
    }

    this.lists = this.lists.filter(list => list.id !== listId);
    
    // Update active list if deleted
    if (this.activeListId === listId) {
      this.activeListId = this.lists[0].id;
    }
    
    this.notifyListeners();
  }

  duplicateList(listId: string, newName?: string): MessageList {
    const sourceList = this.lists.find(list => list.id === listId);
    if (!sourceList) {
      throw new Error('Lista no encontrada');
    }

    const duplicatedList: MessageList = {
      id: Date.now().toString() + Math.random().toString(36).substr(2, 9),
      name: newName || `${sourceList.name} (Copia)`,
      description: sourceList.description,
      color: sourceList.color,
      createdAt: new Date(),
      updatedAt: new Date(),
      messages: sourceList.messages.map(msg => ({
        ...msg,
        id: Date.now().toString() + Math.random().toString(36).substr(2, 9),
        responses: undefined, // Clear responses for duplicated messages
        sentTimestamp: undefined
      }))
    };

    this.lists.push(duplicatedList);
    this.notifyListeners();
    return duplicatedList;
  }

  // List access
  getLists(): MessageList[] {
    return [...this.lists];
  }

  getActiveList(): MessageList | null {
    return this.lists.find(list => list.id === this.activeListId) || null;
  }

  setActiveList(listId: string): void {
    if (this.lists.find(list => list.id === listId)) {
      this.activeListId = listId;
      this.notifyListeners();
    }
  }

  // Message management (operates on active list)
  addMessage(
    content: string,
    host: string,
    messageType: string,
    messageControlId?: string,
    listId?: string
  ): SavedMessage {
    const targetListId = listId || this.activeListId;
    if (!targetListId) {
      throw new Error('No hay lista activa');
    }

    const newMessage: SavedMessage = {
      id: Date.now().toString() + Math.random().toString(36).substr(2, 9),
      content,
      host,
      messageType,
      messageControlId,
      timestamp: new Date()
    };

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? { 
            ...list, 
            messages: [...list.messages, newMessage],
            updatedAt: new Date()
          }
        : list
    );

    this.notifyListeners();
    return newMessage;
  }

  removeMessage(messageId: string, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? { 
            ...list, 
            messages: list.messages.filter(msg => msg.id !== messageId),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  moveMessage(messageId: string, fromListId: string, toListId: string): void {
    const message = this.lists.find(list => list.id === fromListId)?.messages.find(msg => msg.id === messageId);
    if (!message) return;

    // Remove from source list
    this.lists = this.lists.map(list => 
      list.id === fromListId 
        ? { 
            ...list, 
            messages: list.messages.filter(msg => msg.id !== messageId),
            updatedAt: new Date()
          }
        : list
    );

    // Add to target list
    this.lists = this.lists.map(list => 
      list.id === toListId 
        ? { 
            ...list, 
            messages: [...list.messages, { 
              ...message, 
              responses: undefined, // Clear responses when moving
              sentTimestamp: undefined 
            }],
            updatedAt: new Date()
          }
        : list
    );

    this.notifyListeners();
  }

  // Additional message operations...
  updateMessageResponses(controlId: string, responses: ClientMessageResponse[], listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => 
              msg.messageControlId === controlId 
                ? { ...msg, responses }
                : msg
            ),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }
  clearAllResponses(listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => ({
              ...msg,
              responses: undefined,
              sentTimestamp: undefined
            })),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  clearMessageResponses(messageId: string, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => 
              msg.id === messageId 
                ? { ...msg, responses: undefined, sentTimestamp: undefined }
                : msg
            ),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  reorderMessages(startIndex: number, endIndex: number, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => {
      if (list.id === targetListId) {
        const result = Array.from(list.messages);
        const [removed] = result.splice(startIndex, 1);
        result.splice(endIndex, 0, removed);
        return { ...list, messages: result, updatedAt: new Date() };
      }
      return list;
    });
    this.notifyListeners();
  }

  reorderLists(startIndex: number, endIndex: number): void {
    const result = Array.from(this.lists);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    
    this.lists = result;
    this.notifyListeners();
  }

  // Send operations
  async sendMessage(savedMessage: SavedMessage, listId?: string): Promise<ClientMessageResponse[]> {
    const request: SendMessageRequest = {
      message: savedMessage.content,
      hostName: savedMessage.host,
      messageType: savedMessage.messageType,
      controlId: savedMessage.messageControlId
    };

    const responses = await messageService.sendMessage(request);
    
    const targetListId = listId || this.activeListId;
    if (targetListId) {
      this.lists = this.lists.map(list => 
        list.id === targetListId 
          ? {
              ...list,
              messages: list.messages.map(msg => 
                msg.id === savedMessage.id 
                  ? { ...msg, responses, sentTimestamp: new Date() }
                  : msg
              ),
              updatedAt: new Date()
            }
          : list
      );
      this.notifyListeners();
    }
      return responses;
  }

  // Message editing operations
  updateMessageContent(messageId: string, newContent: string, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => 
              msg.id === messageId 
                ? { ...msg, content: newContent }
                : msg
            ),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  updateMessageComment(messageId: string, comment: string, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => 
              msg.id === messageId 
                ? { ...msg, comment: comment.trim() || undefined }
                : msg
            ),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  updateMessageControlId(messageId: string, controlId: string, listId?: string): void {
    const targetListId = listId || this.activeListId;
    if (!targetListId) return;

    this.lists = this.lists.map(list => 
      list.id === targetListId 
        ? {
            ...list,
            messages: list.messages.map(msg => 
              msg.id === messageId 
                ? { ...msg, messageControlId: controlId.trim() || undefined }
                : msg
            ),
            updatedAt: new Date()
          }
        : list
    );
    this.notifyListeners();
  }

  // Export/Import
  exportList(listId: string): string {
    const list = this.lists.find(l => l.id === listId);
    if (!list) {
      throw new Error('Lista no encontrada');
    }

    const exportData = {
      version: '1.0',
      exportDate: new Date().toISOString(),
      listInfo: {
        name: list.name,
        description: list.description,
        color: list.color
      },
      messages: list.messages.map(msg => ({
        ...msg,
        timestamp: msg.timestamp.toISOString(),
        sentTimestamp: msg.sentTimestamp?.toISOString()
      }))
    };
    return JSON.stringify(exportData, null, 2);
  }

  exportAllLists(): string {
    const exportData = {
      version: '1.0',
      exportDate: new Date().toISOString(),
      lists: this.lists.map(list => ({
        ...list,
        createdAt: list.createdAt.toISOString(),
        updatedAt: list.updatedAt.toISOString(),
        messages: list.messages.map(msg => ({
          ...msg,
          timestamp: msg.timestamp.toISOString(),
          sentTimestamp: msg.sentTimestamp?.toISOString()
        }))
      }))
    };
    return JSON.stringify(exportData, null, 2);
  }
}

export const messageListsService = MessageListsService.getInstance();
