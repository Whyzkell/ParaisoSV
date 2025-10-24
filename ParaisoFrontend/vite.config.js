// vite.config.js
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:8081", // ← usa el puerto real de tu backend
        changeOrigin: true,
      },
      "/auth": {
        target: "http://localhost:8081", // para login/refresh
        changeOrigin: true,
      },
    },
  },
});
