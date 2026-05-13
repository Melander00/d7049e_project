
import { useState } from "react"
import styles from "./contextmenu.module.css"

type Option = {
    text: string,
    value: string
}

type ContextMenuProps = {
    options: Option[],
    selectFn: (option: Option) => void
}

export default function ContextMenu({
    options,
    selectFn
}: ContextMenuProps) {
    const [x, setX] = useState(0)
    const [y, setY] = useState(0)

    const [shouldShow, setShow] = useState(false)

    function show(x: number, y: number) {
        setX(x-10)
        setY(y-10)
        setShow(true)
    }

    return {
        show,
        element: (
            <>
            <div 
            onMouseLeave={_ => {setShow(false)}}
            className={styles.container} 
            style={{
                left: x,
                top: y,
                display: shouldShow ? "flex" : "none"
            }}>
                {options.map(op => (
                    <div className={styles.option} key={op.value} onClick={_ => {
                        selectFn(op);
                        setShow(false)
                    }}>
                        {op.text}
                    </div>
                ))}
            </div>
            </>
        )
    }
}