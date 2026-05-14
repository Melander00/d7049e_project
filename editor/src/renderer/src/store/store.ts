import type { Action, PayloadAction, ThunkAction } from '@reduxjs/toolkit'
import { combineReducers, configureStore } from '@reduxjs/toolkit'
import { createDebounce } from '@renderer/lib/debounce'
import { saveProject } from '@renderer/lib/project/project'
import configReducer from './features/configSlice'
import entitiesReducer from './features/entitiesSlice'

const appReducer = combineReducers({
    entities: entitiesReducer,
    config: configReducer
})

export type AppReducerReturn = ReturnType<typeof appReducer>

// @ts-ignore
const rootReducer: typeof appReducer = (state, action) => {
    if(action.type === "project/load") {
        return action.payload
    }
    return appReducer(state, action)
} 

export const store = configureStore({
  reducer: rootReducer
})

export const loadProjectAction = (state): PayloadAction => ({
    type: "project/load",
    payload: state
})


const saveDebounce = createDebounce(() => {
    saveProject()
}, 500)

// Auto-save feature
store.subscribe(() => {
    saveDebounce()
})

// Infer the type of `store`
export type AppStore = typeof store
export type RootState = ReturnType<AppStore['getState']>
// Infer the `AppDispatch` type from the store itself
export type AppDispatch = AppStore['dispatch']
// Define a reusable type describing thunk functions
export type AppThunk<ThunkReturnType = void> = ThunkAction<
  ThunkReturnType,
  RootState,
  unknown,
  Action
>