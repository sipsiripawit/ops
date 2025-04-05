'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import { Eye, EyeOff, User } from 'lucide-react';
import { useAuth } from '@/hooks/useAuth';

export default function LoginPage() {
    const router = useRouter();
    const { login } = useAuth();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!username || !password) {
            setError('กรุณากรอกชื่อผู้ใช้และรหัสผ่าน');
            return;
        }

        try {
            setIsLoading(true);
            setError('');

            // เรียกใช้ login function จาก useAuth hook
            await login(username, password, rememberMe);

            // หากสำเร็จให้ redirect ไปหน้า dashboard
            router.push('/dashboard');
        } catch (err) {
            console.error('Login error:', err);
            setError('ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex h-screen w-full">
            {/* Left Section - Login Form */}
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

                {/* Login Form */}
                <div className="flex-grow flex flex-col justify-center max-w-md">
                    <form onSubmit={handleSubmit}>
                        {error && (
                            <div className="bg-red-50 text-red-500 p-3 rounded-md mb-4">
                                {error}
                            </div>
                        )}

                        <div className="mb-6">
                            <label htmlFor="username" className="block text-sm font-medium mb-2">
                                ชื่อผู้ใช้งาน
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <User size={18} />
                </span>
                                <input
                                    id="username"
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="test.tes"
                                    disabled={isLoading}
                                />
                            </div>
                            <p className="text-xs text-gray-500 mt-1">
                                ชื่อภาษาอังกฤษ ตามด้วย "." และนามสกุล 3 ตัวแรก
                            </p>
                        </div>

                        <div className="mb-6">
                            <label htmlFor="password" className="block text-sm font-medium mb-2">
                                Password
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  🔒
                </span>
                                <input
                                    id="password"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="1234567890"
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
                                เลขรหัสพนักงาน
                            </p>
                        </div>

                        <div className="flex items-center justify-between mb-6">
                            <div className="flex items-center">
                                <input
                                    id="remember-me"
                                    type="checkbox"
                                    checked={rememberMe}
                                    onChange={() => setRememberMe(!rememberMe)}
                                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                                    disabled={isLoading}
                                />
                                <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                                    Remember me
                                </label>
                            </div>
                            <div>
                                <Link href="/forgot-password" className="text-sm text-blue-600 hover:text-blue-800">
                                    ลืมรหัสผ่าน
                                </Link>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className={`w-full py-3 px-4 bg-blue-900 hover:bg-blue-800 text-white font-medium rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                                isLoading ? 'opacity-70 cursor-not-allowed' : ''
                            }`}
                            disabled={isLoading}
                        >
                            {isLoading ? 'กำลังเข้าสู่ระบบ...' : 'Sign in'}
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
                        {/* 3D Illustrations */}
                        <div className="relative">
                            {/* Big blue circle */}
                            <div className="w-96 h-96 bg-blue-800 bg-opacity-70 rounded-full absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"></div>

                            {/* 3D Cube */}
                            <div className="relative z-10 mb-24">
                                <svg width="150" height="150" viewBox="0 0 150 150" className="transform -translate-y-5">
                                    <g stroke="white" strokeWidth="2" fill="none">
                                        {/* Cube */}
                                        <path d="M75,30 L120,60 L120,120 L75,150 L30,120 L30,60 Z" fill="rgba(0,0,150,0.1)" />
                                        <path d="M75,30 L75,90 L120,120 L120,60 Z" fill="rgba(255,255,255,0.1)" />
                                        <path d="M75,30 L75,90 L30,120 L30,60 Z" fill="rgba(0,0,50,0.2)" />
                                        <path d="M75,90 L120,120 L75,150 L30,120 Z" fill="rgba(0,0,100,0.1)" />
                                        {/* Edges */}
                                        <path d="M75,30 L120,60 M120,60 L120,120 M120,120 L75,150 M75,150 L30,120 M30,120 L30,60 M30,60 L75,30 M75,30 L75,90 M75,90 L120,120 M75,90 L30,120" />
                                    </g>
                                </svg>
                            </div>

                            {/* 3D Platform */}
                            <div className="absolute bottom-0 left-1/2 transform -translate-x-1/2 z-20">
                                <svg width="200" height="100" viewBox="0 0 200 100">
                                    <rect x="10" y="20" width="180" height="60" rx="15" fill="rgba(0,100,255,0.3)" stroke="white" strokeWidth="2" />
                                    <rect x="20" y="10" width="160" height="60" rx="10" fill="rgba(100,200,255,0.4)" stroke="white" strokeWidth="2" />
                                </svg>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}