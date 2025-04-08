import { API_BASE_URL, AUTH_CONFIG } from '@/constants/config';

interface ApiResponse<T> {
    success: boolean;
    message: string;
    data?: T;
}

interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    user: {
        id: number;
        username: string;
        role: string[];
        permissions: string[];
    };
}

interface RefreshTokenResponse {
    accessToken: string;
    refreshToken: string;
}

// Helper function สำหรับจัดการ API Requests
const apiRequest = async <T>(url: string, options: RequestInit = {}): Promise<ApiResponse<T>> => {
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

        const data = await response.json();

        if (!response.ok) {
            // ตรวจสอบ token หมดอายุ
            if (response.status === 401) {
                // Token หมดอายุหรือไม่ถูกต้อง
                localStorage.removeItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
            }

            return {
                success: false,
                message: data.message || 'Network response was not ok',
            };
        }

        return {
            success: true,
            message: data.message || 'Success',
            data: data.data,
        };
    } catch (error) {
        console.error('API Request Error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Unknown error occurred',
        };
    }
};

export const authService = {
    // เข้าสู่ระบบ
    login: async (username: string, password: string, rememberMe = false): Promise<ApiResponse<LoginResponse>> => {
        return apiRequest<LoginResponse>('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password, rememberMe }),
        });
    },

    // ออกจากระบบ
    logout: async (): Promise<ApiResponse<void>> => {
        return apiRequest<void>('/auth/logout', {
            method: 'POST',
        });
    },

    // สร้าง Access Token ใหม่จาก Refresh Token
    refreshToken: async (refreshToken: string): Promise<ApiResponse<RefreshTokenResponse>> => {
        return apiRequest<RefreshTokenResponse>('/auth/refresh-token', {
            method: 'POST',
            body: JSON.stringify({ refreshToken }),
            headers: {
                // ไม่ใช้ Authorization header สำหรับการ refresh token
                Authorization: '',
            },
        });
    },

    // ดึงข้อมูลผู้ใช้
    getProfile: async (): Promise<ApiResponse<any>> => {
        return apiRequest('/auth/profile', {
            method: 'GET',
        });
    },

    // ตรวจสอบ Token
    validateToken: async (token: string): Promise<ApiResponse<boolean>> => {
        return apiRequest('/auth/validate-token', {
            method: 'POST',
            body: JSON.stringify({ token }),
        });
    },

    // ลืมรหัสผ่าน
    forgotPassword: async (username: string): Promise<ApiResponse<void>> => {
        return apiRequest('/auth/forgot-password', {
            method: 'POST',
            body: JSON.stringify({ username }),
        });
    },

    // เปลี่ยนรหัสผ่าน
    resetPassword: async (token: string, password: string): Promise<ApiResponse<void>> => {
        return apiRequest('/auth/reset-password', {
            method: 'POST',
            body: JSON.stringify({ token, password }),
        });
    },
};

export default authService;
