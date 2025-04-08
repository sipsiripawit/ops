'use client';

import { useEffect, ReactNode } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';

interface AuthGuardProps {
    children: ReactNode;
    allowedRoles?: string[];
}

/**
 * AuthGuard component ที่ใช้ป้องกันหน้าที่ต้องการการยืนยันตัวตน
 * หรือตรวจสอบบทบาทของผู้ใช้
 *
 * @param {ReactNode} children - Content ที่จะแสดงเมื่อผ่านการตรวจสอบ
 * @param {string[]} allowedRoles - บทบาทที่อนุญาตให้เข้าถึงได้ (optional)
 */
export function AuthGuard({ children, allowedRoles }: AuthGuardProps) {
    const { isAuthenticated, user, isLoading } = useAuth();
    const router = useRouter();
    const pathname = usePathname();

    useEffect(() => {
        // ถ้ายังกำลังโหลดข้อมูลผู้ใช้อยู่ ไม่ต้องทำอะไร
        if (isLoading) return;

        // ถ้าไม่ได้ login ให้ redirect ไปหน้า login
        if (!isAuthenticated) {
            router.push(`/login?from=${encodeURIComponent(pathname)}`);
            return;
        }

        // ตรวจสอบบทบาทถ้ามีการระบุ allowedRoles
        if (allowedRoles && allowedRoles.length > 0) {
            // ตรวจสอบว่าผู้ใช้มีบทบาทที่อนุญาตหรือไม่
            const hasAllowedRole = user?.role.some(role => allowedRoles.includes(role));

            if (!hasAllowedRole) {
                // ถ้าไม่มีสิทธิ์ redirect ไปหน้า unauthorized หรือหน้าแรก
                router.push('/unauthorized');
            }
        }
    }, [isAuthenticated, isLoading, router, pathname, user, allowedRoles]);

    // แสดง loading state หรือ null ระหว่างที่รอตรวจสอบ
    if (isLoading || !isAuthenticated) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="animate-pulse text-center">
                    <div className="text-xl font-medium">กำลังโหลด...</div>
                    <div className="text-gray-500 mt-2">กรุณารอสักครู่</div>
                </div>
            </div>
        );
    }

    // ถ้ามีการตรวจสอบบทบาทและผู้ใช้ไม่มีสิทธิ์ จะไม่แสดงเนื้อหา
    if (allowedRoles && allowedRoles.length > 0) {
        const hasAllowedRole = user?.role.some(role => allowedRoles.includes(role));
        if (!hasAllowedRole) return null;
    }

    // แสดง content เมื่อผู้ใช้มี authentication และมีสิทธิ์เข้าถึง
    return <>{children}</>;
}

export default AuthGuard;
