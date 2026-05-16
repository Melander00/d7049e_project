import ContextMenu from '@renderer/components/contextMenu/ContextMenu'
import Icon from '@renderer/components/icon/Icon'
import { useIpc } from '@renderer/lib/ipc/hooks'
import { ipcRenderer } from '@renderer/lib/ipc/ipcRenderer'
import { getLuaTemplate } from '@renderer/lib/lua/lua'
import { Entity } from '@renderer/store/features/entitiesSlice'
import { Channels } from '@shared/channels'
import { Asset, CreateFileRequest, RenameRequest } from '@shared/ipc'
import { useEffect, useState } from 'react'
import styles from './assetmanager.module.css'

type File = {
    name: string
    isDir: false
    path: string
}

type Folder = {
    name: string
    isDir: true
    files: Node[]
    path: string
}

type Node = Folder | File

type FolderStruct = {
    name: string
    files: {
        [name: string]: string | FolderStruct
    }
}

function flattenStruct(folder: FolderStruct, dir: string): Node[] {
    const nodes: Node[] = []
    for (const key in folder.files) {
        const val = folder.files[key]
        if (typeof val === 'string') {
            nodes.push({
                name: val,
                path: dir,
                isDir: false
            })
        } else {
            nodes.push({
                name: val.name,
                path: dir,
                isDir: true,
                files: flattenStruct(val, dir + val.name + '/')
            })
        }
    }
    return nodes
}

