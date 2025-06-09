
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
              }

              // Broadcast the update to the client via the global function
              // This will be handled by the messageUpdateService
              console.log(`Received message update for controlId: ${controlId}`, responses);
              
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
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
