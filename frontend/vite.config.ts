import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { resolve } from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: resolve(__dirname, '../backend/src/main/resources/static'),
    emptyOutDir: true
  },
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    },
    allowedHosts: true // TODO remove later?
  }
})
