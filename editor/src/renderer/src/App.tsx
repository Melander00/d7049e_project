import { Channels } from "@shared/channels";
import { useEffect } from "react";
import "./App.css";
import { useIpc } from "./lib/ipc/hooks";
import { ipcRenderer } from "./lib/ipc/ipcRenderer";
import { saveProject } from "./lib/project/project";
import { useAppDispatch } from "./store/hooks";
import { loadProjectAction } from "./store/store";
import AssetManagerView from "./views/asset-manager/AssetManagerView";
import ComponentsView from "./views/components/ComponentsView";
import EntityListView from "./views/entity-list/EntityListView";
import InspectorView from "./views/inspector/InspectorView";
import SceneView from "./views/scene/SceneView";

function App() {

    const dispatch = useAppDispatch()

    useIpc(Channels.SAVE_REQUESTED, () => {
        saveProject()
    })

    useIpc(Channels.LOAD_STATE, (_event, state) => {
        dispatch(loadProjectAction(state))
    })

    useIpc(Channels.EXPORT_REQUESTED, () => {
        
    })

    useEffect(() => {
        console.log("initial load")
        ipcRenderer.send(Channels.INITIAL_LOAD)
    }, [])

    return (
        <>
        <div className="editor-grid">
            <div className="panel list"><EntityListView /></div>
            <div className="panel scene"><SceneView/></div>
            <div className="panel inspector"><InspectorView/></div>
            <div className="panel components"><ComponentsView/></div>
            <div className="panel assets"><AssetManagerView/></div>
        </div>
        </>
    )
}

export default App;
