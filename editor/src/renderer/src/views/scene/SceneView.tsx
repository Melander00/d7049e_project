import {
    GizmoHelper,
    GizmoViewport,
    Grid,
    OrbitControls,
    Text,
    TransformControls
} from "@react-three/drei"
import { Canvas } from "@react-three/fiber"

import * as THREE from "three"

import { useEffect, useMemo, useRef, useState } from "react"

import {
    useAppDispatch,
    useAppSelector
} from "@renderer/store/hooks"

import {
    setActiveEntity,
    updateComponent
} from "@renderer/store/features/entitiesSlice"

import { createDebounce } from "@renderer/lib/debounce"
import { useDispatch } from "react-redux"
import styles from "./scene.module.css"
import { useGLTFDisk } from "./useGLTFDisk"

const CLASS = {
    transform:
        "com.example.brainslop.core.components.TransformComponent",

    model:
        "com.example.brainslop.core.components.ModelComponent",

    camera:
        "com.example.brainslop.core.components.CameraComponent",

    text:
        "com.example.brainslop.core.components.TextComponent"

    // TODO: Add CollisionComponent and PhysicsComponent and display the collision shape
}

export default function SceneView() {

    const entities = useAppSelector(
        state => state.entities.entities
    )

    const activeIndex = useAppSelector(
        state => state.entities.activeIndex
    )

    const dispatch = useDispatch()

    const [isDragging, setDragging] = useState(false)

    const [mode, setMode] = useState<"translate" | "rotate" | "scale">("translate")


    useEffect(() => {

        const listener = (e: KeyboardEvent) => {
            switch(e.key.toLowerCase()) {
                case "w":
                    setMode("translate")
                    break;
                case "s":
                    setMode("scale")
                    break;
                case "r":
                    setMode("rotate")
                    break;
            }
        }

        window.addEventListener("keydown", listener)

        return () => {
            window.removeEventListener("keydown", listener)
        }
    }, [])

    return(
        <div 
            className={styles.container}
        >

            <Canvas
                shadows
                camera={{
                    position: [8, 8, 8],
                    fov: 60
                }}
                onPointerMissed={_e => {
                    dispatch(setActiveEntity(-1))
                }}
            >

                <color
                    attach="background"
                    args={["#1a1a1a"]}
                />

                <ambientLight intensity={0.7} />

                <directionalLight
                    position={[10, 10, 5]}
                    intensity={1.5}
                    castShadow
                />

                <Grid
                    args={[200, 200]}
                    cellSize={1}
                    sectionSize={10}
                    fadeDistance={150}
                    fadeStrength={1}
                />

                <OrbitControls
                    makeDefault
                    enableDamping
                    dampingFactor={0.1}
                    enabled={!isDragging}
                />

                <GizmoHelper alignment="bottom-right">
                    <GizmoViewport />
                </GizmoHelper>

                {entities.map((entity, index) => (
                    <EntityRenderer
                        key={entity.id}
                        entity={entity}
                        index={index}
                        selected={index === activeIndex}
                        isDragging={isDragging}
                        setDragging={setDragging}
                        mode={mode}
                    />
                ))}

            </Canvas>

        </div>
    )
}

type EntityRendererProps = {
    entity: any
    index: number
    selected: boolean,
    isDragging: boolean,
    setDragging: (val: boolean) => void,
    mode: "translate" | "rotate" | "scale"
}

