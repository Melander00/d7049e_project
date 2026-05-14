import { store } from "@renderer/store/store"
import { Channels } from "@shared/channels"
import { PROJECT_ROOT_FILE } from "@shared/constants"
import { SaveData } from "@shared/ipc"
import { ipcRenderer } from "../ipc/ipcRenderer"

export async function saveProject() {
    const state = store.getState()
    const saveData: SaveData = {
        filepath: [PROJECT_ROOT_FILE],
        data: JSON.stringify(state)
    }
    ipcRenderer.send(Channels.SAVE_FILE, saveData)
}

export async function exportProject() {
    const state = store.getState();

    // save config file
    

    // save glb-assets file

    // save entities file
}