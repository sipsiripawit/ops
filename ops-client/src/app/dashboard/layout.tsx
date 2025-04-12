// src/app/dashboard/layout.tsx
export default function DashboardLayout({
                                            children,
                                        }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            {/* Your dashboard layout content */}
            {children}
        </div>
    );
}
