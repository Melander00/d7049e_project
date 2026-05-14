import { Channels } from "@shared/channels";
import type { SaveData } from "@shared/ipc";
import { ipcMain } from "electron";
import fs from "fs/promises";
import path from "path";
import { getProjectDir, loadProject } from "./editor";

export function initIpc() {
    ipcMain.on(Channels.SAVE_FILE, async (_event, data: SaveData) => {
        const fp = path.join(getProjectDir(), ...data.filepath)
        try {
            await fs.writeFile(fp, data.data)
        } catch(e) {
            console.error(e)
        }
    }) 


    ipcMain.on(Channels.INITIAL_LOAD, () => {
        const dir = getProjectDir()
        if(dir) {
            loadProject(dir)
        }
    })

    ipcMain.handle(Channels.LOAD_GLTF, async (ev, assetPath) => {
        const fp = path.join(getProjectDir(), assetPath)
        const buffer = await fs.readFile(fp)
        return buffer.buffer.slice(
            buffer.byteOffset,
            buffer.byteOffset + buffer.byteLength
        )
    })
}