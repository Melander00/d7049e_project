export interface SaveData {
    filepath: string[],
    data: string
}

export interface Asset {
    isDir: boolean,
    path: string
}

export interface RenameRequest {
    from: string,
    to: string,
    path: string[]
}

export interface CreateFileRequest {
    path: string[],
    filename: string,
    content: string
}

export interface ReadFileRequest {
    path: string[],
    filename: string,
}