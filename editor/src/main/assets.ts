import { Channels } from "@shared/channels";
import { Asset } from "@shared/ipc";
import console from "console";
import { BrowserWindow } from "electron";
import { existsSync } from "fs";
import fs from "fs/promises";
import path from "path";
import { getMainWindow } from ".";
import { createDebounce } from "./lib";

let ac = new AbortController()

const EXCLUSIONS: RegExp[] = [
    /.json/
]

const debounce = createDebounce((assets: Asset[]) => {
    const win = getMainWindow()
    if(win) {
        win.webContents.send(Channels.ASSETS, assets)
    }
}, 500)

export function startWatch(assetDir: string) {
    ac.abort()
    console.log("ended watch")

    ac = new AbortController()
    ;( async () => {
        try {

            const watcher = fs.watch(assetDir, {
                signal: ac.signal,
                recursive: true,
            })
            
            for await (const event of watcher) {
                const filename = event.filename
                if(!filename) continue;
                if(EXCLUSIONS.some(e => e.test(filename))) continue;
                
                if(event.eventType === "rename") {
                    debounce(await getAssets(assetDir))
                    
                }
            }
        } catch(err: any) {
            if (err.name === 'AbortError')
                return;
            throw err;
        }
    })()
}

export async function getAssets(assetDir: string): Promise<Asset[]> {
    if(!existsSync(assetDir)) return []

    const files = await fs.readdir(assetDir, {recursive: true, withFileTypes: true})

    return files.map(f => {

        const assetPath = path.relative(assetDir, path.join(f.parentPath, f.name))

        if(EXCLUSIONS.some(e => e.test(assetPath))) return null

        return {
            isDir: f.isDirectory(),
            path: assetPath.split(path.sep).join("/")
        }
    }).filter(f => f !== null)
}

export async function sendAssets(assetDir: string, win: BrowserWindow) {
    const assets = await getAssets(assetDir)

    win.webContents.send(Channels.ASSETS, assets)
}