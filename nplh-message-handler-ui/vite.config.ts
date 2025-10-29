
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Custom plugin to handle message update endpoint
const messageUpdatePlugin = () => {
  return {
    name: 'message-update',
    configureServer(server: any) {
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
              
              // Store the update so it can be retrieved by the frontend
              try {
                // Store in a simple object instead of Map to avoid TypeScript issues
                if (typeof globalThis !== 'undefined') {
                  if (!(globalThis as any).messageUpdates) {
                    (globalThis as any).messageUpdates = {};
                  }
                  (globalThis as any).messageUpdates[controlId] = finalResponses;
                  console.log('✅ Message update stored globally for controlId:', controlId);
                  console.log('📦 Current globalThis.messageUpdates:', (globalThis as any).messageUpdates);
                }
              } catch (e) {
                console.warn('❌ Could not store message update globally:', e);
              }
              
              // Also dispatch a global event for immediate processing
              try {
                if (typeof global !== 'undefined' && global.process) {
                  // Node.js environment - dispatch to global
                  process.nextTick(() => {
                    if ((global as any).updateMessageResponses) {
                      console.log('📢 Calling global.updateMessageResponses for controlId:', controlId);
                      (global as any).updateMessageResponses(controlId, finalResponses);
                    }
                  });
                }
              } catch (e) {
                console.warn('Could not dispatch global event:', e);
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
