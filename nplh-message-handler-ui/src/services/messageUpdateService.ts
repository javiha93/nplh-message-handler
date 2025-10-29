// Service to handle message response updates via HTTP polling and global events
import { ClientMessageResponse } from '../components/savedMessages/services/SavedMessagesService';

export class MessageUpdateService {
  private static instance: MessageUpdateService;
  private updateCallbacks: ((controlId: string, responses: ClientMessageResponse[]) => void)[] = [];

  private constructor() {
    this.setupPollingForUpdates();
    this.setupGlobalUpdateListener();
  }

  static getInstance(): MessageUpdateService {
    if (!MessageUpdateService.instance) {
      MessageUpdateService.instance = new MessageUpdateService();
    }
    return MessageUpdateService.instance;
  }
  registerUpdateCallback(callback: (controlId: string, responses: ClientMessageResponse[]) => void) {
    this.updateCallbacks.push(callback);
    console.log('✅ Registered update callback. Total callbacks:', this.updateCallbacks.length);
  }

  unregisterUpdateCallback(callback: (controlId: string, responses: ClientMessageResponse[]) => void) {
    this.updateCallbacks = this.updateCallbacks.filter(cb => cb !== callback);
    console.log('❌ Unregistered update callback. Total callbacks:', this.updateCallbacks.length);
  }

  private notifyCallbacks(controlId: string, responses: ClientMessageResponse[]) {
    console.log(`📢 Notifying ${this.updateCallbacks.length} callbacks for controlId: ${controlId}`, responses);
    
    if (this.updateCallbacks.length === 0) {
      console.warn('⚠️  No callbacks registered to receive updates!');
      return;
    }
    
    this.updateCallbacks.forEach((callback, index) => {
      try {
        console.log(`📨 Calling callback ${index + 1}/${this.updateCallbacks.length} for controlId: ${controlId}`);
        callback(controlId, responses);
        console.log(`✅ Callback ${index + 1} executed successfully`);
      } catch (error) {
        console.error(`❌ Error calling callback ${index + 1}:`, error);
      }
    });
  }
  // Method to manually update message responses (can be called from console or other services)
  updateResponses(controlId: string, responses: string[] | ClientMessageResponse[]) {
    console.log('🔄 updateResponses called for controlId:', controlId, 'with', responses.length, 'responses');
    
    // Convert string responses to ClientMessageResponse format if needed
    const clientResponses: ClientMessageResponse[] = responses.map((resp: any) => {
      if (typeof resp === 'string') {
        return { message: resp, receiveTime: new Date().toISOString() };
      }
      return resp;
    });
    
    console.log('📤 Notifying callbacks for controlId:', controlId, 'with converted responses:', clientResponses);
    this.notifyCallbacks(controlId, clientResponses);
  }

  // Setup polling to check for global updates (fallback mechanism)
  private setupPollingForUpdates() {
    console.log('🔄 Setting up polling for message updates...');
    
    setInterval(() => {
      try {
        // Check globalThis.messageUpdates
        if (typeof globalThis !== 'undefined' && (globalThis as any).messageUpdates) {
          const updates = (globalThis as any).messageUpdates;
          const controlIds = Object.keys(updates);
          
          if (controlIds.length > 0) {
            console.log('🔍 Polling found updates for controlIds:', controlIds);
            
            controlIds.forEach(controlId => {
              const responses = updates[controlId];
              if (responses && Array.isArray(responses)) {
                console.log('📨 Processing polled update for controlId:', controlId, responses);
                this.updateResponses(controlId, responses);
                // Clear the update after processing
                delete updates[controlId];
              }
            });
          }
        }
        
        // Also check window.messageUpdates if available
        if (typeof window !== 'undefined' && (window as any).messageUpdates) {
          const updates = (window as any).messageUpdates;
          const controlIds = Object.keys(updates);
          
          if (controlIds.length > 0) {
            console.log('🔍 Polling found window updates for controlIds:', controlIds);
            
            controlIds.forEach(controlId => {
              const responses = updates[controlId];
              if (responses && Array.isArray(responses)) {
                console.log('📨 Processing window update for controlId:', controlId, responses);
                this.updateResponses(controlId, responses);
                // Clear the update after processing
                delete updates[controlId];
              }
            });
          }
        }
      } catch (e) {
        console.error('❌ Error in polling for updates:', e);
      }
    }, 500); // Check every 500ms for faster updates
  }

  // Setup listener for global updates (direct mechanism)
  private setupGlobalUpdateListener() {
    console.log('🌍 Setting up global update listeners...');
    
    // Expose a global function that can be called directly
    (window as any).updateMessageResponses = (controlId: string, responses: any[]) => {
      console.log('📞 Global update function called for controlId:', controlId, responses);
      this.updateResponses(controlId, responses);
    };

    // Listen for custom events
    window.addEventListener('messageUpdate', (event: any) => {
      const { controlId, responses } = event.detail;
      if (controlId && responses) {
        console.log('🎯 Received messageUpdate event for controlId:', controlId, responses);
        this.updateResponses(controlId, responses);
      }
    });
    
    console.log('✅ Global update listeners set up successfully');
  }
}

// Initialize the service when the module is loaded
export const messageUpdateService = MessageUpdateService.getInstance();
