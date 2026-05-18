import { Channels } from "@shared/channels";
import { useEffect } from "react";
import { ErrorBoundary } from "react-error-boundary";
import "./App.css";
import { useIpc } from "./lib/ipc/hooks";
import { ipcRenderer } from "./lib/ipc/ipcRenderer";
import { exportProject, saveProject } from "./lib/project/project";
import { useAppDispatch } from "./store/hooks";
import { loadProjectAction } from "./store/store";
import AssetManagerView from "./views/asset-manager/AssetManagerView";
import ComponentsView from "./views/components/ComponentsView";
import Config from "./views/config/Config";
import EntityListView from "./views/entity-list/EntityListView";
import InspectorView from "./views/inspector/InspectorView";
import NotificationsView from "./views/notifications/NotificationsView";
import { useNotifications } from "./views/notifications/useNotifications";
import SceneView from "./views/scene/SceneView";

function App() {

    const dispatch = useAppDispatch()

    const {
        addNotification,
    } = useNotifications()
 
    useIpc(Channels.SAVE_REQUESTED, () => {
        saveProject()
        addNotification("Saved", "Successfully saved the project", "success")
    })
    
    useIpc(Channels.LOAD_STATE, (_event, state) => {
        dispatch(loadProjectAction(state))
        addNotification("Loaded", "Successfully loaded the project", "success")
    })
    
    useIpc(Channels.EXPORT_REQUESTED, () => {
        exportProject()
        addNotification("Exported", "Successfully exported the project", "success")

    })

    useEffect(() => {
        console.log("initial load")
        ipcRenderer.send(Channels.INITIAL_LOAD)
    }, [])

    return (
        <>
        <Config/>
        <NotificationsView/>
        <div className="editor-grid">
            <div className="panel list"><EntityListView /></div>
            <div className="panel scene">
                <ErrorBoundary fallback="Scene Canvas Error Occurred">
                    <SceneView/>
                </ErrorBoundary>
            </div>
            <div className="panel inspector"><InspectorView/></div>
            <div className="panel components"><ComponentsView/></div>
            <div className="panel assets"><AssetManagerView/></div>
        </div>
        </>
    )
}

export default App;
