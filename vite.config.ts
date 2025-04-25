
import { defineConfig } from 'vite'
import * as path from 'path';
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  resolve: {
      alias: {
        '@ui': path.resolve(__dirname, './nplh-message-handler-ui/src'),
      },
    },
  server: {
    port: 8083,
    proxy: {
      '/api': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
