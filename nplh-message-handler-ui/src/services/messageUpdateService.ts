// Service to handle message response updates via SSE
export class MessageUpdateService {
  private static instance: MessageUpdateService;
  private updateCallbacks: ((controlId: string, responses: string[]) => void)[] = [];
  private eventSource: EventSource | null = null;

  private constructor() {
    this.setupSSEConnection();
  }

  static getInstance(): MessageUpdateService {
    if (!MessageUpdateService.instance) {
      MessageUpdateService.instance = new MessageUpdateService();
    }
    return MessageUpdateService.instance;
  }

  registerUpdateCallback(callback: (controlId: string, responses: string[]) => void) {
    this.updateCallbacks.push(callback);
    console.log('Registered update callback. Total callbacks:', this.updateCallbacks.length);
  }

  unregisterUpdateCallback(callback: (controlId: string, responses: string[]) => void) {
    this.updateCallbacks = this.updateCallbacks.filter(cb => cb !== callback);
    console.log('Unregistered update callback. Total callbacks:', this.updateCallbacks.length);
  }

  private notifyCallbacks(controlId: string, responses: string[]) {
    console.log(`Notifying ${this.updateCallbacks.length} callbacks for controlId: ${controlId}`, responses);
    this.updateCallbacks.forEach(callback => {
      try {
        callback(controlId, responses);
      } catch (error) {
        console.error('Error calling update callback:', error);
      }
    });
  }

  private setupSSEConnection() {
    try {
      this.eventSource = new EventSource('/api/ui/messages/sse');
      
      this.eventSource.onopen = () => {
        console.log('SSE connection established');
      };
      
      this.eventSource.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          console.log('Received SSE message:', data);
          
          if (data.type === 'messageUpdate' && data.controlId && data.responses) {
            this.notifyCallbacks(data.controlId, data.responses);
          }
        } catch (error) {
          console.error('Error parsing SSE message:', error);
        }
      };
      
      this.eventSource.onerror = (error) => {
        console.error('SSE connection error:', error);
        // Auto-reconnect after 5 seconds
        setTimeout(() => {
          if (this.eventSource?.readyState === EventSource.CLOSED) {
            console.log('Attempting to reconnect SSE...');
            this.setupSSEConnection();
          }
        }, 5000);
      };
      
    } catch (error) {
      console.error('Error setting up SSE connection:', error);
    }
  }

  disconnect() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      console.log('SSE connection closed');
    }
  }

  // Method to manually update message responses (can be called from console or other services)
  updateResponses(controlId: string, responses: string[]) {
    this.notifyCallbacks(controlId, responses);
  }
}

// Initialize the service when the module is loaded
export const messageUpdateService = MessageUpdateService.getInstance();
