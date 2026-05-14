import type { MenuItem, MenuItemConstructorOptions } from "electron";
import electron from "electron";
import { openProject, saveProject } from "./editor.js";
import { isProd } from "./env.js";

const { app, Menu } = electron

const template: (MenuItemConstructorOptions | MenuItem)[] = [
    {
        label: "File",
        submenu: [
            {
                label: "Open",
                accelerator: "Ctrl+O",
                click: openProject
            },
            {
                label: "Save",
                accelerator: "Ctrl+S",
                click: saveProject
            },
            { role: "quit" }
        ]
    },
    
]

if(!isProd) {
    template.push({
        role: "viewMenu"
    })
}

export const menu = Menu.buildFromTemplate(template)