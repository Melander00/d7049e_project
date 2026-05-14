import path from "node:path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export const isProd = process.env["NODE_ENV"] === "production";

export const srcDir = path.resolve(__dirname, "src");