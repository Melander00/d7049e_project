import { createSlice, type PayloadAction } from "@reduxjs/toolkit"

export interface EntityComponent {
    class: string,
    [field: string]: any
}

export interface Entity {
    id: string,
    name: string,
    tag: string,
    components: EntityComponent[]
}

function newEntity(): Entity {
    return {   
        id: crypto.randomUUID(),
        name: "Entity",
        tag: "",
        components: [],
    }
}


export interface EntitiesState {
    entities: Entity[],
    activeIndex: number 
}

const initialState: EntitiesState = {
    entities: [],
    activeIndex: -1
}

export const entitiesSlice = createSlice({
    name: "entities",
    initialState,
    reducers: {
        createEntity: state => {
            state.entities.push(newEntity())
            state.activeIndex = state.entities.length - 1
        },
        removeEntity: (state, action: PayloadAction<number>) => {
            state.entities.splice(action.payload, 1)
            if(state.activeIndex > state.entities.length - 1) {
                state.activeIndex = state.entities.length - 1
            }
        },
        duplicateEntity: (state, action: PayloadAction<number>) => {
            const entity = state.entities[action.payload]
            const cloned = JSON.parse(JSON.stringify(entity))
            cloned.id = crypto.randomUUID()
            state.entities.splice(action.payload, 0, cloned)
        },
        setEntityName: (state, action: PayloadAction<{index: number, name: string}>) => {
            state.entities[action.payload.index].name = action.payload.name
        },
        setEntityTag: (state, action: PayloadAction<{index: number, tag: string}>) => {
            state.entities[action.payload.index].tag = action.payload.tag
        },
        setActiveEntity: (state, action: PayloadAction<number>) => {
            state.activeIndex = action.payload
        },
        addComponent: (state, action: PayloadAction<{index: number, component: EntityComponent}>) => {
            state.entities[action.payload.index].components.push(action.payload.component)
        },
        updateComponent: (
            state,
            action: PayloadAction<{
                index: number,
                componentIndex: number,
                path: string[],
                value: any
            }>
        ) => {

            const component = state.entities[action.payload.index].components[action.payload.componentIndex]

            let target: any = component

            for(let i = 0; i < action.payload.path.length - 1; i++) {
                target = target[action.payload.path[i]]
            }

            const finalKey = action.payload.path[action.payload.path.length - 1]

            target[finalKey] = action.payload.value
        },
        removeComponent: (state, action: PayloadAction<{index: number, componentIndex: number}>) => {
            state.entities[action.payload.index].components.splice(action.payload.componentIndex, 1)
        }
    }
})

export const { 
    createEntity,
    removeEntity,
    duplicateEntity,
    setEntityName,
    setEntityTag,
    setActiveEntity,
    addComponent,
    updateComponent,
    removeComponent
} = entitiesSlice.actions
export default entitiesSlice.reducer