type ModalProps = {
    children: React.ReactNode
    shouldShow: boolean,
    closeFn: () => void
}

export default function Modal({
    children,
    shouldShow,
    closeFn
}: ModalProps) {
    return (
        <dialog open={shouldShow} onClose={() => closeFn()}>
            {children}
        </dialog>
    )
}