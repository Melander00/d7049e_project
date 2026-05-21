import { store } from "@renderer/store/store"
import { Channels } from "@shared/channels"
import { PROJECT_ROOT_FILE } from "@shared/constants"
import { SaveData } from "@shared/ipc"
import { ipcRenderer } from "../ipc/ipcRenderer"

async function saveFile(data: SaveData) {
    ipcRenderer.send(Channels.SAVE_FILE, data)
}

export async function saveProject() {
    const state = store.getState()
    const saveData: SaveData = {
        filepath: [PROJECT_ROOT_FILE],
        data: JSON.stringify(state, null, 2)
    }
    saveFile(saveData)
}

export async function exportProject() {
    const state = store.getState();

    
    
    const assets: string[] = []
    const entitiesJson: string[] = []

    state.entities.entities.forEach((entity) => {
        const components = entity.components;
        components.forEach((comp) => {

            const c: string = comp.class

            if(c.endsWith("ModelComponent")) {
                if(comp.assetPath && !assets.includes(comp.assetPath)) {
                    assets.push(comp.assetPath)
                }
            }
        })
        entitiesJson.push(JSON.stringify(entity))
    })

    // save config file
    saveFile({
        filepath: ["config.json"],
        data: JSON.stringify(state.config, null, 2)
    })

    // save glb-assets file
    saveFile({
        filepath: ["glb-assets.json"],
        data: JSON.stringify({paths: assets}, null, 2)
    })

    // save entities file
    saveFile({
        filepath: ["entities.jsonl"],
        data: entitiesJson.join("\n")
    })
}