import electron from "electron";
const { app, BrowserWindow } = electron;

const isProd = process.env["NODE_ENV"] === "production" || true;

const createWindow = () => {
    const win = new BrowserWindow({
        width: 600,
        height: 600,
    });

    if (isProd) {
        win.loadFile("./www/index.html");
    }
};

app.whenReady().then(() => {
    createWindow();
});

app.on("window-all-closed", () => {
    if (process.platform !== "darwin") app.quit();
});
