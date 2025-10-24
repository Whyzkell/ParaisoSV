import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";


export default defineConfig({
plugins: [react(), tailwindcss()],
server: {
port: 5173,
// proxy opcional
// proxy: {
// "/api": { target: "http://localhost:8081", changeOrigin: true },
// "/auth": { target: "http://localhost:8081", changeOrigin: true },
// "/files": { target: "http://localhost:8081", changeOrigin: true },
// },
},
});