import { useState, type MouseEvent } from "react";

import schema from "@renderer/assets/components.schema.json";
import Icon from "@renderer/components/icon/Icon";
import { ipcRenderer } from "@renderer/lib/ipc/ipcRenderer";
import { Channels } from "@shared/channels";
import { ReadFileRequest } from "@shared/ipc";
import ContextMenu from "../../components/contextMenu/ContextMenu";
import { createEntity, createEntityFromTemplate, duplicateEntity, removeEntity, setActiveEntity, type Entity } from "../../store/features/entitiesSlice";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import styles from "./entitylist.module.css";

type EntityInGroup = {
    entity: Entity,
    index: number
}

export default function EntityListView() {
    const entities = useAppSelector((state) => state.entities.entities);
    const activeIndex = useAppSelector((state) => state.entities.activeIndex);
    const dispatch = useAppDispatch();

    const [showDrop, setShowDrop] = useState(false)

    const menu = ContextMenu({
        options: [
            { text: "Create Entity", value: "create", icon: "add" },
            { text: "Delete Entity", value: "delete", icon: "delete" },
            { text: "Duplicate Entity", value: "duplicate", icon: "content_copy" },
        ],
        selectFn: (option) => {
            switch (option.value) {
                case "create":
                    dispatch(createEntity({}));
                    break;
                case "delete":
                    dispatch(removeEntity(activeIndex));
                    break;
                case "duplicate":
                    dispatch(duplicateEntity(activeIndex))
                    break;
            }
        },
    });

    const onRightClick = (e: MouseEvent<HTMLDivElement>) => {
        menu.show(e.clientX, e.clientY);
    };

    const tags: {
        [tag: string]: EntityInGroup[]
    } = {}

    for(let i = 0; i < entities.length; i++) {
        const entity = entities[i]
    // for(const entity of entities) {
        const tag = entity.tag;
        if(tags[tag]) {
            tags[tag].push({entity, index: i})
            continue
        } else {
            tags[tag] = [{entity, index: i}]
        }
    }

    const groups: React.ReactNode[] = []
    for(const tag in tags) {
        groups.push((
            <EntityGroup key={tag} tagName={tag} entities={tags[tag]} />
        ))
    }

    return (
        <>
            {menu.element}
            <div 
            className={styles.container} 
            onContextMenu={onRightClick}
            onDragOver={e => {
                if(!e.dataTransfer.types.includes("type/new-entity")) return
                e.stopPropagation()
                e.preventDefault()

                setShowDrop(true)
            }}
            onDrop={async e => {
                if(!e.dataTransfer.types.includes("type/new-entity")) return
                e.stopPropagation()

                const path = e.dataTransfer.getData("text/path")
                const ext = e.dataTransfer.getData("type/ext")

                if(ext === "prefab") {   
                    const req: ReadFileRequest = {
                        path: path.split("/"),
                        filename: ""
                    }
                    const data = await ipcRenderer.invoke(Channels.READ_FILE, req)
                    const entity = JSON.parse(data)

                    dispatch(createEntityFromTemplate(entity))
                } else if(ext === "glb") {

                    const splitted = path.split("/")
                    const names = splitted[splitted.length-1].split(".")
                    const name = names[0]

                    const entity: Entity = {
                        id: "",
                        tag: "",
                        name: name,
                        components: [
                            {
                                class: schema.transform.class,
                                position: {
                                    x: 0,
                                    y: 0,
                                    z: 0,
                                },
                                rotation: {
                                    x: 0,
                                    y: 0,
                                    z: 0,
                                    w: 1,
                                },
                                scale: {
                                    x: 1,
                                    y: 1,
                                    z: 1
                                }
                            },
                            {
                                class: schema.model.class,
                                assetPath: path
                            }
                        ]
                    }

                    dispatch(createEntityFromTemplate(entity))

                }


                setShowDrop(false)
            }}
            onDragLeave={e => {
                if(!e.dataTransfer.types.includes("type/new-entity")) return
                e.stopPropagation()

                setShowDrop(false)
            }}
            >
                <div className={styles.groups}>
                    {...groups}
                    {/* {entities.map((e, i) => (
                        <EntityComponent key={e.id} index={i} entity={e} />
                    ))} */}
                    {showDrop ? (
                        <>
                        <EntityComponent index={-2} entity={{id: "", name: "New Entity", components: [], tag: ""}} />
                        </>
                    ) : ""}
                </div>
            </div>
        </>
    );
}

type EntityGroupProps = {
    entities: EntityInGroup[],
    tagName: string
}

function EntityGroup({
    entities,
    tagName
}: EntityGroupProps) {

    const dispatch = useAppDispatch();

    const [collapsed, setCollapsed] = useState(false)

    const menu = ContextMenu({
        options: [
            { text: `Create Entity with tag ${tagName}`, value: "create", onClick: () => {
                dispatch(createEntity({tag: tagName}))
            } },
        ],
    });

    const onRightClick = (e: MouseEvent<HTMLDivElement>) => {
        e.stopPropagation()
        menu.show(e.clientX, e.clientY);
    };

    return(
        <>
        {menu.element}
        <div className={styles.group}>
            {tagName === "" ? "" : (
                <div onContextMenu={onRightClick} className={styles.tag}>
                    <div className={styles.tagName} onClick={() => setCollapsed(f => !f)}>
                        <Icon>{collapsed ? "arrow_drop_up" : "arrow_drop_down"}</Icon>
                        <span>{tagName}</span>
                    </div>
                </div>
            )}
            <div className={styles.entities}>
                {collapsed ? "" : entities.map((e) => (
                    <EntityComponent key={e.entity.id} index={e.index} entity={e.entity} />
                ))}
            </div>
        </div>
        </>
    )
}

type EntityProps = {
    entity: Entity;
    index: number;
};

function EntityComponent({ entity, index }: EntityProps) {
    const dispatch = useAppDispatch();
    const isActive = useAppSelector((state) => state.entities.activeIndex === index);

    return (
        <>
            <div 
                className={[styles.entity, isActive ? styles.active : ""].join(" ")} 
                onClick={() => {
                    dispatch(setActiveEntity(index))
                }}
                onContextMenu={e => {
                    dispatch(setActiveEntity(index))
                }}
                draggable={isActive}
                onDragStart={e => {
                    e.dataTransfer.setData("type/entity", "")
                    e.dataTransfer.setData("application/json", JSON.stringify(entity))
                }}
                >
                {entity.name}
            </div>
        </>
    );
}
