import "./App.css";
import AssetManagerView from "./views/asset-manager/AssetManagerView";
import ComponentsView from "./views/components/ComponentsView";
import EntityListView from "./views/entity-list/EntityListView";
import InspectorView from "./views/inspector/InspectorView";
import SceneView from "./views/scene/SceneView";

function App() {
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
