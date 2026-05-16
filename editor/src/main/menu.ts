import type { MenuItem, MenuItemConstructorOptions } from "electron";
import { Menu } from "electron";
import { exportProject, openConfigWindow, openProject, saveProject } from "./editor";
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
    {
        label: "Edit",
        submenu: [
            {
                label: "Config",
                accelerator: "Ctrl+P",
                click: openConfigWindow
            }
        ]
    }
]

if(!isProd) {
    template.push({
        role: "viewMenu"
    })
}

export const menu = Menu.buildFromTemplate(template)