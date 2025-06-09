
import { defineConfig } from 'vite'
import * as path from 'path';
import react from '@vitejs/plugin-react'
import { fileURLToPath, URL } from 'node:url'

const __dirname = path.dirname(fileURLToPath(import.meta.url));

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
              }              // Broadcast the update to the client via the global function
              // This will be handled by the messageUpdateService
              console.log(`Received message update for controlId: ${controlId}`, responses);
              
              // Store the update in a temporary queue for client polling
              server.messageUpdates = server.messageUpdates || [];
              server.messageUpdates.push({ 
                controlId, 
                responses, 
                timestamp: Date.now() 
              });
              
              // Keep only the last 50 updates to prevent memory leaks
              if (server.messageUpdates.length > 50) {
                server.messageUpdates = server.messageUpdates.slice(-50);
              }
              
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
            }          });
        } else {
          res.statusCode = 405;
          res.setHeader('Content-Type', 'application/json');
          res.end(JSON.stringify({ error: 'Method not allowed' }));
        }
      });

      // Add polling endpoint for client-side retrieval of updates
      server.middlewares.use('/api/ui/messages/poll-updates', (req: any, res: any) => {
        if (req.method === 'GET') {
          const updates = server.messageUpdates || [];
          const urlParams = new URL(req.url, 'http://localhost').searchParams;
          const since = parseInt(urlParams.get('since') || '0');
          
          // Filter updates that are newer than the 'since' timestamp
          const newUpdates = updates.filter((update: any) => update.timestamp > since);
          
          res.statusCode = 200;
          res.setHeader('Content-Type', 'application/json');
          res.setHeader('Access-Control-Allow-Origin', '*');
          res.end(JSON.stringify(newUpdates));
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
