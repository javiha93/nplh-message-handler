
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
      // Message update endpoint that stores updates in global object
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
              
              // Store updates in global object for client to poll
              if (typeof globalThis !== 'undefined') {
                if (!globalThis.messageUpdates) {
                  globalThis.messageUpdates = {};
                }
                globalThis.messageUpdates[controlId] = responses;
              }
              
              // Also dispatch global event for immediate updates
              if (typeof window !== 'undefined') {
                window.dispatchEvent(new CustomEvent('messageUpdate', {
                  detail: { controlId, responses }
                }));
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
