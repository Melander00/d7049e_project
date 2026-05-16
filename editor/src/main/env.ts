import { is } from "@electron-toolkit/utils";
import { app } from "electron";
import path from "path";

export const isProd = !is.dev;

export const configDataDir = path.join(app.getPath("userData"), "app-config")