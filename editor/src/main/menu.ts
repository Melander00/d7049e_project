import type { MenuItem, MenuItemConstructorOptions } from "electron";
import { Menu } from "electron";
import { exportProject, openProject, saveProject } from "./editor";
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
                click: saveProject,
                enabled: false,
            },
            {
                label: "Export",
                accelerator: "Ctrl+E",
                click: exportProject
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