import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

// เส้นทางที่ไม่ต้องตรวจสอบการเข้าสู่ระบบ
const publicPaths = ['/', '/login', '/register', '/forgot-password', '/reset-password'];

export function middleware(request: NextRequest) {
    const { pathname } = request.nextUrl;

    // ตรวจสอบว่าเป็นเส้นทางสาธารณะหรือไม่
    const isPublicPath = publicPaths.some(path =>
        pathname === path || pathname.startsWith(`${path}/`)
    );

    // ตรวจสอบว่ามี token หรือไม่
    const token = request.cookies.get('accessToken')?.value;

    // ถ้าไม่มี token และไม่ใช่เส้นทางสาธารณะ ให้ redirect ไปหน้า login
    if (!token && !isPublicPath) {
        const url = new URL('/login', request.url);
        url.searchParams.set('from', pathname);
        return NextResponse.redirect(url);
    }

    // ถ้ามี token และเป็นเส้นทางสาธารณะ (เช่น /login) ให้ redirect ไปหน้า dashboard
    if (token && (pathname === '/login' || pathname === '/register')) {
        return NextResponse.redirect(new URL('/dashboard', request.url));
    }

    return NextResponse.next();
}

export const config = {
    // เส้นทางที่ต้องผ่าน middleware
    matcher: [
        /*
         * Match all paths except:
         * 1. /api routes
         * 2. /_next (Next.js internals)
         * 3. /_static (inside /public)
         * 4. /_vercel (Vercel internals)
         * 5. /favicon.ico, /robots.txt, etc.
         */
        '/((?!api|_next|_static|_vercel|[\\w-]+\\.\\w+).*)',
    ],
};