function EntityRenderer({
    entity,
    index,
    selected,
    isDragging,
    setDragging,
    mode
}: EntityRendererProps) {

    const dispatch = useAppDispatch()

    const transform = entity.components.find(
        (c: any) => c.class === CLASS.transform
    )

    if(!transform) return null

    const model = entity.components.find(
        (c: any) => c.class === CLASS.model
    )

    const camera = entity.components.find(
        (c: any) => c.class === CLASS.camera
    )

    const text = entity.components.find(
        (c: any) => c.class === CLASS.text
    )

    const groupRef = useRef<THREE.Group>(null)

    const quaternion = useMemo(() => {

        return new THREE.Quaternion(
            transform.rotation.x,
            transform.rotation.y,
            transform.rotation.z,
            transform.rotation.w || 1
        )

    }, [transform.rotation])

    const debounce = useRef(createDebounce(() => {
        const obj = groupRef.current
        if(!obj) return

        dispatch(updateComponent({
            index,
            componentIndex:
                entity.components.findIndex(
                    (c: any) =>
                        c.class === CLASS.transform
                ),

            path: ["position"],
            value: {
                x: obj.position.x,
                y: obj.position.y,
                z: obj.position.z
            }
        }))

        dispatch(updateComponent({
            index,
            componentIndex:
                entity.components.findIndex(
                    (c: any) =>
                        c.class === CLASS.transform
                ),

            path: ["rotation"],
            value: {
                x: obj.quaternion.x,
                y: obj.quaternion.y,
                z: obj.quaternion.z,
                w: obj.quaternion.w
            }
        }))

        dispatch(updateComponent({
            index,
            componentIndex:
                entity.components.findIndex(
                    (c: any) =>
                        c.class === CLASS.transform
                ),

            path: ["scale"],
            value: {
                x: obj.scale.x,
                y: obj.scale.y,
                z: obj.scale.z
            }
        }))
    }, 60/1000))

    return(
        <>

            <group
                ref={groupRef}
                position={[
                    transform.position.x,
                    transform.position.y,
                    transform.position.z
                ]}
                quaternion={quaternion}
                scale={[
                    transform.scale.x || 1,
                    transform.scale.y || 1,
                    transform.scale.z || 1
                ]}
                onClick={(e) => {
                    e.stopPropagation()
                    if(isDragging) return
                    dispatch(setActiveEntity(index))
                }}
            >

                {/* Model */}

                {model && (
                    <ModelRenderer
                        path={model.assetPath}
                    />
                )}

                {/* Camera Frustum */}

                {camera && (
                    <CameraFrustum
                        fov={camera.fov}
                        near={camera.near}
                        far={camera.far}
                    />
                )}

                

            </group>

            {/* Text */}

                {text && (
                    <Text
                        position={[
                            transform.position.x + text.offsetPosition.x,
                            transform.position.y + text.offsetPosition.y,
                            transform.position.z + text.offsetPosition.z
                        ]}
                        fontSize={text.scale * 1}
                        anchorX={
                            text.centered
                                ? "center"
                                : "left"
                        }
                    >
                        {text.text}
                    </Text>
                )}

            {/* Transform Controls */}

            {selected && groupRef.current && (
                <TransformControls
                    object={groupRef.current}
                    mode={mode}

                    onClick={e => {
                        e.stopPropagation()
                    }}

                    onMouseUp={_e => {
                        setDragging(false)
                    }}

                    onMouseDown={_e => {
                        setDragging(true)
                    }}

                    onObjectChange={() => {

                        const obj = groupRef.current
                        if(!obj) return

                        debounce.current()
                        return
                    }}
                />
            )}

        </>
    )
}

type ModelRendererProps = {
    path: string
}

function ModelRenderer({
    path
}: ModelRendererProps) {

    const {
        gltf,
        loading,
        error
    } = useGLTFDisk(path)

    if(loading) {
        return null
    }

    if(error || !gltf) {

        return(
            <mesh>
                <boxGeometry />
                <meshStandardMaterial
                    color="red"
                />
            </mesh>
        )
    }

    return(
        <primitive object={gltf.scene} />
    )
}

type CameraFrustumProps = {
    fov: number
    near: number
    far: number
}

function CameraFrustum({
    fov,
    near,
    far
}: CameraFrustumProps) {

    const camera = useMemo(() => {

        const cam =
            new THREE.PerspectiveCamera(
                fov,
                1,
                near,
                far
            )

        cam.updateProjectionMatrix()

        return cam

    }, [fov, near, far])

    const helper = useMemo(() => {
        return new THREE.CameraHelper(camera)
    }, [camera])

    return(
        <primitive object={helper} />
    )
}