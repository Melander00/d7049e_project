import { dialog } from "electron";

export async function openProject() {
    const res = await dialog.showOpenDialog({
        title: "Open Project Folder",
        properties: ["openDirectory", "createDirectory", ]
    })

    if(res.canceled || res.filePaths.length === 0) return;

    const projectDir = res.filePaths[0]

    
}

export function saveProject() {
    
}