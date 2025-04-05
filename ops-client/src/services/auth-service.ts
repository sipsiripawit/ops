import { API_BASE_URL } from '@/constants/config';

interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    user: {
        id: string;
        username: string;
        role: string;
        permissions: string[];
    };
}

interface RefreshTokenResponse {
    accessToken: string;
    refreshToken: string;
}

interface ApiError {
    message: string;
    statusCode: number;
}

// Helper function สำหรับจัดการ API Requests
const apiRequest = async <T>(url: string, options: RequestInit = {}): Promise<T> => {
    const token = localStorage.getItem('accessToken');

    const headers: HeadersInit = {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...(options.headers || {}),
    };

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        const errorData: ApiError = await response.json();
        throw new Error(errorData.message || 'Network response was not ok');
    }

    return response.json();
};

export const authService = {
    // เข้าสู่ระบบ
    login: async (username: string, password: string): Promise<LoginResponse> => {
        return apiRequest<LoginResponse>('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        });
    },

    // ออกจากระบบ
    logout: async (): Promise<void> => {
        return apiRequest<void>('/auth/logout', {
            method: 'POST',
        });
    },

    // สร้าง Access Token ใหม่จาก Refresh Token
    refreshToken: async (refreshToken: string): Promise<RefreshTokenResponse> => {
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
    getProfile: async () => {
        return apiRequest('/auth/profile', {
            method: 'GET',
        });
    },

    // ตรวจสอบ Token
    validateToken: async (token: string) => {
        return apiRequest('/auth/validate-token', {
            method: 'POST',
            body: JSON.stringify({ token }),
        });
    },

    // ลืมรหัสผ่าน
    forgotPassword: async (username: string) => {
        return apiRequest('/auth/forgot-password', {
            method: 'POST',
            body: JSON.stringify({ username }),
        });
    },

    // เปลี่ยนรหัสผ่าน
    resetPassword: async (token: string, password: string) => {
        return apiRequest('/auth/reset-password', {
            method: 'POST',
            body: JSON.stringify({ token, password }),
        });
    },
};

export default authService;