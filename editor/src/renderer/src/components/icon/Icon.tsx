type IconProps = {
    children: React.ReactNode,
    className?: string
}

export default function Icon({
    children,
    className
}: IconProps) {
    return(
        <span className={["material-symbols-outlined", className ?? ""].join(" ")}>{children}</span>
    )
}