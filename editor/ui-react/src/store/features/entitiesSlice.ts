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
    setEntityName,
    setEntityTag,
    setActiveEntity,
    addComponent,
    removeComponent
} = entitiesSlice.actions
export default entitiesSlice.reducer