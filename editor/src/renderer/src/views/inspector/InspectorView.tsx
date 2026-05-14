import { useState } from "react";
import styles from "./inspector.module.css";

import schema from "@renderer/assets/components.schema.json";
import { schemaCompToFields as schemaCompToObject } from "@renderer/lib/schema/schema";
import { addComponent, setEntityName } from "../../store/features/entitiesSlice";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import EntityComponentElement from "./EntityComponentElement";

export default function InspectorView() {

    const dispatch = useAppDispatch()

    const entity = useAppSelector(state => {
        if(state.entities.activeIndex < 0) return null;

        return state.entities.entities[state.entities.activeIndex]
    })

    const entityIndex = useAppSelector(state => state.entities.activeIndex)

    
    const [showDrop, setShowDrop] = useState(false)
    
    const onComponentDrop = (type: keyof typeof schema) => {
        if(!entity) return;

        const component = schema[type]

        // We enforce a single instance per component type
        if(entity.components.find(e => e.class === component.class)) return

        const comp = schemaCompToObject(component)

        dispatch(addComponent({
            index: entityIndex,
            component: comp
        }))
        
    }
    
    if(entity === null) return ""
    return(
        <div className={styles.container}>
            <input value={entity.name} onChange={e => {
                dispatch(setEntityName({name: e.currentTarget.value, index: entityIndex}))
            }} />
            <div 
                className={styles['component-list']}
                onDragOver={e => {
                    if(e.dataTransfer.types.includes("type/component")) {
                        e.preventDefault();
                        setShowDrop(true)
                    }
                }}
                onDragLeave={_e => {
                    setShowDrop(false)
                }}
                onDrop={e => {
                    setShowDrop(false)
                    const raw = e.dataTransfer.getData("application/json")
                    const json = JSON.parse(raw)
                    onComponentDrop(json.data)
                }}
            >

                {entity.components.map((c,i) => <EntityComponentElement index={i} key={`${entity.id}-${c.class}`} component={c} />)}

                {showDrop ? (<>
                    <div className={styles['drop-component']}>
                        <hr />
                    </div>
                </>) : ""}
            </div>
        </div>
    )
}
