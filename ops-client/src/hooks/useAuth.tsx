'use client';

import { useState, useEffect, useCallback, createContext, useContext } from 'react';
import { useRouter } from 'next/navigation';
import { authService } from '@/services/auth-service';

interface User {
    id: string;
    username: string;
    role: string;
    permissions: string[];
}

interface AuthContextType {
    user: User | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (username: string, password: string, rememberMe?: boolean) => Promise<void>;
    logout: () => Promise<void>;
    refreshToken: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const router = useRouter();
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    // ดึงข้อมูลผู้ใช้จาก Token ที่บันทึกไว้
    const fetchUser = useCallback(async () => {
        try {
            setIsLoading(true);
            // เรียกใช้ API เพื่อดึงข้อมูลผู้ใช้จาก token
            const userData = await authService.getProfile();
            setUser(userData);
        } catch (error) {
            console.error('Error fetching user:', error);
            setUser(null);
            // ลบ Token ใน localStorage หากมีข้อผิดพลาด
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
        } finally {
            setIsLoading(false);
        }
    }, []);

    // เช็ค token และดึงข้อมูลผู้ใช้เมื่อ component mount
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            fetchUser();
        } else {
            setIsLoading(false);
        }
    }, [fetchUser]);

    // Function สำหรับ Login
    const login = async (username: string, password: string, rememberMe = false) => {
        setIsLoading(true);
        try {
            // เรียกใช้ API สำหรับ Login
            const { accessToken, refreshToken, user } = await authService.login(username, password);

            // บันทึก Token ใน localStorage
            localStorage.setItem('accessToken', accessToken);

            // บันทึก Refresh Token เฉพาะเมื่อ rememberMe = true
            if (rememberMe) {
                localStorage.setItem('refreshToken', refreshToken);
            }

            setUser(user);
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
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            setUser(null);
            router.push('/login');
        }
    };

    // Function สำหรับ Refresh Token
    const refreshToken = async () => {
        const storedRefreshToken = localStorage.getItem('refreshToken');
        if (!storedRefreshToken) {
            throw new Error('No refresh token available');
        }

        try {
            const { accessToken, refreshToken: newRefreshToken } = await authService.refreshToken(storedRefreshToken);
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', newRefreshToken);
            return accessToken;
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

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export default useAuth;