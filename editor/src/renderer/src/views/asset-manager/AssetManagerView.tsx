import Icon from "@renderer/components/icon/Icon"
import { useIpc } from "@renderer/lib/ipc/hooks"
import { Channels } from "@shared/channels"
import { Asset } from "@shared/ipc"
import { useState } from "react"
import styles from "./assetmanager.module.css"

type File = {
    name: string,
    isDir: false
    path: string,
}

type Folder = {
    name: string,
    isDir: true,
    files: Node[],
    path: string
}

type Node = Folder | File

type FolderStruct = {
    name: string,
    files: {
        [name: string]: string | FolderStruct
    }
}

function flattenStruct(folder: FolderStruct, dir: string): Node[] {
    const nodes: Node[] = []
    for(const key in folder.files) {
        const val = folder.files[key]
        if(typeof val === "string") {
            nodes.push({
                name: val,
                path: dir,
                isDir: false,
            })
        } else {
            nodes.push({
                name: val.name,
                path: dir,
                isDir: true,
                files: flattenStruct(val, dir + val.name + "/")
            })
        }
    }
    return nodes
}

function createTree(assets: Asset[]) {

    const root: FolderStruct = {
        name: "",
        files: {}
    }

    // loop through to find all directories

    for(let i = 0; i < assets.length; i++) {
        const asset = assets[i]

        if(!asset.isDir) continue;

        const subpaths = asset.path.split("/")
        let folder = root.files
        for(const subpath of subpaths) {

            if(!folder[subpath]) folder[subpath] = {name: subpath, files: {}}

            if(typeof folder[subpath] === "object")
                folder = folder[subpath].files
        }
    }

    for(let i = 0; i < assets.length; i++) {
        const asset = assets[i]

        if(asset.isDir) continue;

        const splitted = asset.path.split("/")
        const subpaths = splitted.slice(0, splitted.length-1)
        const name = splitted[splitted.length-1]
        let folder = root.files
        for(const subpath of subpaths) {
            // if(!folder[subpath]) folder[subpath] = {name: subpath, files: {}}

            if(typeof folder[subpath] === "object") {
                folder = folder[subpath].files
            }
        }
        folder[name] = name
    }

    return root
}

export default function AssetManagerView() {

    const [assets, setAssets] = useState<Asset[]>([])

    const [currPath, setCurrPath] = useState<string[]>([])

    useIpc(Channels.ASSETS, (_ev, assets: Asset[]) => {
        setAssets(assets)
    })

    const root = createTree(assets)

    let curr = root
    let dir = ""
    for(const p of currPath) {
        if(typeof curr.files[p] === "object") {
            curr = curr.files[p]
            dir = dir + p + "/"
        }
    }
    const nodes = flattenStruct(curr, dir)

    const enterFolder = (name: string) => {
        setCurrPath(paths => {
            return [...paths, name]
        })
    }

    return(
        <>
        <div className={styles.container}>
            <div className={styles.header}>
                <span onClick={e => {
                    setCurrPath(paths => {
                        if(paths.length > 0) {
                            const newPaths = [...paths]
                            newPaths.pop()
                            return newPaths
                        }
                        return paths
                    })
                }} className={styles['back-button']}><Icon>arrow_left</Icon></span>
                <span className={styles.dirpath}>{currPath.join("/") ?? <>&nbsp;</>}</span>
            </div>

            <div className={styles.nodes}>
                {nodes.map(e => (
                    <NodeElement 
                    node={e} 
                    key={e.path + "/" + e.name} 
                    currentFolder={currPath.join("/")}
                    enterFolder={enterFolder}
                    />
                ))}
            </div>
        </div>
        </>
    )
}

function getIconByExtension(ext: string) {
    switch(ext) {
        case "prefab":
            return "architecture"
        case "glb":
            return "3d"
        default:
            return "file"
    }
}

type NodeElementProps = {
    node: Node,
    currentFolder: string
    enterFolder: (name: string) => void
}

function NodeElement({
    node,
    enterFolder
}: NodeElementProps) {

    if(!node.isDir) {
        const names = node.name.split(".")
        const ext = names[names.length-1]
        const icon = getIconByExtension(ext)

        return(
            <>

            <div 
                className={[styles.node, styles.file].join(" ")}
                draggable
                onDragStart={e => {
                    e.dataTransfer.setData("text/path", node.path + node.name)
                    e.dataTransfer.setData(ext === "prefab" ? "type/prefab" : "type/asset", "")
                }}
            >
                <Icon className={styles.icon}>{icon}</Icon>
                <span>{node.name}</span>
            </div>
            </>
        )
    }


    return(
        <>
        <div className={[styles.node, styles.folder].join(" ")} onClick={e => {
            enterFolder(node.name)
        }}>
            <Icon className={styles.icon}>folder</Icon>
            <span>{node.name}</span>
        </div>
        </>
    )
}