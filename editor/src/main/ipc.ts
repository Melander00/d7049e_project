import type { SaveData } from "@shared/ipc";
import { ipcMain } from "electron";

export function initIpc() {
    ipcMain.on("save", (_event, data: SaveData) => {
        console.log("Saving",data.filepath)
    }) 
}