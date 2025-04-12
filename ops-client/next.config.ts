/** @type {import('next').NextConfig} */

const nextConfig = {
    eslint: {
        // บังคับให้มองข้ามข้อผิดพลาด ESLint ระหว่างการ build
        ignoreDuringBuilds: true,
    },
    typescript: {
        // บังคับให้มองข้ามข้อผิดพลาด TypeScript ระหว่างการ build
        ignoreBuildErrors: true,
    },
}

export default nextConfig;
