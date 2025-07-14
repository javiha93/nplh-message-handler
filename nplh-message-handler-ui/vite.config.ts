
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Custom plugin to handle message update endpoint
const messageUpdatePlugin = () => {
  // Store message updates in the server context
  let messageUpdates: Record<string, any[]> = {};
  
  return {
    name: 'message-update',
    configureServer(server: any) {
      // Endpoint to receive message updates from backend
      server.middlewares.use('/api/ui/messages/update-responses', (req: any, res: any) => {
        console.log('🌐 Endpoint called:', req.method, req.url);
        
        if (req.method === 'POST') {
          console.log('📥 Processing POST request...');
          let body = '';
          req.on('data', (chunk: any) => {
            body += chunk.toString();
            console.log('📦 Received chunk, total length:', body.length);
          });
          req.on('end', () => {
            console.log('🏁 Request complete, body:', body);
            try {
              const { controlId, responses } = JSON.parse(body);
              console.log('📋 Parsed data - controlId:', controlId, 'responses:', responses);
              
              if (!controlId || !responses || !Array.isArray(responses)) {
                console.error('❌ Invalid request body:', { controlId, responses });
                res.statusCode = 400;
                res.setHeader('Content-Type', 'application/json');
                res.end(JSON.stringify({ error: 'Invalid request body. Expected controlId and responses array.' }));
                return;
              }              // Validate that responses are in ClientMessageResponse format
              const validResponses = responses.every(response => 
                response && 
                typeof response === 'object' && 
                typeof response.message === 'string' && 
                typeof response.receiveTime === 'string'
              );

              let finalResponses = responses;
              if (!validResponses) {
                console.warn('Received responses in unexpected format, attempting to convert...', responses);
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

              console.log(`🔥 RECEIVED MESSAGE UPDATE for controlId: ${controlId}`, finalResponses);
              
              // Store the update in server context
              messageUpdates[controlId] = finalResponses;
              console.log('✅ Message update stored in server context for controlId:', controlId);
              console.log('📦 Current messageUpdates:', Object.keys(messageUpdates));
              
              // Also try to call the global function directly if available
              try {
                // Check if there's a global function available
                if (typeof globalThis !== 'undefined' && (globalThis as any).updateMessageResponses) {
                  console.log('📞 Calling globalThis.updateMessageResponses directly');
                  (globalThis as any).updateMessageResponses(controlId, finalResponses);
                } else {
                  console.log('ℹ️  No globalThis.updateMessageResponses available');
                }
              } catch (e) {
                console.warn('Could not call global function:', e);
              }
              
              // Try to broadcast via eval (last resort for dev server)
              try {
                // This is a hack for development - in production you'd use proper messaging
                const script = `
                  if (typeof window !== 'undefined' && window.updateMessageResponses) {
                    console.log('� Calling window.updateMessageResponses via eval');
                    window.updateMessageResponses('${controlId}', ${JSON.stringify(finalResponses)});
                  }
                `;
                // Note: This won't work in dev server context, but it's here for completeness
                console.log('📝 Would execute script (if in browser context):', script);
              } catch (e) {
                console.warn('Could not execute browser script:', e);
              }
              
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
        } else {
          res.statusCode = 405;
          res.setHeader('Content-Type', 'application/json');
          res.end(JSON.stringify({ error: 'Method not allowed' }));
        }
      });

      // Endpoint for frontend to poll for message updates
      server.middlewares.use('/api/ui/messages/poll-updates', (req: any, res: any) => {
        if (req.method === 'GET') {
          console.log('🔄 Frontend polling for message updates...');
          
          // Return all available updates and clear them
          const updates = { ...messageUpdates };
          messageUpdates = {}; // Clear after returning
          
          if (Object.keys(updates).length > 0) {
            console.log('📤 Returning updates to frontend:', Object.keys(updates));
          }
          
          res.statusCode = 200;
          res.setHeader('Content-Type', 'application/json');
          res.end(JSON.stringify(updates));
        } else {
          res.statusCode = 405;
          res.setHeader('Content-Type', 'application/json');
          res.end(JSON.stringify({ error: 'Method not allowed' }));
        }
      });
    }
  }
}

export default defineConfig({
  plugins: [react(), messageUpdatePlugin()],
  server: {
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
