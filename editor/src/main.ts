import { app, BrowserWindow } from "electron";
import path from "node:path";
import { isProd, srcDir } from "./env.js";
import { menu } from "./menu.js";
// const { app, BrowserWindow } = electron;



const createWindow = () => {
    const win = new BrowserWindow({
        width: 1920,
        height: 1080,
        webPreferences: {
            preload: path.join(srcDir, "preload.js")
        }
    });

    if (isProd) {
        win.loadFile("./www/index.html");
    } else {
        win.loadURL("http://localhost:5173")
    }

    win.setMenu(menu)
};

app.whenReady().then(() => {
    createWindow();
});

app.on("window-all-closed", () => {
    if (process.platform !== "darwin") app.quit();
});
