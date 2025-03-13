import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import gltf from "vite-plugin-gltf";

export default defineConfig({
  plugins: [react(), gltf()],
  define: {
    "process.env.VITE_API_URL": JSON.stringify("YOUR_BACKEND_URL/api"),
    "process.env.VITE_WS_URL": JSON.stringify("YOUR_BACKEND_URL/ws")
  },
  server: {
    proxy: {
      "/api": {
        target: "YOUR_BACKEND_URL", // HTTPS target
        changeOrigin: true,
        secure: true, // HTTPS 인증서 검증 활성화
        ws: true,
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  },
  assetsInclude: ['**/*.glb']
});
