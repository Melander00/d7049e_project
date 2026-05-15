import { Channels } from "@shared/channels";
import type { CreateFileRequest, RenameRequest, SaveData } from "@shared/ipc";
import { ipcMain, shell } from "electron";
import { existsSync } from "fs";
import fs from "fs/promises";
import path from "path";
import { getMainWindow } from ".";
import { sendAssets } from "./assets";
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


    ipcMain.on(Channels.INITIAL_LOAD, (_ev) => {
        const dir = getProjectDir()
        if(dir) {
            loadProject(dir)
            sendAssets(dir, getMainWindow())
        }
    })

    ipcMain.handle(Channels.LOAD_GLTF, async (_ev, assetPath) => {
        if(!assetPath) return null;

        const fp = path.join(getProjectDir(), assetPath)

        if(!existsSync(fp)) return null;

        const buffer = await fs.readFile(fp)
        return buffer.buffer.slice(
            buffer.byteOffset,
            buffer.byteOffset + buffer.byteLength
        )
    })

    ipcMain.on(Channels.CREATE_FOLDER, async (_ev, paths) => {
        
        const fp = path.join(getProjectDir(), ...paths)
        let folder_index = 0
        let cont = true
        let name = "New Folder"
        while(cont) {
            
            if(!existsSync(path.join(fp, name))) {
                await fs.mkdir(path.join(fp, name), {recursive: true})
                cont = false;
                break;
            }

            folder_index++;
            name = `New Folder (${folder_index})`
        }
    })

    ipcMain.on(Channels.RENAME_FILE, async (_ev, req: RenameRequest) => {
        const fp = path.join(getProjectDir(), ...req.path)
        const old = path.join(fp, req.from)
        const to = path.join(fp, req.to)
        
        if(!existsSync(old) || existsSync(to)) return

        await fs.rename(old, to)
    })

    ipcMain.on(Channels.CREATE_FILE, async (_ev, req: CreateFileRequest) => {

        const fp = path.join(getProjectDir(), ...req.path)

        const splitted = req.filename.split(".")
        const filename = splitted.slice(0, splitted.length-1).join(".")
        const ext = splitted[splitted.length-1]

        let accum = 0
        let cont = true
        let name = `${filename}.${ext}`

        while(cont) {
            if(!existsSync(path.join(fp, name))) {
                await fs.writeFile(path.join(fp, name), req.content)
                cont = false;
                break;
            }

            accum++
            name = `${filename} (${accum}).${ext}`
        }

    })

    ipcMain.on(Channels.OPEN_FILE, async (_ev, paths) => {
        const fp = path.join(getProjectDir(), ...paths)
        if(!existsSync(fp)) return

        shell.openPath(fp)
    })
}