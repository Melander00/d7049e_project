import { useIpc } from "@renderer/lib/ipc/hooks"
import { Channels } from "@shared/channels"
import { Asset } from "@shared/ipc"
import { useState } from "react"

export default function AssetManagerView() {

    const [assets, setAssets] = useState<Asset[]>([])

    useIpc(Channels.ASSETS, (_ev, assets: Asset[]) => {
        setAssets(assets)
    })

    return(
        <>
        <div></div>
        <pre>{JSON.stringify(assets, null, 2)}</pre>
        </>
    )
}