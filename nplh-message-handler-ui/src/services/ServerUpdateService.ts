import { ServerMessage } from './ServerService';

// Service to handle real-time server message updates via HTTP polling
export class ServerUpdateService {
  private static instance: ServerUpdateService;
  private updateCallbacks: ((serverName: string, messages: ServerMessage[]) => void)[] = [];
  private pollingInterval: number | null = null;

  private constructor() {
    console.log('🚀 ServerUpdateService initialized');
    this.startPolling();
  }

  static getInstance(): ServerUpdateService {
    if (!ServerUpdateService.instance) {
      ServerUpdateService.instance = new ServerUpdateService();
    }
    return ServerUpdateService.instance;
  }

  registerUpdateCallback(callback: (serverName: string, messages: ServerMessage[]) => void) {
    console.log('📝 Registering server update callback');
    this.updateCallbacks.push(callback);
  }

  unregisterUpdateCallback(callback: (serverName: string, messages: ServerMessage[]) => void) {
    this.updateCallbacks = this.updateCallbacks.filter(cb => cb !== callback);
  }

  private notifyCallbacks(serverName: string, messages: ServerMessage[]) {
    console.log(`📢 Notifying callbacks for server: ${serverName} with ${messages.length} messages`);
    
    this.updateCallbacks.forEach((callback) => {
      try {
        callback(serverName, messages);
      } catch (error) {
        console.error(`❌ Error calling callback:`, error);
      }
    });
  }

  // Start polling for server updates
  startPolling(intervalMs: number = 2000) {
    if (this.pollingInterval) {
      console.log('⚠️ Polling already started');
      return;
    }

    console.log(`🔄 Starting server update polling (every ${intervalMs}ms)...`);
    
    this.pollingInterval = setInterval(async () => {
      try {
        const response = await fetch('/api/ui/servers/get-updates');
        
        if (response.ok) {
          const data = await response.json();
          const serverUpdates = data.serverUpdates || {};
          const serverNames = Object.keys(serverUpdates);
          
          if (serverNames.length > 0) {
            console.log('🔍 Polling found server updates for:', serverNames);
            
            serverNames.forEach(serverName => {
              const messages = serverUpdates[serverName];
              if (messages && Array.isArray(messages) && messages.length > 0) {
                console.log(`📨 Processing ${messages.length} messages for server '${serverName}'`);
                
                // Convert to ServerMessage[] if needed (backward compatibility)
                const serverMessages: ServerMessage[] = messages.map(msg => {
                  // Check if already in ServerMessage format
                  if (typeof msg === 'object' && msg !== null && 'message' in msg && 'responses' in msg) {
                    return msg as ServerMessage;
                  }
                  // Legacy format: convert string to ServerMessage
                  return {
                    message: '',
                    responses: [msg as string]
                  };
                });
                
                this.notifyCallbacks(serverName, serverMessages);
              }
            });
          }
        }
      } catch (e) {
        console.error('❌ Error polling for server updates:', e);
      }
    }, intervalMs);
  }

  stopPolling() {
    if (this.pollingInterval) {
      console.log('⏹️ Stopping server update polling');
      clearInterval(this.pollingInterval);
      this.pollingInterval = null;
    }
  }
}

// Initialize the service and export the instance
export const serverUpdateService = ServerUpdateService.getInstance();
