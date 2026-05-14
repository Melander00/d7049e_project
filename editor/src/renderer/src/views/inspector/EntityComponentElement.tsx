import schema from "@renderer/assets/components.schema.json"

import {
    EntityComponent,
    removeComponent,
    updateComponent
} from "@renderer/store/features/entitiesSlice"

import {
    useAppDispatch,
    useAppSelector
} from "@renderer/store/hooks"

import ContextMenu from "@renderer/components/contextMenu/ContextMenu"
import Icon from "@renderer/components/icon/Icon"
import { useState } from "react"
import styles from "./inspector.module.css"

type EntityComponentProps = {
    component: EntityComponent
    index: number
}

const PRIMITIVES = ["string", "float", "int", "boolean"]

export default function EntityComponentElement({
    component,
    index
}: EntityComponentProps) {

    const dispatch = useAppDispatch()

    const [collapsed, setCollapsed] = useState(false)

    const entityIndex = useAppSelector(
        state => state.entities.activeIndex
    )

    const schemaEntry = Object.values(schema).find(
        s => s.class === component.class
    )

    const Menu = ContextMenu({
        options: [
            { text: "Delete Component", value: "delete", onClick: () => {
                dispatch(removeComponent({index: entityIndex, componentIndex: index}))
            } }
        ],
    })

    if(!schemaEntry) {
        return null
    }

    function setValue(path: string[], value: any) {
        dispatch(updateComponent({
            index: entityIndex,
            componentIndex: index,
            path,
            value
        }))
    }

    return(
        <>
        {Menu.element}
        <div className={styles.component} onContextMenu={e => {
            Menu.show(e.clientX, e.clientY)
        }}>

            <div className={styles.componentHeader} onClick={e => setCollapsed(f => !f)} >
                <span className={styles.componentHeaderDrop} >
                    <Icon>{collapsed ? "arrow_drop_up" : "arrow_drop_down"}</Icon>    
                </span>
                <span>
                    {schemaEntry.type}
                </span>
            </div>

            {collapsed ? "" : (
            <div className={styles.componentFields}>
                <FieldRenderer
                    schemaFields={schemaEntry.fields}
                    values={component}
                    path={[]}
                    onChange={setValue}
                />
            </div>
            )}

        </div>
        </>
    )
}

type FieldRendererProps = {
    schemaFields: any
    values: any
    path: string[]
    onChange: (path: string[], value: any) => void
}

function FieldRenderer({
    schemaFields,
    values,
    path,
    onChange
}: FieldRendererProps) {

    return(
        <>
            {Object.entries(schemaFields).map(([key, field]) => {

                const currentPath = [...path, key]
                const value = values[key]

                // Primitive field
                if(typeof field === "string") {

                    return(
                        <PrimitiveField
                            key={currentPath.join(".")}
                            name={key}
                            type={field}
                            value={value}
                            onChange={(v) =>
                                onChange(currentPath, v)
                            }
                        />
                    )
                }

                // Enum
                // @ts-ignore
                if(field.type === "enum") {

                    return(
                        <EnumField
                            key={currentPath.join(".")}
                            name={key}
                            // @ts-ignore
                            values={field.values}
                            value={value}
                            onChange={(v) =>
                                onChange(currentPath, v)
                            }
                        />
                    )
                }

                // Nested object
                return(
                    <ObjectField
                        key={currentPath.join(".")}
                        name={key}
                    >
                        <FieldRenderer
                            // @ts-ignore
                            schemaFields={field.fields}
                            values={value}
                            path={currentPath}
                            onChange={onChange}
                        />
                    </ObjectField>
                )
            })}
        </>
    )
}

type PrimitiveFieldProps = {
    name: string
    type: string
    value: any
    onChange: (v: any) => void
}

function PrimitiveField({
    name,
    type,
    value,
    onChange
}: PrimitiveFieldProps) {

    const isString = type === "string"

    return(
        <div className={styles.fieldRow}>

            <label className={styles.fieldLabel}>
                {name}
            </label>

            {type === "boolean" ? (
                <input
                    type="checkbox"
                    checked={value}
                    onChange={e =>
                        onChange(e.target.checked)
                    }
                />
            ) : (
                <input
                    className={styles.fieldInput}
                    type={
                        isString
                            ? "text"
                            : "number"
                    }

                    onDragOver={e => {
                        if(isString && e.dataTransfer.types.includes("type/asset")) {
                            e.stopPropagation()
                            e.preventDefault()
                        }
                    }}
                    onDrop={e => {
                        if(!(isString && e.dataTransfer.types.includes("type/asset"))) return
                        const raw = e.dataTransfer.getData("text/plain")
                        onChange(raw)
                    }}

                    step={type === "float" ? "any" : 1}

                    value={value}
                    onChange={e => {

                        if(type === "string") {
                            onChange(e.target.value)
                            return
                        }

                        if(e.target.value === "") {
                            onChange("")
                            return
                        }

                        onChange(Number(e.target.value))
                    }}
                />
            )}

        </div>
    )
}

type EnumFieldProps = {
    name: string
    values: string[]
    value: string
    onChange: (v: string) => void
}

function EnumField({
    name,
    values,
    value,
    onChange
}: EnumFieldProps) {

    return(
        <div className={styles.fieldRow}>

            <label className={styles.fieldLabel}>
                {name}
            </label>

            <select
                className={styles.fieldInput}
                value={value}
                onChange={e => onChange(e.target.value)}
            >
                {values.map(v => (
                    <option key={v} value={v}>
                        {v}
                    </option>
                ))}
            </select>

        </div>
    )
}

type ObjectFieldProps = {
    name: string
    children: React.ReactNode
}

function ObjectField({
    name,
    children
}: ObjectFieldProps) {

    return(
        <div className={styles.objectField}>

            <div className={styles.objectLabel}>
                {name}
            </div>

            <div className={styles.objectContent}>
                {children}
            </div>

        </div>
    )
}