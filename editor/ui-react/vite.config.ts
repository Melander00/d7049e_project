import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';

//@ts-ignore
import path from "path";

//@ts-ignore
const src = path.resolve(__dirname, "src");

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "../www",
    
  },
  base: "./",
  resolve: {
    alias: {
        "@assets": path.resolve(src, "assets")
    }
  }
})
