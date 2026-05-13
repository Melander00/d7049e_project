import electron from "electron";
const { app, BrowserWindow } = electron;

const isProd = process.env["NODE_ENV"] === "production";

const createWindow = () => {
    const win = new BrowserWindow({
        width: 1200,
        height: 800,
    });

    if (isProd) {
        win.loadFile("./www/index.html");
    } else {
        win.loadURL("http://localhost:5173")
    }
};

app.whenReady().then(() => {
    createWindow();
});

app.on("window-all-closed", () => {
    if (process.platform !== "darwin") app.quit();
});
