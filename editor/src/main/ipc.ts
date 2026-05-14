import { Channels } from "@shared/channels";
import type { SaveData } from "@shared/ipc";
import { ipcMain } from "electron";
import fs from "fs/promises";
import path from "path";
import { getProjectDir, loadProject } from "./editor";

export function initIpc() {
    ipcMain.on(Channels.SAVE_FILE, async (_event, data: SaveData) => {
        const fp = path.join(getProjectDir(), ...data.filepath)
        fs.writeFile(fp, data.data)
    }) 


    ipcMain.on(Channels.INITIAL_LOAD, () => {
        const dir = getProjectDir()
        if(dir) {
            loadProject(dir)
        }
    })
}