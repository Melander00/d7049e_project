import { useEffect, useState } from "react"
import * as THREE from "three"

import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader.js"

import { clone } from "three/examples/jsm/utils/SkeletonUtils.js"

import { ipcRenderer } from "@renderer/lib/ipc/ipcRenderer"
import { Channels } from "@shared/channels"
import type { GLTF } from "three/examples/jsm/loaders/GLTFLoader.js"

const CACHE = new Map<string, GLTF>()

export function useGLTFDisk(path: string) {

    const [gltf, setGLTF] =
        useState<GLTF | null>(null)

    const [loading, setLoading] =
        useState(true)

    const [error, setError] =
        useState<any>(null)

    useEffect(() => {

        let mounted = true

        async function load() {

            try {

                setLoading(true)

                // Cache

                if(CACHE.has(path)) {

                    const cached =
                        CACHE.get(path)!

                    setGLTF({
                        ...cached,
                        scene: clone(
                            cached.scene
                        ) as THREE.Group
                    })

                    setLoading(false)

                    return
                }

                // IPC load

                const arrayBuffer = await ipcRenderer.invoke(Channels.LOAD_GLTF, path)

                // Parse

                const loader =
                    new GLTFLoader()

                loader.parse(
                    arrayBuffer,
                    "",

                    parsed => {

                        if(!mounted) return

                        CACHE.set(path, parsed)

                        setGLTF({
                            ...parsed,
                            scene:
                                clone(
                                    parsed.scene
                                ) as THREE.Group
                        })

                        setLoading(false)
                    },

                    err => {

                        console.error(err)

                        setError(err)

                        setLoading(false)
                    }
                )

            } catch(err) {

                console.error(err)

                setError(err)

                setLoading(false)
            }
        }

        load()

        return () => {
            mounted = false
        }

    }, [path])

    return {
        gltf,
        loading,
        error
    }
}