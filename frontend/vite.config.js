import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
    base: '/',  // ← добавь эту строку (явно указываем корень)
    server: {
        host: true,
        port: 3000,
        watch: {
            usePolling: true
        },
        proxy: {
            '/api': {
                target: 'http://backend:8080',
                changeOrigin: true,
                secure: false
            },
            '/auth': {
                target: 'http://backend:8080',
                changeOrigin: true,
                secure: false
            }
        }
    }
});