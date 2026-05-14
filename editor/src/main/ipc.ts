import { ipcMain } from "electron";

export function initIpc() {
    ipcMain.on('ping', () => console.log('pong'))
}