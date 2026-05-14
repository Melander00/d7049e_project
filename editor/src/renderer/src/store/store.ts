import type { Action, PayloadAction, ThunkAction } from '@reduxjs/toolkit'
import { combineReducers, configureStore } from '@reduxjs/toolkit'
import { saveProject } from '@renderer/lib/project/project'
import entitiesReducer from './features/entitiesSlice'

const appReducer = combineReducers({
    entities: entitiesReducer
})

export const store = configureStore({
  reducer: (state, action) => {
    if(action.type === "project/load") {
        return action.payload
    }
    return appReducer(state, action)
  }
})

export const loadProjectAction = (state): PayloadAction => ({
    type: "project/load",
    payload: state
})


// Auto-save feature
store.subscribe(() => {
    saveProject()
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