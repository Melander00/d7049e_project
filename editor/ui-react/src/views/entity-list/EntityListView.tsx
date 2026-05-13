import type { MouseEvent } from "react"

import { createEntity, setActiveEntity, type Entity } from "../../store/features/entitiesSlice"
import { useAppDispatch, useAppSelector } from "../../store/hooks"
import styles from "./entitylist.module.css"

export default function EntityListView() {

    const entities = useAppSelector(state => state.entities.entities)
    const dispatch = useAppDispatch()


    const onRightClick = (e: MouseEvent<HTMLDivElement>) => {
        dispatch(createEntity())
    }

    return(
        <div className={styles.container}>
            <div
                className={styles.entities}
                onContextMenu={onRightClick}
            >
                {entities.map((e, i) => <EntityComponent key={e.id} index={i} entity={e} />)}
            </div>
        </div>
    )
}

type EntityProps = {
    entity: Entity,
    index: number
}

function EntityComponent({
    entity,
    index
}: EntityProps) {

    const dispatch = useAppDispatch()
    const isActive = useAppSelector(state => state.entities.activeIndex === index)

    return(
        <div className={[styles.entity, isActive ? styles.active : ""].join(" ")} onClick={_ => dispatch(setActiveEntity(index))}>
            {entity.name}
        </div>
    )
}