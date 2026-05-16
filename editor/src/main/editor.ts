import { Channels } from "@shared/channels";
import { app, type BrowserWindow, dialog } from "electron";
import fs from "fs";
import path from "path";
import { getMainWindow } from ".";
import { sendAssets, startWatch } from "./assets";
import { setLastDirConfig } from "./config";

let projectDir: string = app.getAppPath()

export function updateTitle(win: BrowserWindow) {
    win.setTitle(`Mobile Slop Editor - ${projectDir}`)
}

export function setProjectDir(dir: string) {
    console.log("Project dir set to:", dir)
    projectDir = dir;
    setLastDirConfig(dir)

    startWatch(dir)
    
    const win = getMainWindow()
    if(win) {
        updateTitle(win)
        sendAssets(dir, win)
    }
}

export function getProjectDir() {
    return projectDir
}

let state: unknown = undefined;

export function getState() {
    return state;
}

export function loadProject(dir: string) {
    const stateFile = path.join(dir, "project.json")

    if(fs.existsSync(stateFile)) {        
        fs.readFile(stateFile, {encoding: "utf-8"}, (err, data) => {
            if(err) throw err;

            const json = JSON.parse(data)
            state = json;
            getMainWindow().webContents.send(Channels.LOAD_STATE, json)
        })
    }
}


export async function openProject() {
    const res = await dialog.showOpenDialog({
        title: "Open Project Folder",
        properties: ["openDirectory", "createDirectory", ]
    })

    if(res.canceled || res.filePaths.length === 0) return;

    const dir = res.filePaths[0]
    setProjectDir(dir)
    loadProject(dir)
}

export function saveProject() {
    const win = getMainWindow()
    win?.webContents.send(Channels.SAVE_REQUESTED)
}


export async function exportProject() {
    const win = getMainWindow()
    win?.webContents.send(Channels.EXPORT_REQUESTED)
}



export function openConfigWindow() {
    const win = getMainWindow()
    win?.webContents.send(Channels.OPEN_CONFIG)
}