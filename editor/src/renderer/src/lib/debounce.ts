export function createDebounce(cb: () => void, ms: number) {

    let timeout = setTimeout(cb, ms)
    clearTimeout(timeout)
    
    return () => {
        
        clearTimeout(timeout)
        timeout = setTimeout(cb, ms)
    }

}