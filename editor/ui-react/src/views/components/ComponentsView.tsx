import styles from "./components.module.css";

import schema from "@assets/components.schema.json";

export default function ComponentsView() {

    const components = []
    for (const key in schema) {
        const comp = schema[key as keyof typeof schema ];
        components.push({...comp, key: key})
    }

    return(
        <>
        <div className={styles.container}>
            <div className={styles['component-list']}>
                {components.map(e => <Component key={e.type} name={e.type} id={e.key} />)}
            </div>
        </div>
        </>
    )
}

type ComponentProps = {
    name: string,
    id: string
}

function Component({
    name,
    id
}: ComponentProps) {
    return(
        <>
            <div 
                className={styles.component}
                draggable
                onDragStart={e => {
                    e.dataTransfer.setData("application/json", JSON.stringify({
                        type: "component",
                        data: id
                    }))
                    e.dataTransfer.setData("type/component", "")
                }}
            >
                <span>{name}</span>
            </div>
        </>
    )
}