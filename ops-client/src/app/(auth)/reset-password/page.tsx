'use client';

import { useState, useEffect } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import Image from 'next/image';
import { Eye, EyeOff, Lock } from 'lucide-react';
import { authService } from '@/services/auth-service';

export default function ResetPasswordPage() {
    const searchParams = useSearchParams();
    const router = useRouter();
    const [token, setToken] = useState<string>('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        // Get token from URL
        const tokenParam = searchParams.get('token');
        if (tokenParam) {
            setToken(tokenParam);
        } else {
            setError('ไม่พบโทเค็นสำหรับรีเซ็ตรหัสผ่าน');
        }
    }, [searchParams]);

    const validatePassword = (password: string): boolean => {
        // ตรวจสอบว่ารหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร
        if (password.length < 8) {
            setError('รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร');
            return false;
        }

        // ตรวจสอบว่ารหัสผ่านต้องมีตัวอักษรพิมพ์ใหญ่อย่างน้อย 1 ตัว
        if (!/[A-Z]/.test(password)) {
            setError('รหัสผ่านต้องมีตัวอักษรพิมพ์ใหญ่อย่างน้อย 1 ตัว');
            return false;
        }

        // ตรวจสอบว่ารหัสผ่านต้องมีตัวเลขอย่างน้อย 1 ตัว
        if (!/[0-9]/.test(password)) {
            setError('รหัสผ่านต้องมีตัวเลขอย่างน้อย 1 ตัว');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Reset error
        setError('');

        // ตรวจสอบว่ามีโทเค็นหรือไม่
        if (!token) {
            setError('ไม่พบโทเค็นสำหรับรีเซ็ตรหัสผ่าน');
            return;
        }

        // ตรวจสอบว่ากรอกรหัสผ่านหรือไม่
        if (!password) {
            setError('กรุณากรอกรหัสผ่านใหม่');
            return;
        }

        // ตรวจสอบว่ารหัสผ่านและยืนยันรหัสผ่านตรงกันหรือไม่
        if (password !== confirmPassword) {
            setError('รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน');
            return;
        }

        // ตรวจสอบความซับซ้อนของรหัสผ่าน
        if (!validatePassword(password)) {
            // Error is set in validatePassword function
            return;
        }

        try {
            setIsLoading(true);

            // เรียกใช้ resetPassword จาก authService
            const response = await authService.resetPassword(token, password);

            if (response.success) {
                setSuccess(true);
            } else {
                setError(response.message || 'เกิดข้อผิดพลาดในการรีเซ็ตรหัสผ่าน');
            }
        } catch (err: any) {
            console.error('Password reset error:', err);
            setError('เกิดข้อผิดพลาดในการรีเซ็ตรหัสผ่าน กรุณาลองใหม่อีกครั้ง');
        } finally {
            setIsLoading(false);
        }
    };

    if (success) {
        return (
            <div className="flex h-screen w-full">
                <div className="w-full md:w-1/2 flex flex-col justify-between p-8">
                    <div className="mb-16">
                        <div className="flex items-center">
                            <Image
                                src="/images/logo.svg"
                                alt="YIP Logo"
                                width={40}
                                height={40}
                                className="mr-2"
                            />
                            <h1 className="text-2xl font-bold text-red-600">YIP E-INVENTORY</h1>
                        </div>
                    </div>

                    <div className="flex-grow flex flex-col justify-center max-w-md">
                        <div className="bg-green-50 text-green-600 p-6 rounded-md mb-6">
                            <h2 className="text-xl font-semibold mb-4">รีเซ็ตรหัสผ่านสำเร็จ</h2>
                            <p className="mb-4">
                                รหัสผ่านของคุณได้รับการเปลี่ยนแปลงเรียบร้อยแล้ว คุณสามารถใช้รหัสผ่านใหม่เพื่อเข้าสู่ระบบได้ทันที
                            </p>
                            <div className="mt-6">
                                <Link
                                    href="/login"
                                    className="inline-block px-4 py-2 bg-blue-900 text-white rounded-md hover:bg-blue-800"
                                >
                                    ไปยังหน้าเข้าสู่ระบบ
                                </Link>
                            </div>
                        </div>
                    </div>

                    {/* Footer */}
                    <div className="text-xs text-gray-500 py-4">
                        Version 0.0.0 | ปรับปรุงล่าสุด 2568-01-31 09:38
                    </div>
                </div>

                {/* Right Section - Background Image */}
                <div className="hidden md:block md:w-1/2 bg-blue-900 relative overflow-hidden">
                    <div className="absolute inset-0 flex items-center justify-center">
                        <div className="w-full h-full bg-blue-900 flex items-center justify-center">
                            {/* 3D Illustrations (same as login page) */}
                            <div className="relative">
                                {/* Big blue circle */}
                                <div className="w-96 h-96 bg-blue-800 bg-opacity-70 rounded-full absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"></div>

                                {/* 3D Elements */}
                                <div className="relative z-10 mb-24">
                                    <svg width="150" height="150" viewBox="0 0 150 150" className="transform -translate-y-5">
                                        <g stroke="white" strokeWidth="2" fill="none">
                                            <path d="M75,30 L120,60 L120,120 L75,150 L30,120 L30,60 Z" fill="rgba(0,0,150,0.1)" />
                                            <path d="M75,30 L75,90 L120,120 L120,60 Z" fill="rgba(255,255,255,0.1)" />
                                            <path d="M75,30 L75,90 L30,120 L30,60 Z" fill="rgba(0,0,50,0.2)" />
                                            <path d="M75,90 L120,120 L75,150 L30,120 Z" fill="rgba(0,0,100,0.1)" />
                                            <path d="M75,30 L120,60 M120,60 L120,120 M120,120 L75,150 M75,150 L30,120 M30,120 L30,60 M30,60 L75,30 M75,30 L75,90 M75,90 L120,120 M75,90 L30,120" />
                                        </g>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="flex h-screen w-full">
            {/* Left Section - Form */}
            <div className="w-full md:w-1/2 flex flex-col justify-between p-8">
                {/* Logo */}
                <div className="mb-16">
                    <div className="flex items-center">
                        <Image
                            src="/images/logo.svg"
                            alt="YIP Logo"
                            width={40}
                            height={40}
                            className="mr-2"
                        />
                        <h1 className="text-2xl font-bold text-red-600">YIP E-INVENTORY</h1>
                    </div>
                </div>

                {/* Reset Password Form */}
                <div className="flex-grow flex flex-col justify-center max-w-md">
                    <h2 className="text-2xl font-bold mb-6">รีเซ็ตรหัสผ่าน</h2>
                    <p className="mb-6 text-gray-600">
                        กรุณากำหนดรหัสผ่านใหม่สำหรับบัญชีของคุณ
                    </p>

                    <form onSubmit={handleSubmit}>
                        {error && (
                            <div className="bg-red-50 text-red-500 p-3 rounded-md mb-4">
                                {error}
                            </div>
                        )}

                        <div className="mb-6">
                            <label htmlFor="password" className="block text-sm font-medium mb-2">
                                รหัสผ่านใหม่
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <Lock size={18} />
                </span>
                                <input
                                    id="password"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="••••••••"
                                    disabled={isLoading}
                                />
                                <button
                                    type="button"
                                    className="absolute inset-y-0 right-3 flex items-center"
                                    onClick={() => setShowPassword(!showPassword)}
                                    disabled={isLoading}
                                >
                                    {showPassword ? (
                                        <EyeOff size={18} className="text-gray-400" />
                                    ) : (
                                        <Eye size={18} className="text-gray-400" />
                                    )}
                                </button>
                            </div>
                            <p className="text-xs text-gray-500 mt-1">
                                รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร และประกอบด้วยตัวอักษรพิมพ์ใหญ่อย่างน้อย 1 ตัว และตัวเลขอย่างน้อย 1 ตัว
                            </p>
                        </div>

                        <div className="mb-6">
                            <label htmlFor="confirm-password" className="block text-sm font-medium mb-2">
                                ยืนยันรหัสผ่านใหม่
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <Lock size={18} />
                </span>
                                <input
                                    id="confirm-password"
                                    type={showPassword ? "text" : "password"}
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    className="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="••••••••"
                                    disabled={isLoading}
                                />
                                <button
                                    type="button"
                                    className="absolute inset-y-0 right-3 flex items-center"
                                    onClick={() => setShowPassword(!showPassword)}
                                    disabled={isLoading}
                                >
                                    {showPassword ? (
                                        <EyeOff size={18} className="text-gray-400" />
                                    ) : (
                                        <Eye size={18} className="text-gray-400" />
                                    )}
                                </button>
                            </div>
                        </div>

                        <div className="flex items-center justify-between mb-6">
                            <Link href="/login" className="text-sm text-blue-600 hover:text-blue-800">
                                กลับไปยังหน้าเข้าสู่ระบบ
                            </Link>
                        </div>

                        <button
                            type="submit"
                            className={`w-full py-3 px-4 bg-blue-900 hover:bg-blue-800 text-white font-medium rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                                isLoading ? 'opacity-70 cursor-not-allowed' : ''
                            }`}
                            disabled={isLoading}
                        >
                            {isLoading ? 'กำลังรีเซ็ตรหัสผ่าน...' : 'รีเซ็ตรหัสผ่าน'}
                        </button>
                    </form>
                </div>

                {/* Footer */}
                <div className="text-xs text-gray-500 py-4">
                    Version 0.0.0 | ปรับปรุงล่าสุด 2568-01-31 09:38
                </div>
            </div>

            {/* Right Section - Background Image */}
            <div className="hidden md:block md:w-1/2 bg-blue-900 relative overflow-hidden">
                <div className="absolute inset-0 flex items-center justify-center">
                    <div className="w-full h-full bg-blue-900 flex items-center justify-center">
                        {/* 3D Illustrations (same as login page) */}
                        <div className="relative">
                            {/* Big blue circle */}
                            <div className="w-96 h-96 bg-blue-800 bg-opacity-70 rounded-full absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"></div>

                            {/* 3D Elements */}
                            <div className="relative z-10 mb-24">
                                <svg width="150" height="150" viewBox="0 0 150 150" className="transform -translate-y-5">
                                    <g stroke="white" strokeWidth="2" fill="none">
                                        <path d="M75,30 L120,60 L120,120 L75,150 L30,120 L30,60 Z" fill="rgba(0,0,150,0.1)" />
                                        <path d="M75,30 L75,90 L120,120 L120,60 Z" fill="rgba(255,255,255,0.1)" />
                                        <path d="M75,30 L75,90 L30,120 L30,60 Z" fill="rgba(0,0,50,0.2)" />
                                        <path d="M75,90 L120,120 L75,150 L30,120 Z" fill="rgba(0,0,100,0.1)" />
                                        <path d="M75,30 L120,60 M120,60 L120,120 M120,120 L75,150 M75,150 L30,120 M30,120 L30,60 M30,60 L75,30 M75,30 L75,90 M75,90 L120,120 M75,90 L30,120" />
                                    </g>
                                </svg>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
