import type { MenuItem, MenuItemConstructorOptions } from "electron";
import { Menu } from "electron";
import { openProject, saveProject } from "./editor";
import { isProd } from "./env";

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