// Service to handle message response updates via HTTP polling and global events
import { ClientMessageResponse } from '../components/savedMessages/services/SavedMessagesService';

export class MessageUpdateService {
  private static instance: MessageUpdateService;
  private updateCallbacks: ((controlId: string, responses: ClientMessageResponse[]) => void)[] = [];

  private constructor() {
    console.log('🚀 MessageUpdateService initialized');
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
  }

  unregisterUpdateCallback(callback: (controlId: string, responses: ClientMessageResponse[]) => void) {
    this.updateCallbacks = this.updateCallbacks.filter(cb => cb !== callback);
  }

  private notifyCallbacks(controlId: string, responses: ClientMessageResponse[]) {
    console.log(`📢 Notifying callbacks for controlId: ${controlId}`);
    
    this.updateCallbacks.forEach((callback) => {
      try {
        callback(controlId, responses);
      } catch (error) {
        console.error(`❌ Error calling callback:`, error);
      }
    });
  }
  // Method to manually update message responses (can be called from console or other services)
  updateResponses(controlId: string, responses: string[] | ClientMessageResponse[]) {
    // Convert string responses to ClientMessageResponse format if needed
    const clientResponses: ClientMessageResponse[] = responses.map((resp: any) => {
      if (typeof resp === 'string') {
        return { message: resp, receiveTime: new Date().toISOString() };
      }
      return resp;
    });
    
    this.notifyCallbacks(controlId, clientResponses);
  }

  // Setup polling to check for global updates (fallback mechanism)
  private setupPollingForUpdates() {
    console.log('🔄 Setting up polling for message updates...');
    console.log('🚀 MessageUpdateService initialized and polling started');
    
    setInterval(async () => {
      try {
        // Poll the GET endpoint for updates
        const response = await fetch('/api/ui/messages/get-updates');
        
        if (response.ok) {
          const data = await response.json();
          const updates = data.updates || {};
          const controlIds = Object.keys(updates);
          
          if (controlIds.length > 0) {
            console.log('🔍 Polling found updates for controlIds:', controlIds);
            
            controlIds.forEach(controlId => {
              const responses = updates[controlId];
              if (responses && Array.isArray(responses)) {
                console.log('📨 Processing update for controlId:', controlId);
                this.updateResponses(controlId, responses);
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
    // Expose a global function that can be called directly
    (window as any).updateMessageResponses = (controlId: string, responses: any[]) => {
      console.log('📞 Global update function called for controlId:', controlId);
      this.updateResponses(controlId, responses);
    };

    // Listen for custom events
    window.addEventListener('messageUpdate', (event: any) => {
      const { controlId, responses } = event.detail;
      if (controlId && responses) {
        console.log('🎯 Received messageUpdate event for controlId:', controlId);
        this.updateResponses(controlId, responses);
      }
    });
  }
}

// Initialize the service when the module is loaded
export const messageUpdateService = MessageUpdateService.getInstance();