function createTree(assets: Asset[]) {
    const root: FolderStruct = {
        name: '',
        files: {}
    }

    // loop through to find all directories

    for (let i = 0; i < assets.length; i++) {
        const asset = assets[i]

        if (!asset.isDir) continue

        const subpaths = asset.path.split('/')
        let folder = root.files
        for (const subpath of subpaths) {
            if (!folder[subpath]) folder[subpath] = { name: subpath, files: {} }

            if (typeof folder[subpath] === 'object') folder = folder[subpath].files
        }
    }

    for (let i = 0; i < assets.length; i++) {
        const asset = assets[i]

        if (asset.isDir) continue

        const splitted = asset.path.split('/')
        const subpaths = splitted.slice(0, splitted.length - 1)
        const name = splitted[splitted.length - 1]
        let folder = root.files
        for (const subpath of subpaths) {
            // if(!folder[subpath]) folder[subpath] = {name: subpath, files: {}}

            if (typeof folder[subpath] === 'object') {
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

    const [selectedFile, setSelectedPath] = useState('')

    useIpc(Channels.ASSETS, (_ev, assets: Asset[]) => {
        setAssets(assets)
    })

    const root = createTree(assets)

    let curr = root
    let dir = ''
    for (const p of currPath) {
        if (typeof curr.files[p] === 'object') {
            curr = curr.files[p]
            dir = dir + p + '/'
        }
    }
    const nodes = flattenStruct(curr, dir)

    const enterFolder = (name: string) => {
        setCurrPath((paths) => {
            return [...paths, name]
        })
        setSelectedPath("")
    }

    const [showDrop, setShowDrop] = useState(false)

    const menu = ContextMenu({
        options: [
            {
                text: 'Create Folder',
                value: 'create-folder',
                icon: 'create_new_folder',
                onClick: () => {
                    ipcRenderer.send(Channels.CREATE_FOLDER, currPath)
                }
            },
            {
                text: "Create Script",
                value: "create-script",
                icon: "code",
                onClick: () => {
                    const data: CreateFileRequest = {
                        path: currPath,
                        filename: "script.lua",
                        content: getLuaTemplate()
                    }
                    ipcRenderer.send(Channels.CREATE_FILE, data)
                }
            },
            {
                text: "Open Folder",
                value: "open-external",
                icon: "folder_open",
                onClick: () => {
                    ipcRenderer.send(Channels.OPEN_FILE, currPath)
                }
            }
        ]
    })

    return (
        <>
            {menu.element}
            <div className={styles.container}>
                <div className={styles.header}>
                    <span
                        onClick={(e) => {
                            setCurrPath((paths) => {
                                if (paths.length > 0) {
                                    const newPaths = [...paths]
                                    newPaths.pop()
                                    return newPaths
                                }
                                return paths
                            })
                        }}
                        className={styles['back-button']}
                    >
                        <Icon>arrow_left</Icon>
                    </span>
                    <span className={styles.dirpath}>{currPath.join('/') ?? <>&nbsp;</>}</span>
                </div>

                <div
                    className={styles.nodes}
                    onContextMenu={(e) => {
                        menu.show(e.clientX, e.clientY)
                    }}
                    onClick={e => {
                        e.stopPropagation()
                        setSelectedPath("")
                    }}
                    onDragOver={e => {
                        if(!e.dataTransfer.types.includes("type/entity")) return
                        e.stopPropagation()
                        e.preventDefault()

                        setShowDrop(true)
                    }}
                    onDrop={e => {
                        if(!e.dataTransfer.types.includes("type/entity")) return
                        e.stopPropagation()

                        const raw = e.dataTransfer.getData("application/json")
                        const entity: Entity = JSON.parse(raw)

                        const data: CreateFileRequest = {
                            path: currPath,
                            filename: `${entity.name}.prefab`,
                            content: JSON.stringify(entity, null, 2)
                        }

                        ipcRenderer.send(Channels.CREATE_FILE, data)

                        setShowDrop(false)
                    }}
                    onDragLeave={e => {
                        if(!e.dataTransfer.types.includes("type/entity")) return
                        e.stopPropagation()

                        setShowDrop(false)
                    }}
                >
                    {nodes.map((e) => (
                        <NodeElement
                            node={e}
                            id={e.path + '/' + e.name}
                            key={e.path + '/' + e.name}
                            currentFolder={currPath.join('/')}
                            enterFolder={enterFolder}
                            setSelected={setSelectedPath}
                            selected={selectedFile === e.path + '/' + e.name}
                        />
                    ))}

                    {showDrop ? (
                        <>
                        
                        <NodeElement
                        
                            node={{
                                isDir: false,
                                name: "",
                                path: ""
                            }}
                            id={"/drop"}
                            key={"/drop"}
                            currentFolder={currPath.join('/')}
                            enterFolder={enterFolder}
                            setSelected={setSelectedPath}
                            selected={false}

                        />

                        </>
                    ) : ""}
                </div>
            </div>
        </>
    )
}

function getIconByExtension(ext: string) {
    switch (ext) {
        case 'prefab':
            return 'architecture'
        case 'glb':
            return '3d'
        case "lua":
            return "code"
        default:
            return 'docs'
    }
}

type NodeElementProps = {
    node: Node
    currentFolder: string
    enterFolder: (name: string) => void
    id: string
    selected: boolean
    setSelected: (id: string) => void
}

function NodeElement({ node, enterFolder, id, selected, setSelected }: NodeElementProps) {
    const [isRenaming, setRenaming] = useState(false)
    const [name, setName] = useState(node.name)

    const menu = ContextMenu({
        options: [
            {
                text: 'Rename',
                value: 'rename',
                icon: 'edit',
                onClick: () => {
                    setRenaming(true)
                }
            },
            {
                text: node.isDir ? "Open Folder" : "Open File",
                value: "open",
                icon: node.isDir ? "folder_open" : "file_open",
                onClick: () => {
                    ipcRenderer.send(Channels.OPEN_FILE, [...node.path.split("/"), node.name])
                }
            }
        ]
    })

    useEffect(() => {
        setRenaming(false)
    }, [selected])

    useEffect(() => {
        if(isRenaming === false) {

            if(name !== node.name) {
                // send a rename event
                const data: RenameRequest = {
                    path: node.path.split("/"),
                    from: node.name,
                    to: name
                }
                ipcRenderer.send(Channels.RENAME_FILE, data)
            }

        }
    }, [isRenaming])

    if (!node.isDir) {
        const names = node.name.split('.')
        const ext = names[names.length - 1]
        const icon = getIconByExtension(ext)

        return (
            <>
                {menu.element}
                <div
                    className={[styles.node, styles.file, selected ? styles.selected : ""].join(' ')}
                    draggable
                    onDragStart={(e) => {
                        e.dataTransfer.setData('text/path', node.path + node.name)
                        e.dataTransfer.setData("type/file", "")
                        if(ext === "prefab" || ext === "glb") {
                            e.dataTransfer.setData("type/new-entity", "")
                        }
                        e.dataTransfer.setData("type/ext", ext)
                        e.dataTransfer.setData('type/asset', '')
                        e.dataTransfer.setData(`ext/${ext}`, "")
                    }}
                    onClick={(e) => {
                        e.stopPropagation()
                        setSelected(id)
                    }}
                    onContextMenu={(e) => {
                        e.stopPropagation()
                        setSelected(id)
                        menu.show(e.clientX, e.clientY)
                    }}
                >
                    <Icon className={styles.icon}>{icon}</Icon>
                    {isRenaming ? (
                        <>
                            <input 
                            className={styles.renaming} 
                            autoFocus 
                            value={name} 
                            onChange={e => setName(e.target.value)} 
                            onKeyDown={e => {
                                if(e.key === "Enter") {
                                    setRenaming(false)
                                }
                            }}
                            />
                        </>
                    ) : (
                        <span>{name}</span>
                    )}
                </div>
            </>
        )
    }

    return (
        <>
            {menu.element}
            <div
                className={[styles.node, styles.folder, selected ? styles.selected : ""].join(' ')}
                onClick={(e) => {
                    e.stopPropagation()
                    if (selected) {
                        enterFolder(node.name)
                    }

                    setSelected(id)
                }}
                onContextMenu={(e) => {
                    e.stopPropagation()
                    setSelected(id)
                    menu.show(e.clientX, e.clientY)
                }}
            >
                <Icon className={styles.icon}>folder</Icon>
                {isRenaming ? (
                    <>
                        <input 
                        className={styles.renaming} 
                        autoFocus 
                        value={name} 
                        onChange={e => setName(e.target.value)} 
                        onKeyDown={e => {
                            if(e.key === "Enter") {
                                setRenaming(false)
                            }
                        }}
                        />
                    </>
                ) : (
                    <span>{name}</span>
                )}
            </div>
        </>
    )
}
