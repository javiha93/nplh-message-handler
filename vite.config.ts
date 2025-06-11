
import { defineConfig } from 'vite'
import * as path from 'path';
import react from '@vitejs/plugin-react'
import { fileURLToPath, URL } from 'node:url'

const __dirname = path.dirname(fileURLToPath(import.meta.url));

// Custom plugin to handle message update endpoint
const messageUpdatePlugin = () => {
  let sseClients: any[] = [];
  
  return {
    name: 'message-update',
    configureServer(server: any) {
      // SSE endpoint for clients to connect and receive updates
      server.middlewares.use('/api/ui/messages/sse', (req: any, res: any) => {
        if (req.method === 'GET') {
          res.writeHead(200, {
            'Content-Type': 'text/event-stream',
            'Cache-Control': 'no-cache',
            'Connection': 'keep-alive',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Headers': 'Cache-Control'
          });

          // Add client to list
          sseClients.push(res);
          console.log(`SSE client connected. Total clients: ${sseClients.length}`);

          // Send initial connection message
          res.write('data: {"type":"connected"}\n\n');

          // Remove client when connection closes
          req.on('close', () => {
            sseClients = sseClients.filter(client => client !== res);
            console.log(`SSE client disconnected. Total clients: ${sseClients.length}`);
          });
        }
      });

      // Message update endpoint that broadcasts to SSE clients
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
              }

              console.log(`Received message update for controlId: ${controlId}`, responses);
              
              // Broadcast to all connected SSE clients
              const eventData = JSON.stringify({
                type: 'messageUpdate',
                controlId,
                responses
              });
              
              sseClients.forEach(client => {
                try {
                  client.write(`data: ${eventData}\n\n`);
                } catch (error) {
                  console.error('Error sending SSE message:', error);
                }
              });
              
              console.log(`Broadcasted update to ${sseClients.length} SSE clients`);
              
              res.statusCode = 200;
              res.setHeader('Content-Type', 'application/json');
              res.end(JSON.stringify({ 
                success: true, 
                message: 'Message responses updated successfully',
                controlId,
                responsesCount: responses.length
              }));
            } catch (error) {
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
  resolve: {
      alias: {
        '@ui': path.resolve(__dirname, './nplh-message-handler-ui/src'),
      },
    },  server: {
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
