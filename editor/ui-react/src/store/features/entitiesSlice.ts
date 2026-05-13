import { createSlice, type PayloadAction } from "@reduxjs/toolkit"

export interface Entity {
    id: string,
    name: string,
    tag: string,
    components: any[]
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
        addComponent: (state, action: PayloadAction<{index: number, component: any}>) => {
            state.entities[action.payload.index].components.push(action.payload.component)
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
    removeComponent
} = entitiesSlice.actions
export default entitiesSlice.reducer