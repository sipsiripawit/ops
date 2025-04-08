import { jwtDecode } from 'jwt-decode';
import { AUTH_CONFIG } from '@/constants/config';

interface JwtPayload {
    sub: string;
    exp: number;
    iat: number;
    roles?: string[];
    permissions?: string[];
    [key: string]: any;
}

/**
 * ตรวจสอบว่า token หมดอายุหรือไม่
 *
 * @param token JWT token ที่ต้องการตรวจสอบ
 * @returns `true` ถ้า token หมดอายุแล้ว, `false` ถ้ายังไม่หมดอายุ
 */
export function isTokenExpired(token: string): boolean {
    try {
        const decoded = jwtDecode<JwtPayload>(token);
        const currentTime = Date.now() / 1000;

        // เพิ่ม buffer time เพื่อป้องกันการหมดอายุระหว่างการทำงาน
        return decoded.exp < currentTime + (AUTH_CONFIG.TOKEN_EXPIRY_BUFFER / 1000);
    } catch (error) {
        console.error('Error decoding token:', error);
        return true; // ถ้ามีข้อผิดพลาดให้ถือว่าหมดอายุแล้ว
    }
}

/**
 * ดึงข้อมูลจาก JWT token
 *
 * @param token JWT token
 * @returns ข้อมูลที่อยู่ใน token payload
 */
export function getTokenPayload(token: string): JwtPayload | null {
    try {
        return jwtDecode<JwtPayload>(token);
    } catch (error) {
        console.error('Error decoding token:', error);
        return null;
    }
}

/**
 * ตรวจสอบว่าผู้ใช้มีสิทธิ์หรือมีบทบาทที่ต้องการหรือไม่
 *
 * @param requiredRoles บทบาทที่ต้องการตรวจสอบ
 * @param userRoles บทบาทของผู้ใช้
 * @returns `true` ถ้าผู้ใช้มีบทบาทที่ต้องการอย่างน้อย 1 บทบาท, `false` ถ้าไม่มี
 */
export function hasRequiredRoles(requiredRoles: string[], userRoles: string[]): boolean {
    if (!requiredRoles || requiredRoles.length === 0) return true;
    if (!userRoles || userRoles.length === 0) return false;

    return requiredRoles.some(role => userRoles.includes(role));
}

/**
 * ตรวจสอบว่าผู้ใช้มีสิทธิ์ที่ต้องการหรือไม่
 *
 * @param requiredPermissions สิทธิ์ที่ต้องการตรวจสอบ
 * @param userPermissions สิทธิ์ของผู้ใช้
 * @returns `true` ถ้าผู้ใช้มีสิทธิ์ที่ต้องการทั้งหมด, `false` ถ้าไม่มี
 */
export function hasRequiredPermissions(
    requiredPermissions: string[],
    userPermissions: string[]
): boolean {
    if (!requiredPermissions || requiredPermissions.length === 0) return true;
    if (!userPermissions || userPermissions.length === 0) return false;

    return requiredPermissions.every(permission => userPermissions.includes(permission));
}

/**
 * ตรวจสอบว่ามี token ที่ใช้ได้อยู่หรือไม่
 *
 * @returns `true` ถ้ามี token ที่ยังไม่หมดอายุ, `false` ถ้าไม่มีหรือหมดอายุแล้ว
 */
export function hasValidToken(): boolean {
    try {
        const token = localStorage.getItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
        if (!token) return false;

        return !isTokenExpired(token);
    } catch (error) {
        return false;
    }
}

/**
 * ลบข้อมูล authentication ทั้งหมดออกจาก local storage
 */
export function clearAuthData(): void {
    localStorage.removeItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
    localStorage.removeItem(AUTH_CONFIG.REFRESH_TOKEN_KEY);
}

export default {
    isTokenExpired,
    getTokenPayload,
    hasRequiredRoles,
    hasRequiredPermissions,
    hasValidToken,
    clearAuthData
};
