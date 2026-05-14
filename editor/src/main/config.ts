

import { app } from "electron";
import fs from "fs";
import fsPromises from "fs/promises";
import path from "path";
import { setProjectDir } from "./editor";
import { configDataDir } from "./env";

type Config = {
    lastProjectDir: string
}

let config: Config = {
    lastProjectDir: app.getAppPath()
}

export async function loadConfig() {
    const configFilePath = path.join(configDataDir, "config.json")
    if(!fs.existsSync(configFilePath)) return;

    const raw = await fsPromises.readFile(configFilePath)
    const config: Config = JSON.parse(raw.toString())

    if(config.lastProjectDir) {
        setProjectDir(config.lastProjectDir)
    }
}

async function saveConfig() {
    const configFilePath = path.join(configDataDir, "config.json")

    if(!fs.existsSync(configDataDir)) {
        await fsPromises.mkdir(configDataDir, {recursive: true})
    }
        
    try {
        await fsPromises.writeFile(configFilePath, JSON.stringify(config, null, 2), {})
    } catch(e) {
        console.error(e)
    }
}

export function setLastDirConfig(dir: string) {
    config.lastProjectDir = dir;

    saveConfig()
}

export function getConfig() {
    return config;
}