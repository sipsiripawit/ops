'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import { Eye, EyeOff, AtSign, User, Phone, Lock } from 'lucide-react';
import { authService } from '@/services/auth-service';

export default function RegisterPage() {
    const router = useRouter();
    const [formData, setFormData] = useState({
        username: '',
        fullName: '',
        email: '',
        phoneNumber: '',
        password: '',
        confirmPassword: '',
    });
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const validateForm = (): boolean => {
        // ตรวจสอบว่ากรอกข้อมูลครบทุกฟิลด์หรือไม่
        if (
            !formData.username ||
            !formData.email ||
            !formData.password ||
            !formData.confirmPassword
        ) {
            setError('กรุณากรอกข้อมูลให้ครบถ้วน');
            return false;
        }

        // ตรวจสอบรูปแบบอีเมล
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) {
            setError('รูปแบบอีเมลไม่ถูกต้อง');
            return false;
        }

        // ตรวจสอบว่ารหัสผ่านและยืนยันรหัสผ่านตรงกันหรือไม่
        if (formData.password !== formData.confirmPassword) {
            setError('รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน');
            return false;
        }

        // ตรวจสอบความซับซ้อนของรหัสผ่าน
        if (formData.password.length < 8) {
            setError('รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร');
            return false;
        }

        if (!/[A-Z]/.test(formData.password)) {
            setError('รหัสผ่านต้องมีตัวอักษรพิมพ์ใหญ่อย่างน้อย 1 ตัว');
            return false;
        }

        if (!/[0-9]/.test(formData.password)) {
            setError('รหัสผ่านต้องมีตัวเลขอย่างน้อย 1 ตัว');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // ตรวจสอบความถูกต้องของข้อมูล
        if (!validateForm()) {
            return;
        }

        try {
            setIsLoading(true);
            setError('');

            // สร้างข้อมูลสำหรับส่งไปยัง API
            const registerData = {
                username: formData.username,
                fullName: formData.fullName,
                email: formData.email,
                phoneNumber: formData.phoneNumber,
                password: formData.password,
            };

            // เรียกใช้ API สำหรับสมัครสมาชิก (สมมติว่ามีฟังก์ชัน register ใน authService)
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registerData),
            });

            const data = await response.json();

            if (response.ok) {
                // หากสำเร็จให้ redirect ไปหน้า login
                router.push('/login?registered=true');
            } else {
                setError(data.message || 'เกิดข้อผิดพลาดในการสมัครสมาชิก');
            }
        } catch (err: any) {
            console.error('Register error:', err);
            setError('เกิดข้อผิดพลาดในการเชื่อมต่อกับเซิร์ฟเวอร์');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex h-screen w-full">
            {/* Left Section - Register Form */}
            <div className="w-full md:w-1/2 flex flex-col justify-between p-8 overflow-auto">
                {/* Logo */}
                <div className="mb-8">
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

                {/* Register Form */}
                <div className="flex-grow flex flex-col justify-center max-w-md">
                    <h2 className="text-2xl font-bold mb-6">สมัครสมาชิก</h2>

                    <form onSubmit={handleSubmit}>
                        {error && (
                            <div className="bg-red-50 text-red-500 p-3 rounded-md mb-4">
                                {error}
                            </div>
                        )}

                        <div className="mb-4">
                            <label htmlFor="username" className="block text-sm font-medium mb-1">
                                ชื่อผู้ใช้งาน <span className="text-red-500">*</span>
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <User size={18} />
                </span>
                                <input
                                    id="username"
                                    name="username"
                                    type="text"
                                    value={formData.username}
                                    onChange={handleChange}
                                    className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="first.las"
                                    disabled={isLoading}
                                />
                            </div>
                            <p className="text-xs text-gray-500 mt-1">
                                ชื่อภาษาอังกฤษ ตามด้วย "." และนามสกุล 3 ตัวแรก
                            </p>
                        </div>

                        <div className="mb-4">
                            <label htmlFor="fullName" className="block text-sm font-medium mb-1">
                                ชื่อ-นามสกุล
                            </label>
                            <input
                                id="fullName"
                                name="fullName"
                                type="text"
                                value={formData.fullName}
                                onChange={handleChange}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="ชื่อ นามสกุล"
                                disabled={isLoading}
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="email" className="block text-sm font-medium mb-1">
                                อีเมล <span className="text-red-500">*</span>
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <AtSign size={18} />
                </span>
                                <input
                                    id="email"
                                    name="email"
                                    type="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="example@yipintsoi.com"
                                    disabled={isLoading}
                                />
                            </div>
                        </div>

                        <div className="mb-4">
                            <label htmlFor="phoneNumber" className="block text-sm font-medium mb-1">
                                เบอร์โทรศัพท์
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <Phone size={18} />
                </span>
                                <input
                                    id="phoneNumber"
                                    name="phoneNumber"
                                    type="tel"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                    className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="0812345678"
                                    disabled={isLoading}
                                />
                            </div>
                        </div>

                        <div className="mb-4">
                            <label htmlFor="password" className="block text-sm font-medium mb-1">
                                รหัสผ่าน <span className="text-red-500">*</span>
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <Lock size={18} />
                </span>
                                <input
                                    id="password"
                                    name="password"
                                    type={showPassword ? "text" : "password"}
                                    value={formData.password}
                                    onChange={handleChange}
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
                            <label htmlFor="confirmPassword" className="block text-sm font-medium mb-1">
                                ยืนยันรหัสผ่าน <span className="text-red-500">*</span>
                            </label>
                            <div className="relative">
                <span className="absolute inset-y-0 left-3 flex items-center text-gray-400">
                  <Lock size={18} />
                </span>
                                <input
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    type={showPassword ? "text" : "password"}
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
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
                            <div>
                                มีบัญชีผู้ใช้แล้ว?{' '}
                                <Link href="/login" className="text-blue-600 hover:text-blue-800">
                                    เข้าสู่ระบบ
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
                            {isLoading ? 'กำลังดำเนินการ...' : 'สมัครสมาชิก'}
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
