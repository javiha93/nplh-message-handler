
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Custom plugin to handle message update endpoint
const messageUpdatePlugin = () => {
  return {
    name: 'message-update',
    configureServer(server: any) {
      server.middlewares.use('/api/ui/messages/update-responses', (req: any, res: any) => {
        if (req.method === 'POST') {
          let body = '';
          req.on('data', (chunk: any) => {
            body += chunk.toString();
          });
          req.on('end', () => {
            try {
              const { controlId, responses } = JSON.parse(body);
              
              if (!controlId || !responses || !Array.isArray(responses)) {
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

              // Broadcast the update to the client via SSE-like mechanism
              // In a real application, this would typically be handled by a proper message broker
              console.log(`Received message update for controlId: ${controlId}`, finalResponses);
              
              // For now, we'll just store this and let the UI poll for updates
              // In a production environment, you'd want to use WebSockets or Server-Sent Events
              // to push these updates to connected clients immediately
              
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
