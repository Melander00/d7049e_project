import { useIpc } from '@renderer/lib/ipc/hooks'
import { setConfig } from '@renderer/store/features/configSlice'
import { useAppDispatch, useAppSelector } from '@renderer/store/hooks'
import { Channels } from '@shared/channels'
import { useEffect, useRef, useState } from 'react'
import styles from './config.module.css'

export default function Config() {
    const [isOpen, setOpen] = useState(false)
    const config = useAppSelector((state) => state.config)
    const dispatch = useAppDispatch()

    const dialog = useRef<HTMLDialogElement>(null)

    useIpc(Channels.OPEN_CONFIG, () => {
        setOpen(true)
        console.log('dasd')
    })

    useEffect(() => {
        if (isOpen) {
            dialog.current?.showModal()
        } else {
            dialog.current?.close()
        }
    }, [isOpen])

    return (
        <dialog
            ref={dialog}
            closedby="any"
            onClose={() => {
                setOpen(false)
            }}
            className={styles.dialog}
        >
            <div className={styles.container}>
                <h3>Game Config</h3>
                <label className={styles.label}>
                    <span>Physics frequency</span>
                    <input
                        type="number"
                        min={10}
                        max={240}
                        value={config.fixedTimeFrequency}
                        onChange={(e) =>
                            dispatch(
                                setConfig({
                                    ...config,
                                    fixedTimeFrequency: parseInt(e.target.value)
                                })
                            )
                        }
                    />
                </label>
                <label className={styles.label}>
                    <span>Show collision wireframe</span>
                    <input
                        type="checkbox"
                        checked={config.debugCollisionWireframe}
                        onChange={(e) =>
                            dispatch(
                                setConfig({
                                    ...config,
                                    debugCollisionWireframe: e.target.checked
                                })
                            )
                        }
                    />
                </label>

                <button
                    onClick={() => {
                        setOpen(false)
                    }}
                >
                    Close
                </button>
            </div>
        </dialog>
    )
}
