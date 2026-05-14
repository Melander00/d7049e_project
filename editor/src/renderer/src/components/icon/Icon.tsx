type IconProps = {
    children: React.ReactNode
}

export default function Icon({
    children
}: IconProps) {
    return(
        <span className="material-symbols-outlined">{children}</span>
    )
}