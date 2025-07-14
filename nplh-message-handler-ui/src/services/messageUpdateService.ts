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
  }

  unregisterUpdateCallback(callback: (controlId: string, responses: ClientMessageResponse[]) => void) {
    this.updateCallbacks = this.updateCallbacks.filter(cb => cb !== callback);
  }

  private notifyCallbacks(controlId: string, responses: ClientMessageResponse[]) {
    if (this.updateCallbacks.length === 0) {
      console.warn('⚠️  No callbacks registered to receive updates!');
      return;
    }
    
    console.log(`� Message update received for controlId: ${controlId} (${responses.length} responses)`);
    
    this.updateCallbacks.forEach((callback, index) => {
      try {
        callback(controlId, responses);
      } catch (error) {
        console.error(`❌ Error calling callback ${index + 1}:`, error);
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
    let pollCount = 0;
    setInterval(async () => {
      try {
        pollCount++;
        
        // Poll the server for updates
        const response = await fetch('/api/ui/messages/poll-updates', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          if (pollCount % 60 === 0) { // Log errors every 60th poll (every 30 seconds)
            console.warn('❌ Failed to poll for updates:', response.status, response.statusText);
          }
          return;
        }

        const updates = await response.json();
        
        // Process each update
        const controlIds = Object.keys(updates);
        if (controlIds.length > 0) {
          controlIds.forEach(controlId => {
            const responses = updates[controlId];
            if (responses && Array.isArray(responses)) {
              this.updateResponses(controlId, responses);
            }
          });
        }
      } catch (e) {
        if (pollCount % 60 === 0) { // Log errors every 60th poll (every 30 seconds)
          console.error('❌ Error in HTTP polling for updates:', e);
        }
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
    
    // Also expose to globalThis for server-side access
    (globalThis as any).updateMessageResponses = (controlId: string, responses: any[]) => {
      console.log('📞 GlobalThis update function called for controlId:', controlId);
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
