import { createSlice, PayloadAction } from "@reduxjs/toolkit"

export interface ConfigState {
    fixedTimeFrequency: number,
    debugCollisionWireframe: boolean
}

const initialState: ConfigState = {
    fixedTimeFrequency: 60,
    debugCollisionWireframe: false,
}

export const configSlice = createSlice({
    name: "config",
    initialState,
    reducers: {
        setConfig: (state, action: PayloadAction<ConfigState>) => {
            return action.payload
        }
    }
})

export const {
    setConfig
} = configSlice.actions

export default configSlice.reducer