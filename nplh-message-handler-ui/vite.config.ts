
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Custom plugin to handle message update endpoint
const messageUpdatePlugin = () => {
  // Store message updates in memory with delivery tracking
  const messageUpdates: { [controlId: string]: { responses: any[], delivered: boolean, timestamp: number } } = {};
  
  // Clean up delivered messages after 5 seconds
  setInterval(() => {
    const now = Date.now();
    Object.keys(messageUpdates).forEach(key => {
      const update = messageUpdates[key];
      if (update.delivered && (now - update.timestamp) > 5000) {
        delete messageUpdates[key];
      }
    });
  }, 1000);
  
  return {
    name: 'message-update',
    configureServer(server: any) {
      server.middlewares.use((req: any, res: any, next: any) => {
        // Handle GET requests for updates
        if (req.url === '/api/ui/messages/get-updates' && req.method === 'GET') {
          const undeliveredUpdates: { [controlId: string]: any[] } = {};
          
          Object.keys(messageUpdates).forEach(key => {
            if (!messageUpdates[key].delivered) {
              undeliveredUpdates[key] = messageUpdates[key].responses;
              messageUpdates[key].delivered = true;
            }
          });
          
          if (Object.keys(undeliveredUpdates).length > 0) {
            console.log('� Delivering updates for:', Object.keys(undeliveredUpdates));
          }
          
          res.statusCode = 200;
          res.setHeader('Content-Type', 'application/json');
          res.end(JSON.stringify({ updates: undeliveredUpdates }));
          return;
        }
        
        // Handle POST requests for update-responses
        if (req.url === '/api/ui/messages/update-responses' && req.method === 'POST') {
          console.log('📥 POST: Receiving message update...');
          let body = '';
          req.on('data', (chunk: any) => {
            body += chunk.toString();
          });
          req.on('end', () => {
            try {
              const { controlId, responses } = JSON.parse(body);
              
              if (!controlId || !responses || !Array.isArray(responses)) {
                console.error('❌ Invalid request body');
                res.statusCode = 400;
                res.setHeader('Content-Type', 'application/json');
                res.end(JSON.stringify({ error: 'Invalid request body. Expected controlId and responses array.' }));
                return;
              }
              
              // Validate that responses are in ClientMessageResponse format
              const validResponses = responses.every(response => 
                response && 
                typeof response === 'object' && 
                typeof response.message === 'string' && 
                typeof response.receiveTime === 'string'
              );

              let finalResponses = responses;
              if (!validResponses) {
                // Convert legacy string responses to ClientMessageResponse format if needed
                finalResponses = responses.map(response => {
                  if (typeof response === 'string') {
                    return {
                      message: response,
                      receiveTime: new Date().toISOString()
                    };
                  }
                  return response;
                });
              }

              console.log(`✅ Message update stored for controlId: ${controlId}`);
              
              // Store the update in memory with metadata
              messageUpdates[controlId] = {
                responses: finalResponses,
                delivered: false,
                timestamp: Date.now()
              };
              
              res.statusCode = 200;
              res.setHeader('Content-Type', 'application/json');
              res.end(JSON.stringify({ 
                success: true, 
                message: 'Message responses updated successfully',
                controlId,
                responsesCount: responses.length,
                format: validResponses ? 'ClientMessageResponse' : 'converted',
                receivedResponses: finalResponses
              }));
            } catch (error) {
              console.error('Error processing message update:', error);
              res.statusCode = 400;
              res.setHeader('Content-Type', 'application/json');
              res.end(JSON.stringify({ error: 'Invalid JSON body' }));
            }
          });
          return;
        }
        
        // Pass to next middleware if not our endpoint
        next();
      });
    }
  }
}

export default defineConfig({
  plugins: [react(), messageUpdatePlugin()],
  server: {
    host: '0.0.0.0', // Listen on all interfaces (IPv4 and IPv6)
    port: 8084,
    proxy: {
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        secure: false
      }
    }
  },
  preview: {
    port: 8084,
    proxy: {
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
