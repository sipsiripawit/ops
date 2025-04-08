import { API_BASE_URL, AUTH_CONFIG } from '@/constants/config';

export interface ApiResponse<T = any> {
    success: boolean;
    message: string;
    data?: T;
}

/**
 * ทำ API request ไปยัง backend พร้อมจัดการ token authorization
 * @param url - Endpoint URL (ไม่รวม base URL)
 * @param options - Request options
 * @returns Promise กับผลลัพธ์ที่ได้จาก API
 */
export async function apiRequest<T = any>(
    url: string,
    options: RequestInit = {}
): Promise<ApiResponse<T>> {
    const token = localStorage.getItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);

    const headers: HeadersInit = {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...(options.headers || {}),
    };

    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers,
        });

        // สำหรับคำขอที่ไม่มีข้อมูลตอบกลับ (เช่น DELETE)
        if (response.status === 204) {
            return {
                success: true,
                message: 'Operation successful',
            };
        }

        let data;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            const textData = await response.text();
            try {
                // พยายามแปลงเป็น JSON ถ้าข้อมูลมีรูปแบบ JSON
                data = JSON.parse(textData);
            } catch (e) {
                // ถ้าแปลงไม่ได้ให้ใช้ข้อความเดิม
                data = { message: textData };
            }
        }

        // ตรวจสอบว่า response เป็น success หรือไม่
        if (!response.ok) {
            return {
                success: false,
                message: data.message || `Error: ${response.status} ${response.statusText}`,
                data: data.data,
            };
        }

        return {
            success: true,
            message: data.message || 'Success',
            data: data.data || data,
        };
    } catch (error) {
        console.error('API Request Error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Unknown error occurred',
        };
    }
}

/**
 * Utility function สำหรับจัดการข้อผิดพลาดจาก API
 * @param error - Error object
 * @returns ข้อความข้อผิดพลาดที่เหมาะสมสำหรับแสดงผลให้ผู้ใช้เห็น
 */
export function handleApiError(error: unknown): string {
    if (typeof error === 'string') {
        return error;
    }

    if (error instanceof Error) {
        return error.message;
    }

    if (
        error &&
        typeof error === 'object' &&
        'message' in error &&
        typeof error.message === 'string'
    ) {
        return error.message;
    }

    return 'เกิดข้อผิดพลาดที่ไม่ทราบสาเหตุ กรุณาลองใหม่อีกครั้ง';
}

export default apiRequest;
