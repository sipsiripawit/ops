import '@styles/globals.css';
import { Noto_Sans_Thai } from 'next/font/google';
import { AuthProvider } from '@/hooks/useAuth';


export const metadata = {
    title: 'YIP E-INVENTORY',
    description: 'ระบบจัดการคลังสินค้าและทรัพยากร',
};

const thaiFont = Noto_Sans_Thai({
    weight: ['400', '700'], // เลือกน้ำหนักฟอนต์ที่ต้องการ
    subsets: ['thai'],
    display: 'swap',
});

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="th">
        <body className={thaiFont.className}>
        <AuthProvider>
            {children}
        </AuthProvider>
        </body>
        </html>
    );
}
