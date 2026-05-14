import type { MouseEvent } from "react";

import ContextMenu from "../../components/contextMenu/ContextMenu";
import { createEntity, duplicateEntity, removeEntity, setActiveEntity, type Entity } from "../../store/features/entitiesSlice";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import styles from "./entitylist.module.css";

export default function EntityListView() {
    const entities = useAppSelector((state) => state.entities.entities);
    const activeIndex = useAppSelector((state) => state.entities.activeIndex);
    const dispatch = useAppDispatch();

    const menu = ContextMenu({
        options: [
            { text: "Create Entity", value: "create" },
            { text: "Delete Active Entity", value: "delete" },
            { text: "Duplicate Entity", value: "duplicate" },
        ],
        selectFn: (option) => {
            switch (option.value) {
                case "create":
                    dispatch(createEntity());
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

    return (
        <>
            {menu.element}
            <div className={styles.container}>
                <div className={styles.entities} onContextMenu={onRightClick}>
                    {entities.map((e, i) => (
                        <EntityComponent key={e.id} index={i} entity={e} contextMenu={onRightClick} />
                    ))}
                </div>
            </div>
        </>
    );
}

type EntityProps = {
    entity: Entity;
    index: number;
    contextMenu: (e: MouseEvent<HTMLDivElement>) => void;
};

function EntityComponent({ entity, index, contextMenu }: EntityProps) {
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
                    e.stopPropagation()
                    dispatch(setActiveEntity(index))
                    contextMenu(e)
                }}
                >
                {entity.name}
            </div>
        </>
    );
}
