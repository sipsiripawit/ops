'use client';

import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { authService } from '@/services/auth-service';
import { API_BASE_URL, AUTH_CONFIG } from '@/constants/config';

export interface User {
    id: number;
    username: string;
    role: string[];
    permissions: string[];
}

export interface AuthContextType {
    user: User | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (username: string, password: string, rememberMe?: boolean) => Promise<void>;
    logout: () => Promise<void>;
    refreshToken: () => Promise<string>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const router = useRouter();
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    // ดึงข้อมูลผู้ใช้จาก Token ที่บันทึกไว้
    const fetchUser = async () => {
        try {
            setIsLoading(true);
            // เรียกใช้ API เพื่อดึงข้อมูลผู้ใช้จาก token
            const response = await authService.getProfile();

            if (response.success) {
                setUser(response.data);
            } else {
                setUser(null);
                // ลบ Token ใน localStorage หากมีข้อผิดพลาด
                localStorage.removeItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
                localStorage.removeItem(AUTH_CONFIG.REFRESH_TOKEN_KEY);
            }
        } catch (error) {
            console.error('Error fetching user:', error);
            setUser(null);
            // ลบ Token ใน localStorage หากมีข้อผิดพลาด
            localStorage.removeItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
            localStorage.removeItem(AUTH_CONFIG.REFRESH_TOKEN_KEY);
        } finally {
            setIsLoading(false);
        }
    };

    // เช็ค token และดึงข้อมูลผู้ใช้เมื่อ component mount
    useEffect(() => {
        const token = localStorage.getItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
        if (token) {
            fetchUser();
        } else {
            setIsLoading(false);
        }
    }, []);

    // Function สำหรับ Login
    const login = async (username: string, password: string, rememberMe = false) => {
        setIsLoading(true);
        try {
            // เรียกใช้ API สำหรับ Login
            const response = await authService.login(username, password);

            if (response.success) {
                const { accessToken, refreshToken, user } = response.data;

                // บันทึก Token ใน localStorage
                localStorage.setItem(AUTH_CONFIG.ACCESS_TOKEN_KEY, accessToken);

                // บันทึก Refresh Token เฉพาะเมื่อ rememberMe = true
                if (rememberMe) {
                    localStorage.setItem(AUTH_CONFIG.REFRESH_TOKEN_KEY, refreshToken);
                }

                setUser(user);
            } else {
                throw new Error(response.message);
            }
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };

    // Function สำหรับ Logout
    const logout = async () => {
        try {
            await authService.logout();
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            // ลบ Token และข้อมูลผู้ใช้
            localStorage.removeItem(AUTH_CONFIG.ACCESS_TOKEN_KEY);
            localStorage.removeItem(AUTH_CONFIG.REFRESH_TOKEN_KEY);
            setUser(null);
            router.push('/login');
        }
    };

    // Function สำหรับ Refresh Token
    const refreshToken = async (): Promise<string> => {
        const storedRefreshToken = localStorage.getItem(AUTH_CONFIG.REFRESH_TOKEN_KEY);
        if (!storedRefreshToken) {
            throw new Error('No refresh token available');
        }

        try {
            const response = await authService.refreshToken(storedRefreshToken);

            if (response.success) {
                const { accessToken, refreshToken } = response.data;
                localStorage.setItem(AUTH_CONFIG.ACCESS_TOKEN_KEY, accessToken);
                localStorage.setItem(AUTH_CONFIG.REFRESH_TOKEN_KEY, refreshToken);
                return accessToken;
            } else {
                throw new Error(response.message);
            }
        } catch (error) {
            console.error('Token refresh error:', error);
            logout();
            throw error;
        }
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                isAuthenticated: !!user,
                isLoading,
                login,
                logout,
                refreshToken,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuthContext = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuthContext must be used within an AuthProvider');
    }
    return context;
};
