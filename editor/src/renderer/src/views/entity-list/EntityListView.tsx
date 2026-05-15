import type { MouseEvent } from "react";

import ContextMenu from "../../components/contextMenu/ContextMenu";
import { createEntity, duplicateEntity, removeEntity, setActiveEntity, type Entity } from "../../store/features/entitiesSlice";
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

    const menu = ContextMenu({
        options: [
            { text: "Create Entity", value: "create" },
            { text: "Delete Entity", value: "delete" },
            { text: "Duplicate Entity", value: "duplicate" },
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
            <div className={styles.container}>
                <div className={styles.groups} onContextMenu={onRightClick}>
                    {...groups}
                    {/* {entities.map((e, i) => (
                        <EntityComponent key={e.id} index={i} entity={e} />
                    ))} */}
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
                    <span className={styles.tagName}>{tagName}</span>
                </div>
            )}
            <div className={styles.entities}>
                {entities.map((e) => (
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
                >
                {entity.name}
            </div>
        </>
    );
}
